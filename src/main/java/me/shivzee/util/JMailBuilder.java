package me.shivzee.util;

import me.shivzee.Config;
import me.shivzee.JMailTM;
import me.shivzee.io.IO;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.security.auth.login.LoginException;

/**
 * The JMailBuilder Class for Login/Signup Operations
 * Check https://api.mail.tm for more info
 */
public class JMailBuilder {

    private static final String baseUrl = Config.BASEURL;
    private static final JSONParser parser = Config.parser;

    /**
     * (Synchronous) Login into the API and Returns a JMailTM
     *
     * @see me.shivzee.JMailTM
     * @param email the email to login
     * @param password the password
     * @return the JMailTM instance
     * @throws LoginException when fails to login user
     */
    public static JMailTM login(String email , String password) throws LoginException{

        try{
            String jsonData = "{\"address\" : \""+email.trim()+"\",\"password\" : \""+password.trim()+"\"}";
            Response response = IO.requestPOST(baseUrl+"/token" , jsonData);
            if(response.getResponseCode() == 200){
                JSONObject json = (JSONObject) parser.parse(response.getResponse());
                return new JMailTM(json.get("token").toString() , json.get("id").toString());
            }else {
                throw new LoginException(response.getResponse());
            }

        }catch (Exception e){
            throw new LoginException("Network error something went wrong " + e);
        }
    }

    /**
     * (Synchronous) Creates a new Account (First Fetch the domain)
     *
     * @see me.shivzee.util.JMailBuilder
     * @param email The email to create
     * @param password The password to set
     * @return true if account was created
     * @throws LoginException when account already exists, invalid inputs or network error
     */
    public static boolean create(String email , String password) throws LoginException{

        try{

            String jsonData = "{\"address\" : \""+email.trim().toLowerCase()+"\",\"password\" : \""+password.trim().toLowerCase()+"\"}";
            Response response = IO.requestPOST(baseUrl+"/accounts" , jsonData);

            return response.getResponseCode() == 200;

        }catch (Exception e){
            return false;
        }

    }

    /**
     * (Synchronous) Creates and Login into an Account
     * @param email the email to create
     * @param password the password to set
     * @return the JMailTM instance of the created user
     * @see me.shivzee.JMailTM
     * @throws LoginException when account already exists, invalid inputs or network error
     */
    public static JMailTM createAndLogin(String email , String password) throws LoginException{

        try{

            String jsonData = "{\"address\" : \""+email.trim().toLowerCase()+"\",\"password\" : \""+password.trim()+"\"}";
            Response response = IO.requestPOST(baseUrl+"/accounts" , jsonData);

            if(response.getResponseCode() == 201){
                return login(email.trim().toLowerCase() , password.trim());

            }else if(response.getResponseCode() == 422){
                throw new LoginException("Account Already Exists! Error 422");
            }else if (response.getResponseCode() == 429){
                throw new LoginException("Too many requests! Error 429 Rate limited");
            }else{
                throw new LoginException("Something went wrong while creating account! Try Again");
            }

        }catch (Exception e){
            throw new LoginException(""+e);
        }

    }

    /**
     * (Synchronous) Creates and Login into a Random Account
     * @param password the password to set
     * @return the JMailTM instance to a randomly generated account
     * @see me.shivzee.JMailTM
     * @throws LoginException when network or api error
     */
    public static JMailTM createDefault(String password) throws LoginException{
        try{
            String email = Utility.createRandomString(8)+"@"+Domains.getRandomDomain().getDomainName();
            return createAndLogin(email , password);

        }catch (Exception e){
            throw new LoginException(""+e);
        }
    }



}
