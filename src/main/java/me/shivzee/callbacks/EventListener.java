package me.shivzee.callbacks;

import me.shivzee.util.Account;
import me.shivzee.util.Message;

/**
 * EventListener Interface to listen to Events : delete,received, etc
 */
public interface EventListener {
    default void onReady(){}
    default void onClose(){}
    default void onSSEComment(String comment){}
    default void onMessageReceived(Message message){}
    default void onMessageDelete(String id){}
    default void onMessageSeen(Message message){}
    default void onAccountDelete(Account account){}
    void onError(String error);
}
