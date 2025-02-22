package me.shivzee.util;

/**
 * The Receiver Class to Wrap Multiple SendTo
 * Check https://api.mail.tm for more info
 */
public class Receiver {
    private String address;
    private String name;

    /**
     * Get Email
     * @return the email address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Get Name
     * @return the name
     */
    public String getName() {
        return name;
    }
}
