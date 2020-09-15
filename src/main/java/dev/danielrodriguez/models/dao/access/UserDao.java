package dev.danielrodriguez.models.dao.access;

import dev.danielrodriguez.models.entities.access.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserDao extends CrudRepository<User, Integer> {
    Optional<User> findByUserNameAndPassword(String username, String password);
    Optional<User> findByUserName(String username);
    Optional<User> findByEmail(String email);
}
