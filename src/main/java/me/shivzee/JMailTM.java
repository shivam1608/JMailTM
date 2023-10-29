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

import com.launchdarkly.eventsource.EventSource;
import me.shivzee.callbacks.EventListener;
import me.shivzee.callbacks.MessageFetchedCallback;
import me.shivzee.callbacks.MessageListener;
import me.shivzee.callbacks.WorkCallback;
import me.shivzee.exceptions.AccountNotFoundException;
import me.shivzee.exceptions.DateTimeParserException;
import me.shivzee.exceptions.MessageFetchException;
import me.shivzee.io.IO;
import me.shivzee.io.IOCallback;
import me.shivzee.util.*;
import okhttp3.Headers;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.time.Duration;
import java.time.ZonedDateTime;
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

    private static final String baseUrl = Config.BASEURL;
    private static final JSONParser parser = Config.parser;
    private final Logger LOG = LoggerFactory.getLogger(JMailTM.class);

    private ExecutorService pool = Executors.newSingleThreadExecutor();

    /**
     * Constructor to be initialised by JMailBuilder
     * @see me.shivzee.util.JMailBuilder
     */
    public JMailTM(String bearerToken , String id){
        this.bearerToken = bearerToken;
        this.id = id;
    }

    private static Account mailUtility(JSONObject json) {
        String id = json.get("id").toString();
        String email = json.get("address").toString();
        String quota = json.get("quota").toString();
        String used = json.get("used").toString();
        boolean isDisabled = (boolean) json.get("isDisabled");
        boolean isDeleted = (boolean) json.get("isDeleted");
        String createdAt = json.get("createdAt").toString();
        String updatedAt = json.get("updatedAt").toString();
        return new Account(id,email,quota,used,isDisabled,isDeleted,createdAt,updatedAt);
    }

    private Message messageUtility(JSONObject json) throws ParseException, DateTimeParserException {
        String id = json.get("id").toString();
        String msgid = json.get("msgid").toString();
        JSONObject from = (JSONObject) parser.parse(json.get("from").toString());
        String senderAddress = from.get("address").toString();
        String senderName = from.get("name").toString();

        List<Receiver> receivers = new ArrayList<>();
        JSONArray receiverArray = (JSONArray) parser.parse(json.get("to").toString());
        Object [] rArray = receiverArray.toArray();
        for(Object jsonObject : rArray){
            JSONObject object = (JSONObject) jsonObject;
            receivers.add(new Receiver(object.get("address").toString() , object.get("name").toString()));
        }
        String subject = json.get("subject").toString();
        String content = json.get("text").toString();
        boolean seen = (boolean) json.get("seen");
        boolean flagged = (boolean) json.get("flagged");
        boolean isDeleted = (boolean) json.get("isDeleted");
        boolean retention = (boolean) json.get("retention");
        String retentionDate = json.get("retentionDate").toString();
        String rawHTML = json.get("html").toString();
        boolean hasAttachments = (boolean) json.get("hasAttachments");

        List<Attachment> attachments = new ArrayList<>();
        if(hasAttachments) {
            JSONArray attachmentArray = (JSONArray) parser.parse(json.get("attachments").toString());
            Object[] aArray = attachmentArray.toArray();

            for (Object attachmentObject : aArray) {
                JSONObject object = (JSONObject) attachmentObject;
                String aId = object.get("id").toString();
                String aFilename = object.get("filename").toString();
                String aContentType = object.get("contentType").toString();
                String aDisposition = object.get("disposition").toString();
                String aTransferEncoding = object.get("transferEncoding").toString();
                boolean aRelated = (boolean) object.get("related");
                long aSize = Long.parseLong(object.get("size").toString());
                String aDownloadUrl = object.get("downloadUrl").toString();
                attachments.add(new Attachment(aId, aFilename, aContentType, aDisposition, aTransferEncoding, aRelated, aSize, aDownloadUrl, bearerToken));
            }
        }

        long size = Long.parseLong(json.get("size").toString());
        String downloadUrl = json.get("downloadUrl").toString();
        String createdAt = json.get("createdAt").toString();
        String updatedAt = json.get("updatedAt").toString();

        ZonedDateTime createdDateTime = Utility.parseToDefaultTimeZone(createdAt,"yyyy-MM-dd'T'HH:mm:ss'+00:00'");
        ZonedDateTime updatedDateTime = Utility.parseToDefaultTimeZone(updatedAt,"yyyy-MM-dd'T'HH:mm:ss'+00:00'");

        return new Message(id,msgid,senderAddress,senderName,receivers,subject,content,seen,flagged,isDeleted,retention,retentionDate,rawHTML,hasAttachments,attachments,size,downloadUrl,createdAt,createdDateTime,updatedAt,updatedDateTime,bearerToken,json.toJSONString());
    }

    /**
     * Get the ID of the User Account
     * @return the id of the user
     */
    public String getId(){
        return this.id;
    }

    /**
     * Initialise JMailTM (Fetches Domains and Etc)
     */
    public void init(){
        Domains.updateDomains();
    }

    /**
     * Get the SelfAccount
     * @return the account instance of the logged in user
     * @see me.shivzee.util.Account
     */
    public Account getSelf(){
        try{
            Response response = IO.requestGET(baseUrl + "/me", bearerToken);
            if(response.getResponseCode() == 200){
                JSONObject json = (JSONObject) parser.parse(response.getResponse());
                return mailUtility(json);
            }
        }catch (Exception e){
            LOG.error(e+"");
        }
        return new Account();
    }


	/**
	 * (Synchronous) Deletes the Self Account
     * @return true if user successfully deleted else false
	 */
	public boolean delete() {
	    if(getSelf().isDeleted()){
	        return true;
        }
		try {
            Response response = IO.requestDELETE(baseUrl + "/accounts/" + id, bearerToken);
            return response.getResponseCode() == 204;
		} catch (Exception e) {
		    LOG.error(e+"");
			return false;
		}
	}

    /**
     * (Synchronous) Deletes the Self Account with a Callback Status.
     * <br />
     * <code>delete((status)->{ if(status) print true; });</code>
     * @param callback The WorkCallback Lambda Function or Using new WorkCallback()
     */
    public void delete(WorkCallback callback){
        callback.workStatus(delete());
    }

    /**
     * (Asynchronous) Deletes the Self Account with a Callback Status.
     * <br />
     * <code>delete((status)->{ if(status) print true; });</code>
     * @param callback The WorkCallback Lambda Function or Using new WorkCallback()
     */
    public void asyncDelete(WorkCallback callback){
        new Thread(() -> { callback.workStatus(delete()); }, "Delete_Account_" + id).start();
    }


    /**
     * (Asynchronous) Deletes the Self Account
     */
    public void asyncDelete(){
        new Thread(this::delete, "Delete_Account_" + id).start();
    }



    /**
     * Gets a UserAccount using UserID
     *
     * @param id the user id of the user
     * @return account object of the user id
     * @see me.shivzee.util.Account
     * @throws me.shivzee.exceptions.AccountNotFoundException when account you're looking for is not found
     */
    public Account getAccountById(String id) throws AccountNotFoundException {
        try{

            Response response = IO.requestGET(baseUrl+"/accounts/"+id , bearerToken);
            if(response.getResponseCode() == 200){
                JSONObject json = (JSONObject) parser.parse(response.getResponse());
                return mailUtility(json);
            }else {
                throw new AccountNotFoundException("Invalid ID");
            }

        }catch (Exception e){
            throw new AccountNotFoundException(""+e);
        }
    }

    /**
     * Get the Total Number of Messages
     *
     * @return the total messages in user's inbox
     */
    public int getTotalMessages(){
        try{
            Response response = IO.requestGET(baseUrl+"/messages/"+id , bearerToken);
            JSONArray array = (JSONArray) parser.parse(response.getResponse());
            return array.size();
        }catch (Exception e){
            return 0;
        }
    }

    /**
     * Get Message by using MessageID
     * @param id The Message ID
     * @return the single message object
     * @see me.shivzee.util.Message
     * @throws MessageFetchException when failed to message due to some reason
     */
    public Message getMessageById(String id) throws MessageFetchException{
        try{
            Response response = IO.requestGET(baseUrl+"/messages/"+id , bearerToken);
            if(response.getResponseCode() == 200){
                JSONObject json = (JSONObject) parser.parse(response.getResponse());
                return messageUtility(json);
            }else {
                throw new MessageFetchException("Invalid Message ID");
            }
        }catch (Exception e){
            throw new MessageFetchException(""+e);
        }
    }

    /**
     * (Synchronous) Fetches All The Messages
     * Get Messages By Callback
     * <br />
     * <code>
     * fetchMessages(new MessageFetchedCallback(){
     *  @Override
     *  public void onMessagesFetched(List<Message> messages){}
     *
     *  });
     * </code>
     *
     * @see me.shivzee.callbacks.MessageFetchedCallback
     * @see me.shivzee.exceptions.MessageFetchException
     * @param callback The MessageFetchedCallback Implemented Class/Function
     * @throws MessageFetchException when fails to fetch any message due to any reason
     */
    public void fetchMessages(MessageFetchedCallback callback) throws MessageFetchException{
        try{
            List<Message> messages = new ArrayList<>();
            Response response = IO.requestGET(baseUrl+"/messages" , bearerToken);
            if(response.getResponseCode() == 200){
                JSONArray array = (JSONArray) parser.parse(response.getResponse());
                Object [] json = array.toArray();
                for(Object object : json){
                    JSONObject jsonObject = (JSONObject) object;
                    messages.add(getMessageById(jsonObject.get("id").toString()));
                }

                callback.onMessagesFetched(messages);

            }else {
                callback.onError(new Response(response.getResponseCode() , response.getResponse()));
            }
        } catch (Exception e) {
            throw new MessageFetchException(""+e);
        }
    }

    /**
     * (Synchronous) Fetches First (Limit) Messages
     *
     * @see me.shivzee.callbacks.MessageFetchedCallback
     * @see me.shivzee.exceptions.MessageFetchException
     * @param limit The Total Number of Message to Fetch (Starts from The TOP)
     * @param callback The MessageFetchedCallback Implemented Class/Function
     * @throws MessageFetchException when fails to fetch any message due to any reason
     */
    public void fetchMessages(int limit , MessageFetchedCallback callback) throws MessageFetchException{
        try{
            List<Message> messages = new ArrayList<>();
            Response response = IO.requestGET(baseUrl+"/messages" , bearerToken);
            if(response.getResponseCode() == 200){
                JSONArray array = (JSONArray) parser.parse(response.getResponse());
                Object [] json = array.toArray();
                int stop = Math.min(json.length, limit);
                for(int i=0;i<stop;i++){
                    JSONObject jsonObject = (JSONObject) json[i];
                    messages.add(getMessageById(jsonObject.get("id").toString()));
                }

                callback.onMessagesFetched(messages);

            }else {
                callback.onError(new Response(response.getResponseCode() , response.getResponse()));
            }
        } catch (Exception e) {
            throw new MessageFetchException(""+e);
        }
    }

    /**
     * (Asynchronous) Fetches All The Messages
     * @see me.shivzee.callbacks.MessageFetchedCallback
     * @param callback MessageFetchedCallback Implemented Class
     */
    public void asyncFetchMessages(MessageFetchedCallback callback){
        new Thread(()->{
            try {
                fetchMessages(callback);
            } catch (MessageFetchException e) {
                callback.onError(new Response(90001 , ""+e) );
            }
        }, "Fetch_Messages_" + id).start();
    }

    /**
     * (Asynchronous) Fetches First (Limit) Messages
     * @see me.shivzee.callbacks.MessageFetchedCallback
     * @param limit The Total Number of Message to Fetch (Starts from The TOP)
     * @param callback MessageFetchedCallback Implemented Class
     */
    public void asyncFetchMessages(int limit , MessageFetchedCallback callback){
        new Thread(()->{
            try {
                fetchMessages(limit , callback);
            } catch (MessageFetchException e) {
                callback.onError(new Response(90001 , ""+e) );
            }
        }, "Fetch_Messages_" + id).start();
    }


    /**
     * (Asynchronous) Open's an event listener on a single thread
     * @param eventListener EventListener implemented class
     * @param retryInterval The reconnect timeout if server disconnects by chance
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
     * (Asynchronous) Open's an event listener on a single thread
     * @param eventListener EventListener implemented class
     */
    public void openEventListener(EventListener eventListener){
        openEventListener(eventListener , 3000L);
    }

    /**
     * Closes the Message Listener
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