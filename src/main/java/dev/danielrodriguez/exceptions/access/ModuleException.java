package dev.danielrodriguez.exceptions.access;

import dev.danielrodriguez.exceptions.ApplicationException;

/**
 * Excepcion para casos de uso de Modulos del aplicativo
 */
public class ModuleException extends ApplicationException {

    public ModuleException(String message, boolean suppressStacktrace) {
        super(message, suppressStacktrace);
    }
}
