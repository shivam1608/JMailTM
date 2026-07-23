package me.shivzee.exceptions;

/**
 * Thrown when something goes wrong while fetching messages
 */
public class MessageFetchException extends Exception {
	private static final long serialVersionUID = 2472147857041620904L;

	public MessageFetchException(String errorMessage){
        super(errorMessage);
    }
}
