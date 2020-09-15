package dev.danielrodriguez.exceptions.access;

import dev.danielrodriguez.exceptions.ApplicationException;

public class AccessException extends ApplicationException {

    public AccessException(String message, boolean suppressStacktrace) {
        super(message, suppressStacktrace);
    }
}
