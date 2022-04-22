package me.shivzee;

import org.json.simple.parser.JSONParser;

/**
 * The Config Class for Global Data
 */
public class Config {
    /**
     * BASEURL of the API
     */
    public static final String BASEURL = "https://api.mail.tm";
    /**
     * The instance of JSON Parser (JSONSIMPLE Used)
     */
    public static final JSONParser parser = new JSONParser();

    public static final String MERCURE_URL = "https://mercure.mail.tm/.well-known/mercure";
}
