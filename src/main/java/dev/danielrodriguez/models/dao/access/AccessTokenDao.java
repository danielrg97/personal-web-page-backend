package dev.danielrodriguez.models.dao.access;

import dev.danielrodriguez.models.entities.access.AccessToken;
import dev.danielrodriguez.models.entities.access.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AccessTokenDao extends CrudRepository<AccessToken, Integer> {
    List<AccessToken> findAccessTokenByToken(String token);
    List<AccessToken> findAccessTokenByUser(User user);
}
