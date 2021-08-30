package me.shivzee.exceptions;

/**
 * Thrown when something goes wrong while fetching messages
 */
public class MessageFetchException extends Exception {
    public MessageFetchException(String errorMessage){
        super(errorMessage);
    }
}
