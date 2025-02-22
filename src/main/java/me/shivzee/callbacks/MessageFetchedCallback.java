package me.shivzee.callbacks;

import me.shivzee.util.Message;
import me.shivzee.util.Response;

import java.util.List;

/**
 * Interface for handling callbacks when messages are fetched from a source.
 */
public interface MessageFetchedCallback {

    /**
     * Called when messages are successfully fetched.
     *
     * @param messages the list of fetched messages
     * @see me.shivzee.util.Message
     */
    void onMessagesFetched(List<Message> messages);

    /**
     * Called when an error occurs during message fetching.
     *
     * @param error the error response indicating the reason for the failure
     * @see me.shivzee.util.Response
     */
    void onError(Response error);
}
