package dev.danielrodriguez.exceptions.access;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class TokenNotFoundException extends AccessException{
    public TokenNotFoundException(String message, boolean suppressStacktrace) {
        super(message, suppressStacktrace);
    }
}
