package me.shivzee.util;


import me.shivzee.callbacks.WorkCallback;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;


/**
 * The Attachment Class to Wrap Attachments
 * Check @link{https://api.mail.tm} for more info
 */
public class Attachment {
    private String id;
    private String filename;
    private String contentType;
    private String disposition;
    private String transferEncoding;
    private boolean related;
    private long size;
    private String downloadUrl;
    private String bearerToken;

    public Attachment(String id, String filename, String contentType, String disposition, String transferEncoding, boolean related, long size, String downloadUrl, String bearerToken) {
        this.id = id;
        this.filename = filename;
        this.contentType = contentType;
        this.disposition = disposition;
        this.transferEncoding = transferEncoding;
        this.related = related;
        this.size = size;
        this.downloadUrl = downloadUrl;
        this.bearerToken = bearerToken;
    }

    /**
     * Get the Attachment ID
     * @return String
     */
    public String getId() {
        return id;
    }

    /**
     * Get the Filename
     * @return String
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Get the ContentType of Attachment
     * @return String
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * Get The Disposition
     * @return
     */
    public String getDisposition() {
        return disposition;
    }

    /**
     * Get TransferEncoding Type
     * @return String
     */
    public String getTransferEncoding() {
        return transferEncoding;
    }

    /**
     * Get Related Status
     * @return boolean
     */
    public boolean isRelated() {
        return related;
    }

    /**
     * Get the Attachment Size in Killobytes
     * @return long
     */
    public long getSize() {
        return size;
    }

    /**
     * Get the Download URL for the attachment (Will not work without JWT Token as AuthHead) Check the Attachment.save() Method
     * @return String
     */
    public String getDownloadUrl() {
        return "https://api.mail.tm"+downloadUrl;
    }


    /**
     * Save the Attachment on System
     * @param path The Path to Save the File eg("C:/Data/Downloads/")
     * @param filename The File Name of The Attachment
     * @param callback The WorkCallback to know the Download Status
     * @see me.shivzee.callbacks.WorkCallback
     */
    public void save(String path , String filename , WorkCallback callback){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    URL attachmentUrl = new URL(getDownloadUrl());
                    HttpURLConnection connection = (HttpURLConnection) attachmentUrl.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Authorization" , "Bearer "+bearerToken);
                    if(connection.getResponseCode() == 200) {
                        Files.copy(connection.getInputStream(), Paths.get(path + filename));
                        callback.workStatus(true);
                    }else{
                        callback.workStatus(false);
                    }
                }catch (Exception e){
                    callback.workStatus(false);
                }
            }
        },"Attachment_Download").start();
    }

    /**
     * Save the Attachment in the Working Directory
     */
    public void save(){
        save("./" , getFilename() , (status)->{});
    }

    /**
     * Save the Attachment in the Working Directory with Callback Status
     * @param callback The WorkCallback for Status
     * @see me.shivzee.callbacks.WorkCallback
     */
    public void save(WorkCallback callback){
        save("./" , getFilename() , callback);
    }

    /**
     * Save the Attachment in the Working Directory With Custom Filename
     * @param filename The filename including extension
     */
    public void save(String filename){
        save("./" , filename , status -> {});
    }

    /**
     * Save the Attachment in the Working Directory With Custom Filename and Callback
     * @param filename The Filename including Extension
     * @param callback The WorkCallback for status
     * @see me.shivzee.callbacks.WorkCallback
     */
    public void save(String filename , WorkCallback callback){
        save("./", filename , callback);
    }



}
