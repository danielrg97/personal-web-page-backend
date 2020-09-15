package dev.danielrodriguez.delegation.users;

import dev.danielrodriguez.exceptions.ApplicationException;
import dev.danielrodriguez.exceptions.access.RoleException;
import dev.danielrodriguez.exceptions.users.ExistingResourceException;
import dev.danielrodriguez.exceptions.users.UserException;
import dev.danielrodriguez.exceptions.users.UserNotFoundException;
import dev.danielrodriguez.models.dao.access.RoleDao;
import dev.danielrodriguez.models.dao.access.UserDao;
import dev.danielrodriguez.models.dto.access.UserRegister;
import dev.danielrodriguez.models.entities.access.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserDelegateImpl implements UserDelegate{
    private UserDao userDao;
    private PasswordEncoder passwordEncoder;
    private RoleDao roleDao;

    /**
     * Constructor para inyecciones de dependencias
     * @param userDao
     * @param passwordEncoder
     */
    @Autowired
    public UserDelegateImpl(UserDao userDao, PasswordEncoder passwordEncoder, RoleDao roleDao){
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.roleDao = roleDao;
    }

    @Override
    public User createUser(UserRegister user) throws ApplicationException{
        if(userDao.findByEmail(user.getEmail()).isPresent()){
            throw new ExistingResourceException("El email ya fué registrado", true);
        }
        if(userDao.findByUserName(user.getUserName()).isPresent()){
            throw new ExistingResourceException("El username ya fue registrado",true);
        }
        User newUser= setValuesToEntity(user);
        return userDao.save(newUser);
    }

    @Override
    public User updateUser(UserRegister user) throws ApplicationException {
        try {
            //Busca por id
            User userToSave = userDao.findById(user.getId()).orElseThrow(() -> new UserNotFoundException("Usuario no encotrado", true));
            userToSave = setValuesToEntity(user, userToSave);
            return userDao.save(userToSave);
        }catch (UserNotFoundException e){
            //Si no lo encuentra por id, busca por username
            User userToSave = userDao.findByUserName(user.getUserName()).orElseThrow(() -> new UserNotFoundException("Usuario no encotrado", true));
            userToSave = setValuesToEntity(user, userToSave);
            return userDao.save(userToSave);
        }
    }
    private User setValuesToEntity(UserRegister user) throws RoleException {
        User userToSave = new User();
        userToSave.setNames(user.getNames());
        userToSave.setLastNames(user.getLastNames());
        userToSave.setUserName(user.getUserName());
        userToSave.setEmail(user.getEmail());
        userToSave.setPassword(passwordEncoder.encode(user.getPassword()));
        userToSave.setRole(roleDao.findById(2).orElseThrow(()->new RoleException("Rol no existente", true)));
        return userToSave;
    }
    private User setValuesToEntity(UserRegister user, User userToSave) {
        userToSave.setNames(user.getNames());
        userToSave.setLastNames(user.getLastNames());
        userToSave.setUserName(user.getUserName());
        userToSave.setEmail(user.getEmail());
        return userToSave;
    }

    @Override
    public void deleteUser(User user) throws UserException {
        try{
            userDao.delete(user);
        }catch (Exception e){
            throw new UserException("Error al eliminar usuario", false);
        }
    }

    @Override
    public List<User> getUsers() {
        return StreamSupport.stream(userDao.findAll().spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public User getUser(Integer id) throws UserException {
        return userDao.findById(id).orElseThrow(() -> new UserException("Usuario no encontrado", true));
    }
}
