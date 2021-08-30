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
     * @return String
     */
    public String getId() {
        return id;
    }

    /**
     * Get Email
     * @return String
     */
    public String getEmail() {
        return email;
    }

    /**
     * Get the Storage/Quota
     * @return String
     */
    public String getQuota() {
        return quota;
    }

    /**
     * Get the Occupied Storage/Quota of User
     * @return String
     */
    public String getUsed() {
        return used;
    }

    /**
     * Get User Account Ban/Disabled Status
     * @return boolean
     */
    public boolean isDisabled() {
        return isDisabled;
    }

    /**
     * Get User Account Delete Status
     * @return boolean
     */
    public boolean isDeleted() {
        return isDeleted;
    }

    /**
     * Get the time of creation of User in String
     * @return String
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * Get the time of update of User in String
     * @return String
     */
    public String getUpdatedAt() {
        return updatedAt;
    }
}
