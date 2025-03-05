package me.shivzee.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.shivzee.Config;
import me.shivzee.JMailTM;
import me.shivzee.io.IO;

import javax.security.auth.login.LoginException;

/**
 * The JMailBuilder class provides methods for account creation and authentication.
 * <p>
 * This class handles login, signup, and account management operations for the mail.tm API.
 * It provides both synchronous and asynchronous methods for these operations.
 * </p>
 * <p>
 * For more information about the API, see <a href="https://api.mail.tm">API Documentation</a>.
 * </p>
 */
public class JMailBuilder {

    private static final String baseUrl = Config.BASEURL;

    /**
     * Logs in to the API and returns a JMailTM instance.
     * <p>
     * This method synchronously authenticates the user with the provided email and password.
     * Upon successful authentication, it returns a new JMailTM instance that can be used
     * for further operations.
     * </p>
     *
     * @param email the email address to log in with
     * @param password the password for authentication
     * @return a new JMailTM instance for the authenticated user
     * @throws LoginException if authentication fails or network errors occur
     * @see me.shivzee.JMailTM
     */
    public static JMailTM login(String email , String password) throws LoginException{

        try{
            String jsonData = "{\"address\" : \""+email.trim()+"\",\"password\" : \""+password.trim()+"\"}";
            Response response = IO.requestPOST(baseUrl+"/token" , jsonData);
            if(response.getResponseCode() == 200){
                JsonObject json = JsonParser.parseString(response.getResponse()).getAsJsonObject();
                return new JMailTM(json.get("token").getAsString() , json.get("id").getAsString());
            }else {
                throw new LoginException(response.getResponse());
            }

        }catch (Exception e){
            throw new LoginException("Network error something went wrong " + e);
        }
    }

    /**
     * Creates a new account with the specified email and password.
     * <p>
     * This method synchronously creates a new account on the mail.tm service.
     * The email address is automatically converted to lowercase before creation.
     * </p>
     *
     * @param email the email address for the new account
     * @param password the password for the new account
     * @return {@code true} if the account was created successfully; {@code false} otherwise
     * @throws LoginException if the account already exists or invalid inputs are provided
     */
    public static boolean create(String email , String password) throws LoginException{

        try{

            String jsonData = "{\"address\" : \""+email.trim().toLowerCase()+"\",\"password\" : \""+password.trim().toLowerCase()+"\"}";
            Response response = IO.requestPOST(baseUrl+"/accounts" , jsonData);

            return response.getResponseCode() == 200 || response.getResponseCode() == 201;

        }catch (Exception e){
            return false;
        }

    }

    /**
     * Creates a new account and logs in to it.
     * <p>
     * This method combines account creation and login into a single operation.
     * The email address is automatically converted to lowercase before creation.
     * </p>
     *
     * @param email the email address for the new account
     * @param password the password for the new account
     * @return a new JMailTM instance for the created and authenticated user
     * @throws LoginException if account creation or login fails
     * @see me.shivzee.JMailTM
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
            throw new LoginException(e.toString());
        }

    }

    /**
     * Creates and logs in to a randomly generated account.
     * <p>
     * This method creates a new account with a randomly generated email address
     * and the specified password, then logs in to that account.
     * </p>
     *
     * @param password the password for the new account
     * @return a new JMailTM instance for the created and authenticated user
     * @throws LoginException if account creation or login fails
     * @see me.shivzee.JMailTM
     */
    public static JMailTM createDefault(String password) throws LoginException{
        try{
            String email = Utility.createRandomString(8)+"@"+Domains.getRandomDomain().getDomainName();
            return createAndLogin(email , password);

        }catch (Exception e){
            throw new LoginException(e.toString());
        }
    }


    /**
     * Login into an account with token
     * @param token the jwt token of the account
     * @return the JMailTM instance to a jwt specifed account
     * @throws LoginException when network error or token provided is invalid
     */
    public static JMailTM loginWithToken(String token) throws LoginException {
        try{
            Response response = IO.requestGET(baseUrl + "/me", token);
            if(response.getResponseCode() == 401){
                throw new LoginException("Invalid Token Provided");
            }
            if(response.getResponseCode() == 200){
                JsonObject json = JsonParser.parseString(response.getResponse()).getAsJsonObject();
                return new JMailTM(token , json.get("id").getAsString());
            }
            throw new LoginException("Invalid response received");
        }catch (Exception e){
            throw new LoginException(e.getMessage());
        }
    }



}
