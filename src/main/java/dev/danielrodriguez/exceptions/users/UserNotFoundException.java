package dev.danielrodriguez.exceptions.users;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends UserException{
    public UserNotFoundException(String message, boolean suppressStacktrace) {
        super(message, suppressStacktrace);
    }
}
