package me.shivzee.exceptions;

/**
 * Thrown When the Domain is Not Found or Does not Exists
 */
public class DomainNotFoundException extends Exception{

    public DomainNotFoundException(String errorMessage){
        super(errorMessage);
    }
    
    /***
     * Domain is not found, because of Exception cause(mostly JSON-parsing related)
     * @param errorMessage
     * @param cause
     */
    public DomainNotFoundException(String errorMessage, Throwable cause){
        super(errorMessage, cause);
    }
}
