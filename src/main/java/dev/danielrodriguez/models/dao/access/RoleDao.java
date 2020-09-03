package dev.danielrodriguez.models.dao.access;

import dev.danielrodriguez.models.entities.access.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleDao extends CrudRepository<Role, Integer> {
}
