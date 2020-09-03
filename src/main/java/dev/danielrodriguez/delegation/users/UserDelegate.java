package dev.danielrodriguez.delegation.users;

import dev.danielrodriguez.exceptions.users.UserException;
import dev.danielrodriguez.models.dao.access.UserDao;
import dev.danielrodriguez.models.entities.access.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Interfaz de logica de negocio de usuario
 */
public interface UserDelegate {
    User createUser(User user) throws UserException;
    User updateUser(User user) throws UserException;
    void deleteUser(User user) throws UserException;
    List<User> getUsers();
    User getUser(Integer id) throws UserException;
}
