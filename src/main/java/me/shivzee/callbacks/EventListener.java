package me.shivzee.callbacks;

import me.shivzee.util.Account;
import me.shivzee.util.Message;

/**
 * Interface for handling various events such as message deletion, reception, account updates, and errors.
 */
public interface EventListener {
    /**
     * Invoked when the listener is ready to start receiving events.
     */
    default void onReady(){}


    /**
     * Invoked when the listener is closed or stopped.
     */
    default void onClose(){}

    /**
     * Invoked when a comment is received as part of SSE (Server-Sent Events).
     *
     * @param comment the SSE comment received
     */
    default void onSSEComment(String comment){}

    /**
     * Invoked when a new message is received.
     *
     * @param message the received message
     * @see me.shivzee.util.Message
     */
    default void onMessageReceived(Message message){}

    /**
     * Invoked when a message is deleted.
     *
     * @param id the ID of the deleted message
     */
    default void onMessageDelete(String id){}

    /**
     * Invoked when a message is seen by the recipient.
     *
     * @param message the seen message
     * @see me.shivzee.util.Message
     */
    default void onMessageSeen(Message message){}

    /**
     * Invoked when an account is deleted.
     *
     * @param account the deleted account
     * @see me.shivzee.util.Account
     */
    default void onAccountDelete(Account account){}

    /**
     * Invoked when an account is updated.
     *
     * @param account the updated account
     * @see me.shivzee.util.Account
     */
    default void onAccountUpdate(Account account){}

    /**
     * Invoked when an error occurs.
     *
     * @param error the error message
     */
    void onError(String error);
}
