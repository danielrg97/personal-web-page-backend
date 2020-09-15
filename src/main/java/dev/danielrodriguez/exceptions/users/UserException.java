package dev.danielrodriguez.exceptions.users;

import dev.danielrodriguez.exceptions.ApplicationException;

public class UserException extends ApplicationException {

    public UserException(String message, boolean suppressStacktrace) {
        super(message, suppressStacktrace);
    }
}
