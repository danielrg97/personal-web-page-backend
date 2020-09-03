package dev.danielrodriguez.delegation.users;

import dev.danielrodriguez.exceptions.users.UserException;
import dev.danielrodriguez.models.dao.access.UserDao;
import dev.danielrodriguez.models.entities.access.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserDelegateImpl implements UserDelegate{
    private UserDao userDao;
    private PasswordEncoder passwordEncoder;

    /**
     * Constructor para inyecciones de dependencias
     * @param userDao
     * @param passwordEncoder
     */
    @Autowired
    public UserDelegateImpl(UserDao userDao, PasswordEncoder passwordEncoder){
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User createUser(User user) throws UserException {
        try {
            User newUser = user;
            newUser.setPassword(passwordEncoder.encode(user.getPassword()));
            return userDao.save(newUser);
        }catch (Exception e){
            throw  new UserException("Error al crear usuario", false);
        }
    }

    @Override
    public User updateUser(User user) throws UserException {
        try {
            return userDao.save(user);
        }catch (Exception e){
            throw new UserException("Error al actualizar usuario", false);
        }
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
