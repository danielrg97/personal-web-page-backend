package dev.danielrodriguez.exceptions.access;

import dev.danielrodriguez.exceptions.ApplicationException;

/**
 * Excepcion para casos de uso de Roles de los usuarios
 */
public class RoleException extends ApplicationException {

    public RoleException(String message, boolean suppressStacktrace) {
        super(message, suppressStacktrace);
    }
}
