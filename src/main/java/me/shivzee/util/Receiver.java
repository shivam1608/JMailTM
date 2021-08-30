package me.shivzee.util;

/**
 * The Receiver Class to Wrap Multiple SendTo
 * Check https://api.mail.tm for more info
 */
public class Receiver {
    private String address;
    private String name;

    public Receiver(String address, String name) {
        this.address = address;
        this.name = name;
    }

    /**
     * Get Email
     * @return String
     */
    public String getAddress() {
        return address;
    }

    /**
     * Get Name
     * @return String
     */
    public String getName() {
        return name;
    }
}
