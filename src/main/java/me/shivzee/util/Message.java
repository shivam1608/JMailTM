package me.shivzee.util;

import me.shivzee.Config;
import me.shivzee.callbacks.WorkCallback;
import me.shivzee.io.IO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * The Message Class to Wrap Emails Received
 * Check https://api.mail.tm for more info
 */
public class Message {

    private final Logger LOG = LoggerFactory.getLogger(Message.class);

    private String id ;
    private String msgid;
    private String senderAddress;
    private String senderName;
    private List<Receiver> receivers;
    private String subject;
    private String content;
    private boolean seen;
    private boolean flagged;
    private boolean isDeleted;
    private boolean retention;
    private String retentionDate;
    private String rawHTML;
    private boolean hasAttachments;
    private List<Attachment> attachments;
    private long size;
    private String downloadUrl;
    private String createdAt;
    private String updatedAt;
    private String bearerToken;
    private String rawJson;

    public Message(String id, String msgid, String senderAddress, String senderName, List<Receiver> receivers, String subject, String content, boolean seen, boolean flagged, boolean isDeleted, boolean retention, String retentionDate, String rawHTML, boolean hasAttachments, List<Attachment> attachments, long size, String downloadUrl, String createdAt, String updatedAt, String bearerToken, String rawJson) {
        this.id = id;
        this.msgid = msgid;
        this.senderAddress = senderAddress;
        this.senderName = senderName;
        this.receivers = receivers;
        this.subject = subject;
        this.content = content;
        this.seen = seen;
        this.flagged = flagged;
        this.isDeleted = isDeleted;
        this.retention = retention;
        this.retentionDate = retentionDate;
        this.rawHTML = rawHTML;
        this.hasAttachments = hasAttachments;
        this.attachments = attachments;
        this.size = size;
        this.downloadUrl = downloadUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.bearerToken = bearerToken;
    }

    /**
     * Get the Email/Message ID
     * @return the id of the message
     */
    public String getId() {
        return id;
    }

    /**
     * Get the MSGID
     * @link {https://api.mail.tm} Check for more info
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
        return senderAddress;
    }

    /**
     * Get the Sender's Name
     * @return the email sender's name
     */
    public String getSenderName() {
        return senderName;
    }

    /**
     * Get all the Receivers/send To
     * @return the list of all receivers to whom the email was sent (simply carbon copy cc: tag)
     */
    public List<Receiver> getReceivers() {
        return receivers;
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
        return content;
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
        return rawHTML;
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
        return rawJson;
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
