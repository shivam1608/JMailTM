package me.shivzee.util;

/**
 * The Domain Class to Wrap Domains
 * Check https://api.mail.tm for more info
 */
public class Domain {
    private String id;
    private String domainName;
    private boolean isActive;
    private boolean isPrivate;
    private String createdAt;
    private String updatedAt;

    public Domain(String id, String domainName, boolean isActive, boolean isPrivate, String createdAt, String updatedAt) {
        this.id = id;
        this.domainName = domainName;
        this.isActive = isActive;
        this.isPrivate = isPrivate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * Get Domain ID
     * @return String
     */
    public String getId() {
        return id;
    }

    /**
     * Get DomainName (eg. tempexample.xyz)
     * @return String
     */
    public String getDomainName() {
        return domainName;
    }

    /**
     * Get Domain Active Status
     * @return boolean
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Get the Private Status of Domain
     * @return boolean
     */
    public boolean isPrivate() {
        return isPrivate;
    }

    /**
     * Get the domain creation time in String
     * @return String
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * Get the domain update time in String
     * @return String
     */
    public String getUpdatedAt() {
        return updatedAt;
    }
}
