package dev.danielrodriguez.models.dao.access;

import dev.danielrodriguez.models.entities.access.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserDao extends CrudRepository<User, Integer> {
    List<User> findByUserNameAndPassword(String username, String password);
    List<User> findByUserName(String username);
}
