package dev.danielrodriguez.exceptions.access;

public class AccessException extends Exception{
    private boolean suppressStacktrace = false;
    public AccessException(String message, boolean suppressStacktrace){
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
