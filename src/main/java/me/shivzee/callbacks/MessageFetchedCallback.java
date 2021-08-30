package me.shivzee.callbacks;

import me.shivzee.util.Message;
import me.shivzee.util.Response;

import java.util.List;

/**
 * Then Interface for Callback When Message is Fetched From the source
 */
public interface MessageFetchedCallback {
    void onMessagesFetched(List<Message> messages);
    void onError(Response error);
}
