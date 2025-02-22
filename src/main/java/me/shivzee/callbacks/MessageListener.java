package me.shivzee.callbacks;

import me.shivzee.util.Message;

/**
 * The Message Listener Callback. Runs when some new message or email has arrived
 */
@Deprecated
public interface MessageListener {
    default void onReady(){
        // Do What you want
    }
    default void onClose(){
        // Do What you want
    }
    void onMessageReceived(Message message);
    void onError(String error);
}
