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
 * The IO Class Written Specifically for JMailTM
 * version <= 0.2 -> Native HttpURLConnection
 * version >= 0.3 -> OkHttp
 * @author shizee
 */
public class IO {

    private static final OkHttpClient client = new OkHttpClient();
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static final MediaType PATCH = MediaType.get("application/merge-patch+json");



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
