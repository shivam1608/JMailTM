package me.shivzee.util;

import me.shivzee.exceptions.DateTimeParserException;

import java.time.ZonedDateTime;

import static me.shivzee.util.Utility.parseToDefaultTimeZone;

/**
 * The Account class represents a user account in the email system.
 * <p>
 * This class encapsulates user account information including email address, storage quota,
 * account status, and timestamps. It provides methods to access and manage account properties.
 * </p>
 */
public class Account {
    private String id;
    private String address;
    private String quota;
    private String used;
    private Boolean isDisabled;
    private Boolean isDeleted;
    private String createdAt;
    private String updatedAt;

    /**
     * Gets the user ID.
     *
     * @return the user ID of the account
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the email address.
     *
     * @return the email address of the account
     */
    public String getEmail() {
        return address;
    }

    /**
     * Gets the total storage quota.
     *
     * @return the total amount of allowed storage for mails and attachments
     */
    public String getQuota() {
        return quota;
    }

    /**
     * Gets the amount of storage used.
     *
     * @return the amount of storage used for mails and attachments
     */
    public String getUsed() {
        return used;
    }

    /**
     * Checks if the account is disabled or banned.
     *
     * @return {@code true} if the account is disabled; {@code false} otherwise
     */
    public boolean isDisabled() {
        return isDisabled;
    }

    /**
     * Checks if the account has been deleted.
     *
     * @return {@code true} if the account is deleted; {@code false} otherwise
     */
    public boolean isDeleted() {
        return isDeleted;
    }

    /**
     * Gets the account creation timestamp.
     *
     * @return the account creation date
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * Gets the account last update timestamp.
     * <p>
     * This timestamp is updated when the account is modified, such as when messages are received
     * or when the account is deleted.
     * </p>
     *
     * @return the account update date
     * @see me.shivzee.callbacks.EventListener
     */
    public String getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Get the account creation Date/Time in ZonedDateTime format
     * @return the date at which the account was created
     * @throws DateTimeParserException when fail to parse
     */
    public ZonedDateTime getCreatedDateTime() throws DateTimeParserException {
        return parseToDefaultTimeZone(createdAt, "yyyy-MM-dd'T'HH:mm:ss'+00:00'");
    }

    /**
     * Get the account update Date/Time in  ZonedDateTime format
     * @return the date on which the account was updated
     * @see me.shivzee.callbacks.EventListener
     */
    public ZonedDateTime getUpdatedDateTime() throws DateTimeParserException {
        return parseToDefaultTimeZone(updatedAt, "yyyy-MM-dd'T'HH:mm:ss'+00:00'");
    }
}
