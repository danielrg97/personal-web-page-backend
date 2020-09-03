package dev.danielrodriguez.exceptions.users;

public class UserException extends Exception {
    private boolean suppressStacktrace = false;
    public UserException(String message, boolean suppressStacktrace){
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
