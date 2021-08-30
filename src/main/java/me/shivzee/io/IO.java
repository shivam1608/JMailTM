package me.shivzee.io;

import me.shivzee.util.Response;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * The IO Class Written Specifically for JMailTM
 * @author shizee
 */
public class IO {

    public static Response requestPOST(String baseUrl, String auth, String contentJSON){
        try {

            URL url = new URL(baseUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type" , "application/json");
            connection.setRequestProperty("Authorization" , "Bearer "+auth);
            connection.setRequestProperty("accept" , "application/json");
            connection.setDoOutput(true);
            connection.setDoInput(true);

            OutputStream request = connection.getOutputStream();
            request.write(contentJSON.getBytes(StandardCharsets.UTF_8));

            int responseCode = connection.getResponseCode();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line).append("\n");
            }
            reader.close();
            connection.disconnect();
            return new Response(responseCode , response.toString());

        }catch (Exception e){
            return new Response(0 , "");
        }
    }

    public static Response requestPOST(String baseUrl , String contentJSON){
        try {

            URL url = new URL(baseUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type" , "application/json");
            connection.setRequestProperty("accept" , "application/json");
            connection.setDoOutput(true);
            connection.setDoInput(true);

            OutputStream request = connection.getOutputStream();
            request.write(contentJSON.getBytes(StandardCharsets.UTF_8));

            int responseCode = connection.getResponseCode();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line).append("\n");
            }
            reader.close();
            connection.disconnect();
            return new Response(responseCode , response.toString());

        }catch (Exception e){
            return new Response(0 , ""+e);
        }
    }

    public static Response requestGET(String baseUrl, String auth){

        try {

            URL url = new URL(baseUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type" , "application/json");
            connection.setRequestProperty("Authorization" , "Bearer "+auth);
            connection.setRequestProperty("accept" , "application/json");
            connection.setDoOutput(true);
            connection.setDoInput(true);

            int responseCode = connection.getResponseCode();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line).append("\n");
            }
            reader.close();
            connection.disconnect();
            return new Response(responseCode , response.toString());

        }catch (Exception e){
            return new Response(0 , "");
        }

    }

    public static Response requestGET(String baseUrl){

        try {

            URL url = new URL(baseUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type" , "application/json");
            connection.setRequestProperty("accept" , "application/json");
            connection.setDoOutput(true);
            connection.setDoInput(true);

            int responseCode = connection.getResponseCode();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line).append("\n");
            }
            reader.close();
            connection.disconnect();
            return new Response(responseCode , response.toString());

        }catch (Exception e){
            return new Response(0 , "");
        }
    }

    public static Response requestDELETE(String baseUrl , String auth){
        try {

            URL url = new URL(baseUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Content-Type" , "application/json");
            connection.setRequestProperty("Authorization" , "Bearer "+auth);
            connection.setRequestProperty("accept" , "application/json");
            connection.setDoOutput(true);
            connection.setDoInput(true);

            int responseCode = connection.getResponseCode();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line).append("\n");
            }
            reader.close();
            connection.disconnect();
            return new Response(responseCode , response.toString());

        }catch (Exception e){
            return new Response(0 , "");
        }
    }

    public static Response requestPATCH(String baseUrl , String auth){
        try {

            URL url = new URL(baseUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PATCH");
            connection.setRequestProperty("Content-Type" , "application/json");
            connection.setRequestProperty("Authorization" , "Bearer "+auth);
            connection.setRequestProperty("accept" , "application/json");
            connection.setDoOutput(true);
            connection.setDoInput(true);

            int responseCode = connection.getResponseCode();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line).append("\n");
            }
            reader.close();
            connection.disconnect();
            return new Response(responseCode , response.toString());

        }catch (Exception e){
            return new Response(0 , "");
        }
    }
}
