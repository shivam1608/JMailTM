package me.shivzee.io;

import com.google.gson.JsonParser;
import me.shivzee.util.Response;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.net.URL;
import java.util.Objects;

/**
 * The IO class handles HTTP communication for the JMailTM library.
 * <p>
 * This class provides methods for making HTTP requests to the mail.tm API. It uses OkHttp
 * for HTTP communication (version {@literal >=} 0.3) and previously used HttpURLConnection (version {@literal <=} 0.2).
 * </p>
 *
 * @author shivzee
 */

public class IO {

    private static final OkHttpClient client = new OkHttpClient();
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static final MediaType PATCH = MediaType.get("application/merge-patch+json");

    /**
     * Makes a POST request to the specified URL with authentication and JSON content.
     *
     * @param baseUrl the URL to send the request to
     * @param auth the authentication token (can be null)
     * @param contentJSON the JSON content to send in the request body
     * @return a Response object containing the server's response
     */
    public static Response requestPOST(String baseUrl, String auth, String contentJSON){
        try {
            URL url = new URL(baseUrl);
            Request.Builder request = new Request.Builder()
                    .url(url)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("accept", "application/json")
                    .post(RequestBody.create(contentJSON, JSON));
            if(auth!=null){
                request.addHeader("Authorization", "Bearer " + auth);
            }
            okhttp3.Response response = client.newCall(request.build()).execute();
            return new Response(response.code() , Objects.requireNonNull(response.body()).string());

        }catch (Exception e){
            return new Response(0 , "");
        }
    }

    /**
     * Makes a POST request to the specified URL with JSON content.
     *
     * @param baseUrl the URL to send the request to
     * @param contentJSON the JSON content to send in the request body
     * @return a Response object containing the server's response
     */
    public static Response requestPOST(String baseUrl , String contentJSON){
        try {
            return requestPOST(baseUrl , null , contentJSON);
        }catch (Exception e){
            return new Response(0 , ""+e);
        }
    }

    public static Response requestGET(String baseUrl, String auth){

        try {
            URL url = new URL(baseUrl);
            Request.Builder request = new Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("accept", "application/json");
            if(auth!=null){
                request.addHeader("Authorization", "Bearer " + auth);
            }
            okhttp3.Response response = client.newCall(request.build()).execute();
            return new Response(response.code() , Objects.requireNonNull(response.body()).string());

        }catch (Exception e){
            return new Response(0 , "");
        }

    }

    public static Response requestGET(String baseUrl){

        try {
            return requestGET(baseUrl , null);
        }catch (Exception e){
            return new Response(0 , "");
        }
    }

    public static Response requestDELETE(String baseUrl , String auth){
        try {

            URL url = new URL(baseUrl);
            Request.Builder request = new Request.Builder()
                    .url(url)
                    .delete()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("accept", "application/json");

            if(auth!=null){
                request.addHeader("Authorization", "Bearer " + auth);
            }
            okhttp3.Response response = client.newCall(request.build()).execute();
            return new Response(response.code() , Objects.requireNonNull(response.body()).string());

        }catch (Exception e){
            return new Response(0 , "");
        }
    }

    public static Response requestPATCH(String baseUrl , String auth , String data){
        try {

            URL url = new URL(baseUrl);
            Request.Builder request = new Request.Builder()
                    .url(url)
                    .patch(RequestBody.create(data , PATCH))
                    .addHeader("accept", "application/json");

            if(auth!=null){
                request.addHeader("Authorization", "Bearer " + auth);
            }
            okhttp3.Response response = client.newCall(request.build()).execute();
            return new Response(response.code() , Objects.requireNonNull(response.body()).string());

        }catch (Exception e){
            return new Response(0 , "");
        }
    }

    public static Response requestPATCH(String baseUrl , String auth){
        return requestPATCH(baseUrl , auth , "{\"seen\" : true}");
    }
}
