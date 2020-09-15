package dev.danielrodriguez.models.dao.access;

import dev.danielrodriguez.models.entities.access.AccessToken;
import dev.danielrodriguez.models.entities.access.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AccessTokenDao extends CrudRepository<AccessToken, Integer> {
    Optional<AccessToken> findAccessTokenByToken(String token);
    List<AccessToken> findAccessTokenByUser(User user);
}
