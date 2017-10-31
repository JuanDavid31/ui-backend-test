package Exceptions;

/**
 * Exception especifica para un nullPointerException en entidades
 */
public class EntidadNoExisteException extends Exception{

    public EntidadNoExisteException(String mensaje){
        super(mensaje);
    }
}
