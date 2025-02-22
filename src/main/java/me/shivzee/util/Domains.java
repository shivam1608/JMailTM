package me.shivzee.util;

import com.google.gson.*;
import me.shivzee.Config;
import me.shivzee.exceptions.DomainNotFoundException;
import me.shivzee.io.IO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * The Domains Class Static functions for Domain Operations
 * <br />
 * Check <a href="https://api.mail.tm">API Docs</a> for more info
 */
public class Domains {

    private static final String baseUrl = Config.BASEURL;
    private static final Gson gson = new Gson();
    private static List<Domain> domains = new ArrayList<>();

    private final static Logger LOG = LoggerFactory.getLogger(Domains.class);


    /**
     * Get The Domain List
     * @see me.shivzee.util.Domain
     * @return the list containing the domain objects
     */
    public static List<Domain> getDomainList() {
        return domains;
    }

    /**
     * Updates the Domain List
     * @return true if the domain list was refreshed from server
     */
    public static boolean updateDomains(){
        domains = new ArrayList<>();
        try{
            Response response = IO.requestGET(baseUrl+"/domains?page=1");
            if(response.getResponseCode() == 200){
                JsonArray json = JsonParser.parseString(response.getResponse()).getAsJsonArray();
                for(JsonElement domain : json){
                    domains.add(gson.fromJson(domain.getAsJsonObject() , Domain.class));
                }
            }
            return true;
        }catch (Exception e){
            return false;
        }
    }

    /**
     * Fetch and Return Domain List
     * @return list of domain objects
     * @see me.shivzee.util.Domain
     * @see me.shivzee.exceptions.DomainNotFoundException
     */
    public static List<Domain> fetchDomains(){
        domains = new ArrayList<>();

           try{
               Response response = IO.requestGET(baseUrl+"/domains?page=1");
               if(response.getResponseCode() == 200){
                   JsonArray json = JsonParser.parseString(response.getResponse()).getAsJsonArray();
                   for(JsonElement domain : json){
                       domains.add(gson.fromJson(domain.getAsJsonObject() , Domain.class));
                   }
               }
               return domains;
           }catch (Exception e){
               LOG.warn("Failed to fetch Domains "+e);
           }
           return domains;


    }

    /**
     * Fetches the Domain information by DomainID
     * @param id The domain ID to fetch
     * @return the single Domain object
     * @see me.shivzee.util.Domain
     * @see me.shivzee.exceptions.DomainNotFoundException
     * @throws DomainNotFoundException when domain was not found on server
     */
    public static Domain fetchDomainById(String id) throws DomainNotFoundException {
        try{

            Response response = IO.requestGET(baseUrl+"/domains/"+id);

            if(response.getResponseCode() == 200){
                JsonObject json = JsonParser.parseString(response.getResponse()).getAsJsonObject();
                return gson.fromJson(json , Domain.class);
            }else{
                throw new DomainNotFoundException("ID Specified can not be Found!");
            }


        }catch (Exception e){
            throw new DomainNotFoundException(e.toString());
        }
    }

    /**
     * Get a Random Domain From List
     * @return a single random Domain object from the list
     * @see me.shivzee.util.Domain
     */
    public static Domain getRandomDomain(){
        updateDomains();
        return domains.get(0);
    }

}
