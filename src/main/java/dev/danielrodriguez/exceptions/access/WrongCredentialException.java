package dev.danielrodriguez.exceptions.access;

import dev.danielrodriguez.exceptions.ApplicationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepcion para casos en los que las credenciales de acceso sean invalidas
 */
@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class WrongCredentialException extends ApplicationException {

    public WrongCredentialException(String message, boolean suppressStacktrace) {
        super(message, suppressStacktrace);
    }
}
