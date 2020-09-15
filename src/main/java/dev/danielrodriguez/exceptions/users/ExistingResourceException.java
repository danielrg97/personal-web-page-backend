package dev.danielrodriguez.exceptions.users;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class ExistingResourceException extends UserException{
    public ExistingResourceException(String message, boolean suppressStacktrace) {
        super(message, suppressStacktrace);
    }
}
