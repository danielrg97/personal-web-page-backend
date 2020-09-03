package dev.danielrodriguez.exceptions.access;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepcion para casos en los que las credenciales de acceso sean invalidas
 */
@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class WrongCredentialException extends Exception{
    private boolean suppressStacktrace = false;
    public WrongCredentialException(String message, boolean suppressStacktrace){
        super(message, null, suppressStacktrace, !suppressStacktrace);
        this.suppressStacktrace = suppressStacktrace;
    }


    @Override
    public String toString() {
        if (suppressStacktrace) {
            return getLocalizedMessage();
        } else {
            return super.toString();
        }
    }
}
