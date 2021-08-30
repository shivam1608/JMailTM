package me.shivzee.util;

/**
 * The Response Class to Handle Response and Errors
 * Check https://api.mail.tm for more info
 */
public class Response {

    private int responseCode;
    private String response;

    public Response(int responseCode , String response){
        this.response = response;
        this.responseCode = responseCode;
    }

    /**
     * Get the Response Code
     * @return int
     */
    public int getResponseCode() {
        return responseCode;
    }

    /**
     * Get the Response
     * @return String
     */
    public String getResponse() {
        return response;
    }
}
