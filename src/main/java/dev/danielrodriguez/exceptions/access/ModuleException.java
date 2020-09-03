package dev.danielrodriguez.exceptions.access;

/**
 * Excepcion para casos de uso de Modulos del aplicativo
 */
public class ModuleException extends Exception{
    private boolean suppressStacktrace = false;
    public ModuleException(String message, boolean suppressStacktrace){
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
