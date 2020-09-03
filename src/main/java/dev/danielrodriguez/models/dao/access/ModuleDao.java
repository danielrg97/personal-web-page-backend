package dev.danielrodriguez.models.dao.access;
import dev.danielrodriguez.models.entities.access.Module;
import org.springframework.data.repository.CrudRepository;

public interface ModuleDao extends CrudRepository<Module, Integer> {
}
