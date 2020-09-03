package dev.danielrodriguez.delegation.access;

import dev.danielrodriguez.exceptions.access.AccessException;
import dev.danielrodriguez.exceptions.access.ModuleException;
import dev.danielrodriguez.exceptions.access.RoleException;
import dev.danielrodriguez.exceptions.access.WrongCredentialException;
import dev.danielrodriguez.models.dao.access.AccessTokenDao;
import dev.danielrodriguez.models.dao.access.ModuleDao;
import dev.danielrodriguez.models.dao.access.RoleDao;
import dev.danielrodriguez.models.dao.access.UserDao;
import dev.danielrodriguez.models.dto.access.LoginCredential;
import dev.danielrodriguez.models.entities.access.AccessToken;
import dev.danielrodriguez.models.entities.access.Module;
import dev.danielrodriguez.models.entities.access.Role;
import dev.danielrodriguez.models.entities.access.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import static dev.danielrodriguez.constants.access.SecurityConstants.KEY;


@Service
public class AccessDelegateImpl implements AccessDelegate{
    private ModuleDao moduleDao;
    private RoleDao roleDao;
    private UserDao userDao;
    private AccessTokenDao accessTokenDao;
    private PasswordEncoder passwordEncoder;
    /**
     * Constructor para inyeccion de dependencias
     * @param moduleDao
     * @param roleDao
     * @param userDao
     */
    @Autowired
    public AccessDelegateImpl(ModuleDao moduleDao, RoleDao roleDao, UserDao userDao, AccessTokenDao accessTokenDao, PasswordEncoder passwordEncoder){
        this.moduleDao = moduleDao;
        this.roleDao = roleDao;
        this.userDao = userDao;
        this.accessTokenDao = accessTokenDao;
        this.passwordEncoder =passwordEncoder;
    }

    /**
     * Metodo para autenticacion, valida si las credenciales son correctas y si lo son, crea token y
     * lo almacena en la base de datos
     * @param loginCredential DTO con usuario y contraseña
     * @return String token
     * @throws WrongCredentialException
     */
    @Override
    public AccessToken authenticate(LoginCredential loginCredential) throws WrongCredentialException {
        //Busco por nombre de usuario
        List<User> userList = userDao.findByUserName(loginCredential.getUsername());
        if(userList.size() == 0)  throw new WrongCredentialException("El usuario no existe", true);
        User user = userList.get(0);
        //Compruebo que la contraseña sea correcta
        if (!passwordEncoder.matches(loginCredential.getPassword(), user.getPassword())) throw new WrongCredentialException("Contraseña incorrecta", true);
        //Genero el token y lo retorno
        return generateToken(user);
    }
    private AccessToken generateToken(User user){
        //Si hay tokens anteriores del usuario los borro para tener un unico token
        List<AccessToken> accessTokensByUser = accessTokenDao.findAccessTokenByUser(user);
        if(accessTokensByUser.size()>0) accessTokensByUser.stream().forEach(token -> accessTokenDao.delete(token));
        String token = getJWTToken(user);
        AccessToken newToken = new AccessToken();
        newToken.setToken(token);
        newToken.setCreationDate(new Date(System.currentTimeMillis()));
        newToken.setUser(user);
        accessTokenDao.save(newToken);
        return newToken;
    }

    @Override
    public Role createRole(Role role) throws RoleException {
        try {
            return roleDao.save(role);
        }catch (Exception e){
            throw new RoleException("Error al crear rol", false);
        }
    }

    @Override
    public Module createModule(Module module) throws ModuleException{
        try {
            return moduleDao.save(module);
        }catch (Exception e){
            throw new ModuleException("Error al crear modulo del aplicativo", false);
        }
    }

    @Override
    public Role updateRole(Role role) throws RoleException {
        try {
            return roleDao.save(role);
        }catch (Exception e){
            throw new RoleException("Error al crear rol", false);
        }
    }

    @Override
    public Module updateModule(Module module) throws ModuleException {
        try {
            return moduleDao.save(module);
        }catch (Exception e){
            throw new ModuleException("Error al crear modulo del aplicativo", false);
        }
    }

    @Override
    public void deleteRole(Role role) throws RoleException {
        try {
            roleDao.delete(role);
        }catch (Exception e){
            throw new RoleException("Error al eliminar rol", false);
        }
    }

    @Override
    public void deleteModule(Module module) throws ModuleException {
        try {
            moduleDao.delete(module);
        }catch (Exception e){
            throw new ModuleException("Error al eliminar modulo del sistema", false);
        }
    }

    @Override
    public void logout(String token) throws AccessException {
        try{
            accessTokenDao.delete(accessTokenDao.findAccessTokenByToken(token).get(0));
        }catch (Exception e){
            throw new AccessException("Error al desloggearse", false);
        }
    }

    /**
     * Metodo para crear el Token de acceso
     * @param user
     * @return String token de acceso
     */
    private String getJWTToken(User user) {
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList(user.getRole().getName());

        String token = Jwts
                .builder()
                .setSubject(user.getUserName())
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 2400000))//3600))
                .signWith(SignatureAlgorithm.HS512,
                        KEY).compact();

        return  token;
    }
}
