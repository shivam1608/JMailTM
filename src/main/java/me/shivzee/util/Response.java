package me.shivzee.util;

/**
 * The Response class represents an HTTP response from the server.
 * <p>
 * This class encapsulates both the HTTP response code and the response body from server requests.
 * It provides methods to access the response code and content.
 * </p>
 * <p>
 * For more information about the API, see <a href="https://api.mail.tm">API Documentation</a>.
 * </p>
 */
public class Response {

    private int responseCode;
    private String response;

    /**
     * Constructs a new Response object with the specified response code and content.
     *
     * @param responseCode the HTTP response code
     * @param response the response body from the server
     */
    public Response(int responseCode, String response) {
        this.response = response;
        this.responseCode = responseCode;
    }

    /**
     * Gets the HTTP response code.
     *
     * @return the HTTP response code
     */
    public int getResponseCode() {
        return responseCode;
    }

    /**
     * Gets the response body from the server.
     *
     * @return the response content
     */
    public String getResponse() {
        return response;
    }
}
