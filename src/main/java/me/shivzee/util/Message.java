package me.shivzee.util;

import com.google.gson.Gson;
import me.shivzee.Config;
import me.shivzee.callbacks.WorkCallback;
import me.shivzee.exceptions.DateTimeParserException;
import me.shivzee.io.IO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;
import java.util.List;

import static me.shivzee.util.Utility.parseToDefaultTimeZone;

/**
 * The Message Class to Wrap Emails Received
 * Check https://api.mail.tm for more info
 */
public class Message {

    private final Logger LOG = LoggerFactory.getLogger(Message.class);

    private String id ;
    private String msgid;
    private Sender from;
    private List<Receiver> to;
    private String subject;
    private String text;
    private Boolean seen;
    private Boolean flagged;
    private Boolean isDeleted;
    private Boolean retention;
    private String retentionDate;
    private List<String> html;
    private Boolean hasAttachments;
    private List<Attachment> attachments;
    private Long size;
    private String downloadUrl;
    private String createdAt;
    private String updatedAt;
    private String bearerToken;

    /**
     * Get the Email/Message ID
     * @return the id of the message
     */
    public String getId() {
        return id;
    }

    /**
     * Get the MSGID
     * <br />
     * Check <a href="https://api.mail.tm">API Docs</a> for more info
     * @return String
     */
    public String getMsgid() {
        return msgid;
    }

    /**
     * Get the Sender's Email Address
     * @return the email sender's address
     */
    public String getSenderAddress() {
        return from.getAddress();
    }

    /**
     * Get the Sender's Name
     * @return the email sender's name
     */
    public String getSenderName() {
        return from.getName();
    }

    /**
     * Get all the Receivers/send To
     * @return the list of all receivers to whom the email was sent (simply carbon copy cc: tag)
     */
    public List<Receiver> getReceivers() {
        return to;
    }

    /**
     * Get the Email/Message Subject
     * @return the email's subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Get the Content of Email/Message
     * @return the inside content of email
     */
    public String getContent() {
        return text;
    }

    /**
     * Get the Seen Status
     * @return true if the message is marked seen
     */
    public boolean isSeen() {
        return seen;
    }

    /**
     * Get the Flagged Status
     * @return ture if the message is marked as flagged
     */
    public boolean isFlagged() {
        return flagged;
    }

    /**
     * Get the Delete Status
     * @return true if the message is deleted
     */
    public boolean isDeleted() {
        return isDeleted;
    }

    /**
     * Get retention Status
     * @return true if enabled retention
     */
    public boolean retention() {
        return retention;
    }

    /**
     * Get Retention Date in String
     * @return the retention date
     */
    public String getRetentionDate() {
        return retentionDate;
    }

    /**
     * Get the Email/Message Raw HTML Content
     * @return the raw HTML to manually parse the content of mail
     */
    public String getRawHTML() {
        return html.toString();
    }

    /**
     * Get the Status of Attachments on the Email/Message
     * @return true if email contains some attachments
     */
    public boolean hasAttachments() {
        return hasAttachments;
    }

    /**
     * Get the list of all Attachments on a Message/Email
     * @return the list of all attachments on the email
     * @see me.shivzee.util.Attachment
     */
    public List<Attachment> getAttachments() {
        return attachments;
    }

    /**
     * Get the Size of Message
     * @return the size in bytes of message content
     */
    public long getSize() {
        return size;
    }

    /**
     * Get the Download URL See API Docs for more info
     * @return the download url to download the message from server
     */
    public String getDownloadUrl() {
        return downloadUrl;
    }

    /**
     * Get the Message Received Date/Time in String
     * @return the date at which the message was sent/created/received
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * Get the Message Received Date/Time in ZonedDateTime format
     * @return the date at which the message was sent/created/received
     */
    public ZonedDateTime getCreatedDateTime() throws DateTimeParserException {
        return parseToDefaultTimeZone(createdAt, "yyyy-MM-dd'T'HH:mm:ss'+00:00'");
    }

    /**
     * Get the Message Update Date/Time in  ZonedDateTime format
     * @return the date on which the message was updated (markAsRead fires the update event)
     * @see me.shivzee.callbacks.EventListener
     */
    public ZonedDateTime getUpdatedDateTime() throws DateTimeParserException {
        return parseToDefaultTimeZone(updatedAt, "yyyy-MM-dd'T'HH:mm:ss'+00:00'");
    }

    /**
     * Get the Message Update Date/Time in String
     * @return the date on which the message was updated (markAsRead fires the update event)
     * @see me.shivzee.callbacks.EventListener
     */
    public String getUpdatedAt() {
        return updatedAt;
    }

    /**
     * (Synchronous) Deletes the Message
     * @return true if message was deleted from the server
     */
    public boolean delete(){
        if(isDeleted){
            return true;
        }
        try{
            return IO.requestDELETE(Config.BASEURL+"/messages/"+id , bearerToken).getResponseCode() == 204;
        }catch (Exception e){
            LOG.warn("Failed to Delete message "+e);
            return false;
        }
    }

    /**
     * (Synchronous) Deletes the Message with a Callback
     * @param callback The WorkCallback Implementation or Lambda Function
     */
    public void delete(WorkCallback callback){
        callback.workStatus(delete());
    }

    /**
     * (Asynchronous) Silently Deletes the Message with no response
     */
    public void asyncDelete(){
        new Thread(this::delete, "Delete_Message_" + id).start();
    }

    /**
     * (Asynchronous) Deletes the Message with a Callback
     * @param callback The WorkCallback Implementation or Lambda Function
     */
    public void asyncDelete(WorkCallback callback){
        new Thread(()->{
            callback.workStatus(delete());
        }, "Delete_Message_" + id).start();
    }

    /**
     * (Synchronous) Marks the Message/Email as Read
     * @return true if the message was marked as read on server
     */
    public boolean markAsRead() {
        if(seen){
            return true;
        }
        try {
            Response response = IO.requestPATCH(Config.BASEURL + "/messages/" + id, bearerToken);
            return response.getResponseCode() == 200;
        } catch (Exception e) {
            LOG.warn("Failed to mark message as read "+e);
            return false;
        }
    }



    /**
     * (Sync) Marks the Message/Email asRead with a Callback
     * @param callback The WorkCallback Implementation or Lambda Function
     */
    public void markAsRead(WorkCallback callback) {
        callback.workStatus(markAsRead());
    }



    /**
     * (Async) Silently Marks the Message/Email asRead with no response
     */
    public void asyncMarkAsRead(){
        new Thread(this::markAsRead, "Mark_Message_As_Read_" + id).start();
    }

    /**
     * (Async) Marks the Message/Email asRead with a Callback
     * @param callback The WorkCallback Implementation or Lambda Function
     */
    public void asyncMarkAsRead(WorkCallback callback){
        new Thread(() -> { this.markAsRead(callback); }, "Mark_Message_As_Read_" + id).start();
    }

    /**
     * Get the Raw JSON Response For Message
     * @return the raw json response to parse manually
     */
    public String getRawJson(){
        return new Gson().toJson(this);
    }

    
    /**
     * (Synchronous) Deletes the Message
     */
    @Deprecated
    public boolean deleteSync(){
        return delete();
    }

    /**
     * (Synchronous) Deletes the Message with a Callback
     * @param callback The WorkCallback Implementation or Lambda Function
     */
    @Deprecated
    public void deleteSync(WorkCallback callback){
        callback.workStatus(deleteSync());
    }


    /**
     * (Synchronous) Marks the Message/Email asRead with no response
     * @return Boolean
     */
    @Deprecated
    public boolean markAsReadSync(){
        return markAsRead();
    }

    /**
     * (Synchronous) Marks the Message/Email asRead with a Callback
     * @param callback The WorkCallback Implementation or Lambda Function
     */
    @Deprecated
    public void markAsReadSync(WorkCallback callback){
        callback.workStatus(markAsReadSync());
    }

}
