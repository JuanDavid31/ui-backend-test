package Exceptions;

/**
 * Exception especifica para un nullPointerException en entidades
 */
public class ObjetoNoExisteException extends Exception{

    public ObjetoNoExisteException(String mensaje){
        super(mensaje);
    }
}
