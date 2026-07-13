package me.shivzee.exceptions;

/**
 * Thrown When an Account is not found during login
 */
public class AccountNotFoundException extends Exception{

	private static final long serialVersionUID = 1014727086112238318L;

	public AccountNotFoundException(String errorMessage){
        super(errorMessage);
    }
}
