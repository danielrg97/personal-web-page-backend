package dev.danielrodriguez.exceptions.access;

/**
 * Excepcion para casos de uso de Roles de los usuarios
 */
public class RoleException extends Exception{
    private boolean suppressStacktrace = false;
    public RoleException(String message, boolean suppressStacktrace){
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
