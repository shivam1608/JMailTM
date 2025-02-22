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
    private Boolean related;
    private Long size;
    private String downloadUrl;
    private String bearerToken;


    /**
     * Get the Attachment ID
     * @return the id of the attachment in email
     */
    public String getId() {
        return id;
    }

    /**
     * Get the Filename
     * @return the filename of the attachment
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Get the ContentType of Attachment
     * @return the content-type/mime of the attachment eg(image/gif , image/png)
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * Get The Disposition
     * @return the disposition
     */
    public String getDisposition() {
        return disposition;
    }

    /**
     * Get TransferEncoding Type
     * @return the encoding of the attachment
     */
    public String getTransferEncoding() {
        return transferEncoding;
    }

    /**
     * Get Related Status
     * @return true if the attachment is related
     */
    public boolean isRelated() {
        return related;
    }

    /**
     * Get the Attachment Size in Killobytes
     * @return the size of attachment in KiB
     */
    public long getSize() {
        return size;
    }

    /**
     * Get the Download URL for the attachment (Will not work without JWT Token as AuthHead) Check the Attachment.save() Method
     * @return the download url
     */
    public String getDownloadUrl() {
        return "https://api.mail.tm"+downloadUrl;
    }

    /**
     * (Synchronous) Save the Attachment on System
     * @param path The Path to Save the File eg("C:/Data/Downloads/")
     * @param filename The File Name of The Attachment
     * @return <code>true</code> if download was successful, else <code>false</code>
     */
	public boolean saveSync(String path, String filename) {
		try {
			URL attachmentUrl = new URL(getDownloadUrl());
			HttpURLConnection connection = (HttpURLConnection) attachmentUrl.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Authorization", "Bearer " + bearerToken);
			if (connection.getResponseCode() == 200) {
				Files.copy(connection.getInputStream(), Paths.get(path + filename));
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}

	}

    /**
     * (Synchronous) Save the Attachment in the Working Directory With Custom Filename
     * @param filename The filename including extension
     * @return <code>true</code> if download was successful, else <code>false</code>
     */
    public boolean saveSync(String filename){
        return saveSync("./" , filename);
    }
    
    /**
     * (Synchronous) Save the Attachment in the Working Directory
     * @return <code>true</code> if download was successful, else <code>false</code>
     */
    public boolean saveSync(){
        return saveSync("./" , getFilename());
    }


    
    /**
     * (Asynchronous) Save the Attachment on System
     * @param path The Path to Save the File eg("C:/Data/Downloads/")
     * @param filename The File Name of The Attachment
     * @param callback The WorkCallback to know the Download Status
     * @see me.shivzee.callbacks.WorkCallback
     */
	public void save(String path, String filename, WorkCallback callback) {
		new Thread(() -> { callback.workStatus(saveSync(path, filename)); }
				, "Attachment_Download_" + id).start();
	}

    /**
     * (Asynchronous) Save the Attachment in the Working Directory
     */
    public void save(){
        save("./" , getFilename() , status -> {});
    }

    /**
     * (Asynchronous) Save the Attachment in the Working Directory with Callback Status
     * @param callback The WorkCallback for Status
     * @see me.shivzee.callbacks.WorkCallback
     */
    public void save(WorkCallback callback){
        save("./" , getFilename() , callback);
    }

    /**
     * (Asynchronous) Save the Attachment in the Working Directory With Custom Filename
     * @param filename The filename including extension
     */
    public void save(String filename){
        save("./" , filename , status -> {});
    }

    /**
     * (Asynchronous) Save the Attachment in the Working Directory With Custom Filename and Callback
     * @param filename The Filename including Extension
     * @param callback The WorkCallback for status
     * @see me.shivzee.callbacks.WorkCallback
     */
    public void save(String filename , WorkCallback callback){
        save("./", filename , callback);
    }

}
