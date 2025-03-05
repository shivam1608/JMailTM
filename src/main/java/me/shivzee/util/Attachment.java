package me.shivzee.util;


import me.shivzee.callbacks.WorkCallback;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;


/**
 * The Attachment class represents an email attachment.
 * <p>
 * This class wraps attachment information including metadata such as filename, content type,
 * size, and download URL. It provides methods to access attachment properties and download
 * the attachment content.
 * </p>
 * <p>
 * For more information about the API, see <a href="https://api.mail.tm">API Documentation</a>.
 * </p>
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
     * Gets the attachment ID.
     *
     * @return the ID of the attachment in the email
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the filename of the attachment.
     *
     * @return the filename of the attachment
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Gets the content type (MIME type) of the attachment.
     *
     * @return the content type of the attachment (e.g., "image/gif", "image/png")
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * Gets the disposition of the attachment.
     *
     * @return the disposition value of the attachment
     */
    public String getDisposition() {
        return disposition;
    }

    /**
     * Gets the transfer encoding type of the attachment.
     *
     * @return the transfer encoding of the attachment
     */
    public String getTransferEncoding() {
        return transferEncoding;
    }

    /**
     * Checks if the attachment is related to the email content.
     *
     * @return {@code true} if the attachment is related; {@code false} otherwise
     */
    public boolean isRelated() {
        return related;
    }

    /**
     * Gets the size of the attachment in kilobytes.
     *
     * @return the size of the attachment in KiB
     */
    public long getSize() {
        return size;
    }

    /**
     * Gets the download URL for the attachment.
     * <p>
     * Note: This URL will not work without a valid JWT token as an authorization header.
     * Use the {@link #save(String)} method to download the attachment.
     * </p>
     *
     * @return the download URL for the attachment
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
