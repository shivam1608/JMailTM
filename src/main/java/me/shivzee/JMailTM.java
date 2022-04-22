/*
 * The Following is the JavaWrapper Class for Mail.tm API.
 * OpenSource Library (Make PullRequest if you got something good)
 * MIT Licence
 * Author Shivzee
 * Github : https://github.com/shivam1608
 * Discord : Shivam#8010
 */

package me.shivzee;

import me.shivzee.callbacks.MessageFetchedCallback;
import me.shivzee.callbacks.MessageListener;
import me.shivzee.callbacks.WorkCallback;
import me.shivzee.exceptions.AccountNotFoundException;
import me.shivzee.exceptions.MessageFetchException;
import me.shivzee.io.IO;
import me.shivzee.util.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/***
 * The JMailTM Class which have the instance of the API
 * @author shivzee
 */
public class JMailTM {


    private String bearerToken;
    private String id;

    private static final String baseUrl = Config.BASEURL;
    private static final JSONParser parser = Config.parser;

    /**
     * Constructor to be initialised by JMailBuilder
     * @see me.shivzee.util.JMailBuilder
     */
    public JMailTM(String bearerToken , String id){
        this.bearerToken = bearerToken;
        this.id = id;
    }

    private static Account mailUtility(JSONObject json){
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

    private Message messageUtility(JSONObject json) throws ParseException {
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
        JSONArray attachmentArray = (JSONArray) parser.parse(json.get("attachments").toString());
        Object [] aArray = attachmentArray.toArray();

        for(Object attachmentObject : aArray){
            JSONObject object = (JSONObject) attachmentObject;
            String aId = object.get("id").toString();
            String aFilename = object.get("filename").toString();
            String aContentType = object.get("contentType").toString();
            String aDisposition = object.get("disposition").toString();
            String aTransferEncoding = object.get("transferEncoding").toString();
            boolean aRelated = (boolean) object.get("related");
            long aSize = Long.parseLong(object.get("size").toString());
            String aDownloadUrl = object.get("downloadUrl").toString();
            attachments.add(new Attachment(aId , aFilename , aContentType , aDisposition, aTransferEncoding ,aRelated ,aSize , aDownloadUrl ,bearerToken));
        }

        long size = Long.parseLong(json.get("size").toString());
        String downloadUrl = json.get("downloadUrl").toString();
        String createdAt = json.get("createdAt").toString();
        String updatedAt = json.get("updatedAt").toString();

        return new Message(id,msgid,senderAddress,senderName,receivers,subject,content,seen,flagged,isDeleted,retention,retentionDate,rawHTML,hasAttachments,attachments,size,downloadUrl,createdAt,updatedAt,bearerToken,json.toJSONString());

    }

    /**
     * Get the ID of the User Account
     * @return String
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
     * Get the SelfAccount (SelfUser)
     * @return me.shivzee.util.Account
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
            System.out.println("|NO LOGGER| Something Went Wrong! Contact Developer "+e);
        }
        return new Account();
    }


    /**
     * (Asynchronous) Deletes the Self Account
     */
    public void delete(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    IO.requestDELETE(baseUrl+"/accounts/"+id , bearerToken);
                }catch (Exception e){
                    System.out.println("|EXCEPTION IGNORE | "+e);
                }
            }
        }).start();
    }

    /**
     * (Asynchronous) Deletes the Self Account with a Callback Status.
     * Lambda Expression Works
     * <code>delete((status)->{ if(status) print true; });</code>
     * @param callback The WorkCallback Lambda Function or Using new WorkCallback()
     */
    public void delete(WorkCallback callback){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Response response = IO.requestDELETE(baseUrl+"/accounts/"+id , bearerToken);
                    callback.workStatus(response.getResponseCode() == 204);
                }catch (Exception e){
                    callback.workStatus(false);
                }
            }
        }).start();
    }

    /**
     * Gets a UserAccount using UserID
     *
     * @param id
     * @return me.shivzee.util.Account
     * @see me.shivzee.util.Account
     * @throws me.shivzee.exceptions.AccountNotFoundException
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
            throw new AccountNotFoundException("Something went wrong!");
        }
    }

    /**
     * Get the Total Number of Messages
     *
     * @return int
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
     * @return me.shivzee.util.Message
     * @see me.shivzee.util.Message
     * @throws MessageFetchException
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
            throw new MessageFetchException("Error While Fetching Messages "+e);
        }
    }

    /**
     * (Synchronous) Fetches All The Messages
     * Get Messages By Callback
     *
     * <code>fetchMessages(new MessageFetchedCallback(){
     * @Override
     * public void onMessagesFetched(List<Message> messages){}
     *
     * })</code>
     *
     * @see me.shivzee.callbacks.MessageFetchedCallback
     * @see me.shivzee.exceptions.MessageFetchException
     * @param callback The MessageFetchedCallback Implemented Class/Function
     * @throws MessageFetchException
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
            throw new MessageFetchException("Error While Fetching Messages "+e);
        }
    }

    /**
     * (Synchronous) Fetches First (Limit) Messages
     *
     * @see me.shivzee.callbacks.MessageFetchedCallback
     * @see me.shivzee.exceptions.MessageFetchException
     * @param limit The Total Number of Message to Fetch (Starts from The TOP)
     * @param callback The MessageFetchedCallback Implemented Class/Function
     * @throws MessageFetchException
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
            throw new MessageFetchException("Error While Fetching Messages "+e);
        }
    }

    /**
     * (Asynchronous) Fetches All The Messages
     * @see me.shivzee.callbacks.MessageFetchedCallback
     * @param callback MessageFetchedCallback Implemented Class
     */
    public void asyncFetchMessages(MessageFetchedCallback callback){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    fetchMessages(callback);
                } catch (MessageFetchException e) {
                    callback.onError(new Response(90001 , "Lib Error Exception Caught | UPDATE LIB | ASYNC IGNORE. Exception"+e) );
                }
            }
        }).start();
    }

    /**
     * (Asynchronous) Fetches First (Limit) Messages
     * @see me.shivzee.callbacks.MessageFetchedCallback
     * @param limit The Total Number of Message to Fetch (Starts from The TOP)
     * @param callback MessageFetchedCallback Implemented Class
     */
    public void asyncFetchMessages(int limit , MessageFetchedCallback callback){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    fetchMessages(limit , callback);
                } catch (MessageFetchException e) {
                    callback.onError(new Response(90001 , "Lib Error Exception Caught | UPDATE LIB | ASYNC IGNORE. Exception"+e) );
                }
            }
        }).start();
    }



    private String messageID = "";

    /**
     * (Asynchronous) Opens a Message Listener on a New Thread
     * @param messageListener MessageListener Implemented Class
     * @param refreshInterval The Refresh Time for Fetching Messages
     */
    public void openMessageListener(MessageListener messageListener , long refreshInterval){
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    fetchMessages(1, new MessageFetchedCallback() {
                        @Override
                        public void onMessagesFetched(List<Message> messages) {
                            if(messageID.equals("") && messages.size() == 0){
                                messageID = "";
                            }else if(!messageID.equals(messages.get(0).getId())) {
                                messageListener.onMessageReceived(messages.get(0));
                                messageID = messages.get(0).getId();
                            }

                        }

                        @Override
                        public void onError(Response error) {
                            messageListener.onError(error.getResponse());
                        }
                    });
                } catch (MessageFetchException e) {
                    messageListener.onError(""+e);
                }
            }
        } , 0L , refreshInterval);
    }

    /**
     * (Asynchronous) Opens a MessageListener on a New Thread Default Refresh Time 1.5 seconds
     * @see me.shivzee.callbacks.MessageListener
     * @param messageListener MessageListener Implemented Class
     */
    public void openMessageListener(MessageListener messageListener){
        openMessageListener(messageListener , 1500L);
    }






}