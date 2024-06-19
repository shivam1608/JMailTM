package me.shivzee.util;

/**
 * The Domain Class to Wrap Domains
 * Check https://api.mail.tm for more info
 */
public class Domain {
    private String id;
    private String domain;
    private boolean isActive;
    private boolean isPrivate;
    private String createdAt;
    private String updatedAt;


    /**
     * Get Domain ID
     * @return the id of the domain
     */
    public String getId() {
        return id;
    }

    /**
     * Get DomainName (eg. example.com)
     * @return the domain name
     */
    public String getDomainName() {
        return domain;
    }

    /**
     * Get Domain Active Status
     * @return true if the domain is active
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Get the Private Status of Domain
     * @return true if domain is marked as private
     */
    public boolean isPrivate() {
        return isPrivate;
    }

    /**
     * Get the domain creation time in String
     * @return the date when the domain was added to the server
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * Get the domain update time in String
     * @return the date when the domain was last updated on server
     */
    public String getUpdatedAt() {
        return updatedAt;
    }
}
