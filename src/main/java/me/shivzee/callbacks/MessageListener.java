package me.shivzee.callbacks;

import me.shivzee.util.Message;

/**
 * The Message Listener Callback. Runs when some new message or email has arrived
 */
public interface MessageListener {
    void onMessageReceived(Message message);
    void onError(String error);
}
