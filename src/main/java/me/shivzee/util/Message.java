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
import java.util.ArrayList;
import java.util.Map;

import static me.shivzee.util.Utility.parseToDefaultTimeZone;

/**
 * The Message class represents an email message in the system.
 * <p>
 * This class encapsulates all information about an email message, including sender and recipient
 * details, content, attachments, and various metadata. It provides methods to access and manage
 * message properties.
 * </p>
 * <p>
 * For more information about the API, see <a href="https://api.mail.tm">API Documentation</a>.
 * </p>
 */
public class Message {

    private final Logger LOG = LoggerFactory.getLogger(Message.class);

    private String id ;
    private String msgid;
    private Sender from;
    private Object to;
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
     * Gets the message ID.
     *
     * @return the unique identifier of the message
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the message ID from the email headers.
     * <p>
     * This is the Message-ID field from the email headers, which is used for email threading
     * and tracking.
     * </p>
     *
     * @return the message ID from the email headers
     */
    public String getMsgid() {
        return msgid;
    }

    /**
     * Gets the sender's email address.
     *
     * @return the email address of the sender
     */
    public String getSenderAddress() {
        return from.getAddress();
    }

    /**
     * Gets the sender's display name.
     *
     * @return the display name of the sender
     */
    public String getSenderName() {
        return from.getName();
    }

    /**
     * Gets the list of recipients.
     *
     * @return the list of recipients to whom the email was sent
     */
    public List<Receiver> getReceivers() {
        List<Receiver> receivers = new ArrayList<>();

        if (to instanceof List) {
            List<?> toList = (List<?>) to;
            for (Object item : toList) {
                if (item instanceof String) {
                    // Handle string format: "email@example.com"
                    Receiver receiver = new Receiver();
                    receiver.setAddress((String) item);
                    receiver.setName("");
                    receivers.add(receiver);
                } else if (item instanceof Map) {
                    // Handle object format: {"address": "email", "name": "name"}
                    Map<?, ?> map = (Map<?, ?>) item;
                    Receiver receiver = new Receiver();
                    receiver.setAddress((String) map.get("address"));
                    receiver.setName((String) map.get("name"));
                    receivers.add(receiver);
                }
            }
        }

        return receivers;
    }

    /**
     * Gets the email subject.
     *
     * @return the subject line of the email
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Gets the plain text content of the email.
     *
     * @return the plain text version of the email content
     */
    public String getContent() {
        return text;
    }

    /**
     * Checks if the message has been read.
     *
     * @return {@code true} if the message has been read; {@code false} otherwise
     */
    public boolean isSeen() {
        return seen;
    }

    /**
     * Checks if the message has been flagged.
     *
     * @return {@code true} if the message has been flagged; {@code false} otherwise
     */
    public boolean isFlagged() {
        return flagged;
    }

    /**
     * Checks if the message has been deleted.
     *
     * @return {@code true} if the message has been deleted; {@code false} otherwise
     */
    public boolean isDeleted() {
        return isDeleted;
    }

    /**
     * Gets the retention status of the message.
     *
     * @return {@code true} if the message is retained; {@code false} otherwise
     */
    public boolean retention() {
        return retention;
    }

    /**
     * Gets the retention date of the message.
     *
     * @return the date when the message will be retained until
     */
    public String getRetentionDate() {
        return retentionDate;
    }

    /**
     * Gets the HTML content of the email.
     *
     * @return the list of HTML parts of the email content
     */
    public String getRawHTML() {
        return html.toString();
    }

    /**
     * Checks if the message has attachments.
     *
     * @return {@code true} if the message has attachments; {@code false} otherwise
     */
    public boolean hasAttachments() {
        return hasAttachments;
    }

    /**
     * Gets the list of attachments.
     *
     * @return the list of attachments in the message
     */
    public List<Attachment> getAttachments() {
        return attachments;
    }

    /**
     * Gets the size of the message in bytes.
     *
     * @return the size of the message
     */
    public long getSize() {
        return size;
    }

    /**
     * Gets the download URL for the message.
     *
     * @return the URL where the message can be downloaded
     */
    public String getDownloadUrl() {
        return downloadUrl;
    }

    /**
     * Gets the creation timestamp of the message.
     *
     * @return the date and time when the message was created
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * Get the Message Received Date/Time in ZonedDateTime format
     * @return the date at which the message was sent/created/received
     * @throws DateTimeParserException when fail to parse
     */
    public ZonedDateTime getCreatedDateTime() throws DateTimeParserException {
        return parseToDefaultTimeZone(createdAt, "yyyy-MM-dd'T'HH:mm:ss'+00:00'");
    }

    /**
     * Get the Message Update Date/Time in  ZonedDateTime format
     * @return the date on which the message was updated (markAsRead fires the update event)
     * @throws DateTimeParserException when fail to parse
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
     * @return {@code true} if the message was successfully deleted; {@code false} otherwise
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
