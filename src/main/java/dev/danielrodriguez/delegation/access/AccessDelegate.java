package dev.danielrodriguez.delegation.access;

import dev.danielrodriguez.exceptions.access.AccessException;
import dev.danielrodriguez.exceptions.access.ModuleException;
import dev.danielrodriguez.exceptions.access.RoleException;
import dev.danielrodriguez.exceptions.access.WrongCredentialException;
import dev.danielrodriguez.models.dto.access.LoginCredential;
import dev.danielrodriguez.models.entities.access.AccessToken;
import dev.danielrodriguez.models.entities.access.Role;
import dev.danielrodriguez.models.entities.access.Module;

public interface AccessDelegate {
    /**
     * Metodo de autenticacion, consulta las credenciales en la base de datos y si son correctas,
     * crea token, lo almacena en la base de datos y lo retorna al cliente.
     * @param loginCredential DTO con usuario y contraseña
     * @return Token de acceso
     * @throws WrongCredentialException
     */
    AccessToken authenticate (LoginCredential loginCredential) throws WrongCredentialException;

    Role createRole(Role role) throws RoleException;
    Module createModule(Module module) throws ModuleException;
    Role updateRole(Role role) throws RoleException;
    Module updateModule(Module module) throws ModuleException;
    void deleteRole(Role role) throws RoleException;
    void deleteModule(Module module) throws ModuleException;

    void logout(String token) throws AccessException;
}