package me.shivzee.callbacks;

import me.shivzee.util.Message;

/**
 * EventListener Interface to listen to Events : delete,received, etc
 */
public interface EventListener {
    default void onReady(){}
    default void onClose(){}
    default void onSSEComment(){}
    default void onMessageReceived(Message message){}
    default void onMessageDelete(Message message){}
    default void onMessageMarkAsRead(Message message){}
    void onError(String error);
}
