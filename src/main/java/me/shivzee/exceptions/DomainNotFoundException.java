package me.shivzee.exceptions;

/**
 * Thrown When the Domain is Not Found or Does not Exists
 * <p>
 * This exception is used by {@link me.shivzee.util.Domains} methods to indicate
 * failures in domain fetching, parsing, or lookup operations.
 
 */
public class DomainNotFoundException extends Exception{

    /**
     * Constructs a DomainNotFoundException with the specified error message.
     *
     * @param errorMessage the error message
     */
    public DomainNotFoundException(String errorMessage){
        super(errorMessage);
    }
    
    /**
     * Constructs a DomainNotFoundException with the specified detail message and cause.
     * <p>
     * This constructor is used when the failure originates from another exception,
     * such as a JSON parsing error.
     * </p>
     *
     * @param errorMessage the error message
     * @param cause the underlying cause of the exception
     */
    public DomainNotFoundException(String errorMessage, Throwable cause){
        super(errorMessage, cause);
    }
}
