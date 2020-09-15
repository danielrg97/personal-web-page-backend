package dev.danielrodriguez.controllers.users;

import dev.danielrodriguez.annotations.AllowedRole;
import dev.danielrodriguez.delegation.users.UserDelegate;
import dev.danielrodriguez.exceptions.ApplicationException;
import dev.danielrodriguez.exceptions.users.UserException;
import dev.danielrodriguez.models.dto.access.UserRegister;
import dev.danielrodriguez.models.entities.access.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UsersController {

    private UserDelegate userDelegate;

    @Autowired
    public UsersController(UserDelegate userDelegate){
        this.userDelegate = userDelegate;
    }

    @PostMapping(path = "/create-user", produces = "application/json")
    @ResponseBody
    public User createUser(@RequestBody UserRegister user) throws ApplicationException {
        return userDelegate.createUser(user);
    }

    @PutMapping(path = "/update-user", produces = "application/json")
    @ResponseBody
    public User updateUser(@RequestBody UserRegister user) throws ApplicationException {
        return userDelegate.updateUser(user);
    }

    @AllowedRole(role = "admin")
    @DeleteMapping(path = "/delete-user")
    public void deleteUser(@RequestBody User user) throws UserException {
        userDelegate.deleteUser(user);
    }

    @AllowedRole(role = "admin")
    @GetMapping(path = "/get-all-users")
    @ResponseBody
    public List<User> getAllUsers() throws UserException {
        return userDelegate.getUsers();
    }

    @AllowedRole(role = "admin")
    @GetMapping(path = "/get-user-by-id")
    @ResponseBody
    public User getAllUsers(Integer id) throws UserException {
        return userDelegate.getUser(id);
    }
}
