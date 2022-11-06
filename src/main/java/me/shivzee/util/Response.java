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
     * @return the HTTP response code
     */
    public int getResponseCode() {
        return responseCode;
    }

    /**
     * Get the Response
     * @return the response from server
     */
    public String getResponse() {
        return response;
    }
}
