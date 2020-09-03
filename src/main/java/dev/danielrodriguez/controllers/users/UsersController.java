package dev.danielrodriguez.controllers.users;

import dev.danielrodriguez.delegation.users.UserDelegate;
import dev.danielrodriguez.exceptions.users.UserException;
import dev.danielrodriguez.models.entities.access.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@Controller
public class UsersController {

    private UserDelegate userDelegate;

    @Autowired
    public UsersController(UserDelegate userDelegate){
        this.userDelegate = userDelegate;
    }

    @RolesAllowed("admin")
    @PostMapping(path = "/create-user", produces = "application/json")
    @ResponseBody
    public User createUser(@RequestBody User user) throws UserException {
        return userDelegate.createUser(user);
    }

    @RolesAllowed("admin")
    @PutMapping(path = "/update-user", produces = "application/json")
    @ResponseBody
    public User updateUser(@RequestBody User user) throws UserException {
        return userDelegate.updateUser(user);
    }

    @RolesAllowed("admin")
    @DeleteMapping(path = "/delete-user")
    public void deleteUser(@RequestBody User user) throws UserException {
        userDelegate.deleteUser(user);
    }

    @RolesAllowed("admin")
    @GetMapping(path = "/get-all-users")
    @ResponseBody
    public List<User> getAllUsers() throws UserException {
        return userDelegate.getUsers();
    }

    @RolesAllowed("admin")
    @GetMapping(path = "/get-user-by-id")
    @ResponseBody
    public User getAllUsers(Integer id) throws UserException {
        return userDelegate.getUser(id);
    }
}
