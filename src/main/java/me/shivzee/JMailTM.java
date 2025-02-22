/*
 * OpenSource Library
 * MIT Licence
 * Author shivzee & Community
 * Github : https://github.com/shivam1608
 * Discord : Shivam#8010
 *
 * Copyright 2022 shivzee & contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.shivzee;

import com.google.gson.*;
import com.launchdarkly.eventsource.EventSource;
import me.shivzee.adapters.TokenAdapter;
import me.shivzee.callbacks.EventListener;
import me.shivzee.callbacks.MessageFetchedCallback;
import me.shivzee.callbacks.MessageListener;
import me.shivzee.callbacks.WorkCallback;
import me.shivzee.exceptions.AccountNotFoundException;
import me.shivzee.exceptions.MessageFetchException;
import me.shivzee.io.IO;
import me.shivzee.io.IOCallback;
import me.shivzee.util.*;
import okhttp3.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



/***
 * The JMailTM Class which have the instance of the API
 * @author shivzee
 * @see JMailBuilder
 */
public class JMailTM {


    private String bearerToken;
    private String id;
    private final Gson gson;

    private static final String baseUrl = Config.BASEURL;
    private final Logger LOG = LoggerFactory.getLogger(JMailTM.class);

    private ExecutorService pool = Executors.newSingleThreadExecutor();

    /**
     * Constructs a new {@code JMailTM} instance with the specified bearer token and ID.
     * <p>
     * This constructor is intended to be initialized by the {@code JMailBuilder} class.
     * It sets up the necessary authentication using the provided bearer token and
     * configures a Gson instance with a custom type adapter for handling token-related
     * data.
     * </p>
     *
     * @param bearerToken the bearer token used for authentication.
     * @param id the unique identifier for the JMailTM instance.
     * @see me.shivzee.util.JMailBuilder
     */
    public JMailTM(String bearerToken , String id){
        this.bearerToken = bearerToken;
        this.id = id;

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapterFactory(new TokenAdapter(bearerToken));
        this.gson = gsonBuilder.create();

    }


    /**
     * Retrieves the id of the user account
     * @return the id of the user
     */
    public String getId(){
        return this.id;
    }

    /**
     * Initializes the {@code JMailTM} instance by performing necessary setup operations.
     * <p>
     * This method fetches and updates the available domains for the instance. It should be
     * called to ensure that the {@code JMailTM} instance is properly configured and ready
     * to use.
     * This is only required when using {@code createDefault()}
     * </p>
     */
    public void init(){
        Domains.updateDomains();
    }

    /**
     * Retrieves the account instance of the logged-in user.
     *
     * @return the account instance of the logged-in user.
     * @see me.shivzee.util.Account
     */
    public Account getSelf(){
        try{
            Response response = IO.requestGET(baseUrl + "/me", bearerToken);
            if(response.getResponseCode() == 200){
                return gson.fromJson(response.getResponse() , Account.class);
            }
        }catch (Exception e){
            LOG.error(e.toString());
        }
        return new Account();
    }


    /**
     * (Synchronous) Deletes the self account in a <b>synchronous</b> manner.
     * <p>
     * This method attempts to delete the account of the logged-in user.
     * It returns {@code true} if the account is successfully deleted, otherwise {@code false}.
     * </p>
     *
     * @return {@code true} if the account is successfully deleted; {@code false} otherwise.
     */
	public boolean delete() {
	    if(getSelf().isDeleted()){
	        return true;
        }
		try {
            Response response = IO.requestDELETE(baseUrl + "/accounts/" + id, bearerToken);
            return response.getResponseCode() == 204;
		} catch (Exception e) {
		    LOG.error(e.toString());
			return false;
		}
	}

    /**
     * (Synchronous) Deletes the self account and provides a callback with the status of the operation.
     * <p>
     * This method attempts to delete the account of the logged-in user and invokes the provided callback
     * with the status of the deletion operation. The callback receives {@code true} if the account is
     * successfully deleted, otherwise {@code false}.
     * </p>
     * <p>
     * Example usage:
     * <pre>
     * delete((status) -> {
     *     if (status) {
     *         System.out.println("Account deleted successfully.");
     *     } else {
     *         System.out.println("Failed to delete account.");
     *     }
     * });
     * </pre>
     * </p>
     *
     * @param callback the {@code WorkCallback} lambda function or an instance created using {@code new WorkCallback()}.
     */
    public void delete(WorkCallback callback){
        callback.workStatus(delete());
    }

    /**
     * (Asynchronous) Deletes the self account and provides a callback with the status of the operation.
     * <p>
     * This method attempts to delete the account of the logged-in user asynchronously and invokes the provided callback
     * with the status of the deletion operation. The callback receives {@code true} if the account is
     * successfully deleted, otherwise {@code false}.
     * </p>
     * <p>
     * Example usage:
     * <pre>
     * asyncDelete((status) -> {
     *     if (status) {
     *         System.out.println("Account deleted successfully.");
     *     } else {
     *         System.out.println("Failed to delete account.");
     *     }
     * });
     * </pre>
     * </p>
     *
     * @param callback the {@code WorkCallback} lambda function or an instance created using {@code new WorkCallback()}.
     */

    public void asyncDelete(WorkCallback callback){
        new Thread(() -> { callback.workStatus(delete()); }, "Delete_Account_" + id).start();
    }


    /**
     * (Asynchronous) Initiates the deletion of the self account in a separate thread.
     * <p>
     * This method starts a new thread to delete the account of the logged-in user asynchronously.
     * </p>
     */
    public void asyncDelete(){
        new Thread(this::delete, "Delete_Account_" + id).start();
    }



    /**
     * Retrieves a user account using the specified user ID.
     *
     * @param id the user ID of the account to retrieve.
     * @return the {@code Account} object associated with the given user ID.
     * @throws AccountNotFoundException if the account with the specified ID is not found or an error occurs during retrieval.
     * @see me.shivzee.util.Account
     */
    public Account getAccountById(String id) throws AccountNotFoundException {
        try{

            Response response = IO.requestGET(baseUrl+"/accounts/"+id , bearerToken);
            if(response.getResponseCode() == 200){
                return gson.fromJson(response.getResponse() , Account.class);
            }else {
                throw new AccountNotFoundException("Invalid account id. Response : "+response.getResponse());
            }

        }catch (Exception e){
            throw new AccountNotFoundException(e.toString());
        }
    }

    /**
     * Retrieves the total number of messages in the user's inbox.
     *
     * @return the total number of messages.
     */
    public int getTotalMessages(){
        try{
            Response response = IO.requestGET(baseUrl+"/messages" , bearerToken);
            JsonArray array = JsonParser.parseString(response.getResponse()).getAsJsonArray();
            return array.size();
        }catch (Exception e){
            return 0;
        }
    }

    /**
     * Retrieves a single message object using the specified message ID.
     *
     * @param id the message ID of the message to retrieve.
     * @return the {@code Message} object corresponding to the given message ID.
     * @throws MessageFetchException if the message with the specified ID cannot be fetched due to an error.
     * @see me.shivzee.util.Message
     */
    public Message getMessageById(String id) throws MessageFetchException{
        try{
            Response response = IO.requestGET(baseUrl+"/messages/"+id , bearerToken);
            if(response.getResponseCode() == 200){
                return gson.fromJson(response.getResponse() , Message.class);
            }else {
                throw new MessageFetchException("Invalid message id. Response : "+response.getResponse());
            }
        }catch (Exception e){
            throw new MessageFetchException(e.toString());
        }
    }

    /**
     * (Synchronous) Fetches all messages and invokes a callback with the fetched messages or an error response.
     * <p>
     * This method synchronously retrieves all messages from the server using a GET request and invokes the provided
     * {@code MessageFetchedCallback} with either a list of fetched messages or an error response. It handles exceptions
     * internally and throws a {@code MessageFetchException} if fetching fails for any reason.
     * </p>
     * <p>
     * Example usage:
     * <pre>
     * fetchMessages(new MessageFetchedCallback() {
     *     \@Override
     *     public void onMessagesFetched(List<Message> messages) {
     *         // Process fetched messages
     *     }
     *
     *     \@Override
     *     public void onError(Response errorResponse) {
     *         // Handle error
     *     }
     * });
     * </pre>
     * </p>
     *
     * @param callback the {@code MessageFetchedCallback} implementation to receive the fetched messages or handle errors.
     * @throws MessageFetchException if there's an error while fetching messages from the server.
     * @see me.shivzee.callbacks.MessageFetchedCallback
     * @see me.shivzee.exceptions.MessageFetchException
     */
    public void fetchMessages(MessageFetchedCallback callback) throws MessageFetchException{
        try{
            List<Message> messages = new ArrayList<>();
            Response response = IO.requestGET(baseUrl+"/messages" , bearerToken);
            if(response.getResponseCode() == 200){
                JsonArray array = JsonParser.parseString(response.getResponse()).getAsJsonArray();
                for(JsonElement object : array){
                    messages.add(getMessageById(object.getAsJsonObject().get("id").getAsString()));
                }

                callback.onMessagesFetched(messages);

            }else {
                callback.onError(new Response(response.getResponseCode() , response.getResponse()));
            }
        }
        catch (MessageFetchException e){
            throw e;
        }
        catch (Exception e) {
            throw new MessageFetchException(e.toString());
        }
    }

    /**
     * (Synchronous) Fetches the first {@code limit} number of messages and invokes a callback with the fetched messages or an error response.
     * <p>
     * This method synchronously retrieves the first {@code limit} number of messages from the server using a GET request
     * and invokes the provided {@code MessageFetchedCallback} with either a list of fetched messages or an error response.
     * It handles exceptions internally and throws a {@code MessageFetchException} if fetching fails for any reason.
     * </p>
     * <p>
     * Example usage:
     * <pre>
     * fetchMessages(10, new MessageFetchedCallback() {
     *     \@Override
     *     public void onMessagesFetched(List<Message> messages) {
     *         // Process fetched messages
     *     }
     *
     *     \@Override
     *     public void onError(Response errorResponse) {
     *         // Handle error
     *     }
     * });
     * </pre>
     * </p>
     *
     * @param limit    the maximum number of messages to fetch from the top of the list.
     * @param callback the {@code MessageFetchedCallback} implementation to receive the fetched messages or handle errors.
     * @throws MessageFetchException if there's an error while fetching messages from the server.
     * @see me.shivzee.callbacks.MessageFetchedCallback
     * @see me.shivzee.exceptions.MessageFetchException
     */
    public void fetchMessages(int limit , MessageFetchedCallback callback) throws MessageFetchException{
        try{
            List<Message> messages = new ArrayList<>();
            Response response = IO.requestGET(baseUrl+"/messages" , bearerToken);
            if(response.getResponseCode() == 200){
                JsonArray array = JsonParser.parseString(response.getResponse()).getAsJsonArray();
                int stop = Math.min(array.size(), limit);
                for(int i=0;i<stop;i++){
                    messages.add(getMessageById(array.get(i).getAsJsonObject().get("id").getAsString()));
                }

                callback.onMessagesFetched(messages);

            }else {
                callback.onError(new Response(response.getResponseCode() , response.getResponse()));
            }
        }
        catch (MessageFetchException e){
            throw e;
        }
        catch (Exception e) {
            throw new MessageFetchException(e.toString());
        }
    }

    /**
     * (Asynchronous) Initiates the fetching of all messages and invokes a callback with the fetched messages or an error response.
     * <p>
     * This method asynchronously initiates the fetching of all messages from the server using a separate thread.
     * It invokes the provided {@code MessageFetchedCallback} with either a list of fetched messages or an error response
     * once the messages are retrieved. If there's an error during the fetch process, it handles it internally and
     * invokes the callback's {@code onError} method.
     * </p>
     * <p>
     * Example usage:
     * <pre>
     * asyncFetchMessages(new MessageFetchedCallback() {
     *     \@Override
     *     public void onMessagesFetched(List<Message> messages) {
     *         // Process fetched messages
     *     }
     *
     *     \@Override
     *     public void onError(Response errorResponse) {
     *         // Handle error
     *     }
     * });
     * </pre>
     * </p>
     *
     * @param callback the {@code MessageFetchedCallback} implementation to receive the fetched messages or handle errors.
     * @see me.shivzee.callbacks.MessageFetchedCallback
     */
    public void asyncFetchMessages(MessageFetchedCallback callback){
        new Thread(()->{
            try {
                fetchMessages(callback);
            } catch (MessageFetchException e) {
                callback.onError(new Response(90001 , e.toString()) );
            }
        }, "Fetch_Messages_" + id).start();
    }

    /**
     * (Asynchronous) Initiates the fetching of the first {@code limit} number of messages and invokes a callback with the fetched messages or an error response.
     * <p>
     * This method asynchronously initiates the fetching of the first {@code limit} number of messages from the server using a separate thread.
     * It invokes the provided {@code MessageFetchedCallback} with either a list of fetched messages or an error response once the messages are retrieved.
     * If there's an error during the fetch process, it handles it internally and invokes the callback's {@code onError} method.
     * </p>
     * <p>
     * Example usage:
     * <pre>
     * asyncFetchMessages(10, new MessageFetchedCallback() {
     *     \@Override
     *     public void onMessagesFetched(List<Message> messages) {
     *         // Process fetched messages
     *     }
     *
     *     \@Override
     *     public void onError(Response errorResponse) {
     *         // Handle error
     *     }
     * });
     * </pre>
     * </p>
     *
     * @param limit    the maximum number of messages to fetch from the top of the list.
     * @param callback the {@code MessageFetchedCallback} implementation to receive the fetched messages or handle errors.
     * @see me.shivzee.callbacks.MessageFetchedCallback
     */
    public void asyncFetchMessages(int limit , MessageFetchedCallback callback){
        new Thread(()->{
            try {
                fetchMessages(limit , callback);
            } catch (MessageFetchException e) {
                callback.onError(new Response(90001 , e.toString()) );
            }
        }, "Fetch_Messages_" + id).start();
    }


    /**
     * (Asynchronous) Opens an event listener on a single thread to receive server-sent events (SSE).
     * <p>
     * This method asynchronously opens an event listener using SSE (Server-Sent Events) on a single thread.
     * It initializes an {@code EventSource} with the provided {@code EventListener} implementation and connects
     * to the specified MERCURE_URL topic associated with the user account. It handles reconnecting to the server
     * in case of disconnection with the specified {@code retryInterval}.
     * </p>
     * <p>
     * Example usage:
     * <pre>
     * openEventListener(new EventListener() {
     *
     *     \@Override
     *     public void onReady() {
     *         // Handle error
     *     }
     * }, 5000); // Retry every 5 seconds if disconnected
     * </pre>
     * </p>
     *
     * @param eventListener the {@code EventListener} implementation to handle incoming events and errors.
     * @param retryInterval the reconnect timeout interval in milliseconds if the server disconnects unexpectedly.
     */
    public void openEventListener(EventListener eventListener , long retryInterval){
        if(pool.isShutdown()){
            pool = Executors.newSingleThreadExecutor();
        }
        Map<String , String> headers = new HashMap<>();
        headers.put("Authorization" , "Bearer "+bearerToken);
        EventSource.Builder sse = new EventSource.Builder(new IOCallback(eventListener , this), URI.create(Config.MERCURE_URL+"?topic=/accounts/"+id))
                .reconnectTime(Duration.ofMillis(retryInterval))
                .headers(Headers.of(headers));
        EventSource sourceSSE = sse.build();
        pool.execute(sourceSSE::start);
    }

    /**
     * (Asynchronous) Open's a default event listener on a single thread
     * @param eventListener EventListener implemented class
     */
    public void openEventListener(EventListener eventListener){
        openEventListener(eventListener , 3000L);
    }

    /**
     * Closes the message listener, shutting down the thread pool used for event handling.
     * <p>
     * This method shuts down the thread pool used for handling events, effectively stopping any ongoing
     * event processing or listeners initiated by this instance.
     * </p>
     */
    public void closeMessageListener(){
        pool.shutdown();
    }



    /**
     * (Asynchronous) Opens a Message Listener on a New Thread
     * @param messageListener MessageListener Implemented Class
     * @param retryInterval The Refresh Time for Fetching Messages
     */
    @Deprecated
    public void openMessageListener(MessageListener messageListener , long retryInterval){

        openEventListener(new EventListener() {
            @Override
            public void onReady() {
                messageListener.onReady();
            }

            @Override
            public void onClose() {
                messageListener.onClose();
            }

            @Override
            public void onMessageReceived(Message message) {
                messageListener.onMessageReceived(message);
            }

            @Override
            public void onError(String error) {
                messageListener.onError(error);
            }
        }, retryInterval);
    }

    /**
     * (Asynchronous) Opens a MessageListener on a New Thread Default Refresh Time 1.5 seconds
     * @see me.shivzee.callbacks.MessageListener
     * @param messageListener MessageListener Implemented Class
     */
    @Deprecated
    public void openMessageListener(MessageListener messageListener){
        openMessageListener(messageListener , 3000);
    }

    /**
     * (Synchronous) Deletes the Self Account
     * @return Boolean
     */
    @Deprecated
    public boolean deleteSync(){
        return delete();
    }

    /**
     * (Synchronous) Deletes the Self Account
     * @return Boolean
     * @param callback The work status callback
     */
    @Deprecated
    public void deleteSync(WorkCallback callback){
        callback.workStatus(deleteSync());
    }





}