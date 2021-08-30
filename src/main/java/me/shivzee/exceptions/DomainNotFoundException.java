package me.shivzee.exceptions;

/**
 * Thrown When the Domain is Not Found or Does not Exists
 */
public class DomainNotFoundException extends Exception{

    public DomainNotFoundException(String errorMessage){
        super(errorMessage);
    }
}
