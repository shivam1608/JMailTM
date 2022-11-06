package me.shivzee.util;

/**
 * The User Account Class to wrap User Details
 */
public class Account {
    private String id;
    private String email;
    private String quota;
    private String used;
    private boolean isDisabled;
    private boolean isDeleted;
    private String createdAt;
    private String updatedAt;

    public Account(){
        this.id = "";
        this.email = "";
        this.quota = "";
        this.used = "";
        this.isDisabled = false;
        this.isDeleted = false;
        this.createdAt = "";
        this.updatedAt = "";
    }
    public Account(String id, String email, String quota, String used, boolean isDisabled, boolean isDeleted, String createdAt, String updatedAt) {
        this.id = id;
        this.email = email;
        this.quota = quota;
        this.used = used;
        this.isDisabled = isDisabled;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * Get the UserID
     * @return the user id of the account
     */
    public String getId() {
        return id;
    }

    /**
     * Get Email
     * @return the email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Get the Storage/Quota
     * @return the total amount of allowed storage for mails and attachments
     */
    public String getQuota() {
        return quota;
    }

    /**
     * Get the Occupied Storage/Quota of User
     * @return the amount of storage used for mails and attachments
     */
    public String getUsed() {
        return used;
    }

    /**
     * Get User Account Ban/Disabled Status
     * @return true if account is banned or disabled
     */
    public boolean isDisabled() {
        return isDisabled;
    }

    /**
     * Get User Account Delete Status
     * @return true if account is deleted from the server
     */
    public boolean isDeleted() {
        return isDeleted;
    }

    /**
     * Get the time of creation of User in String
     * @return the account creation date
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * Get the time of update of User in String
     * @return the account update date (delete, message received events triggers the account update event)
     * @see me.shivzee.callbacks.EventListener
     */
    public String getUpdatedAt() {
        return updatedAt;
    }
}
