package me.shivzee.exceptions;

/**
 * Thrown When an Account is not found during login
 */
public class AccountNotFoundException extends Exception{
    public AccountNotFoundException(String errorMessage){
        super(errorMessage);
    }
}
