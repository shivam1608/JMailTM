package me.shivzee.util;

import me.shivzee.Config;
import me.shivzee.exceptions.DomainNotFoundException;
import me.shivzee.io.IO;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.List;

/**
 * The Domains Class Static functions for Domain Operations
 * Check https://api.mail.tm for more info
 */
public class Domains {

    private static final String baseUrl = Config.BASEURL;
    private static final JSONParser parser = Config.parser;
    private static List<Domain> domains = new ArrayList<>();

    private static Domain domainUtility(JSONObject json){
        String id = json.get("id").toString();
        String domainName = json.get("domain").toString();
        boolean isActive = (boolean) json.get("isActive");
        boolean isPrivate = (boolean) json.get("isPrivate");
        String createdAt = json.get("createdAt").toString();
        String updatedAt = json.get("updatedAt").toString();
        return new Domain(id , domainName , isActive , isPrivate , createdAt , updatedAt);
    }

    /**
     * Get The Domain List
     * @return List<Domains>
     */
    public static List<Domain> getDomainList() {
        return domains;
    }

    /**
     * Updates the Domain List
     * @return boolean
     */
    public static boolean updateDomains(){
        domains = new ArrayList<>();
        try{
            Response response = IO.requestGET(baseUrl+"/domains?page=1");
            if(response.getResponseCode() == 200){
                JSONArray json = (JSONArray) parser.parse(response.getResponse());
                Object [] domainArray = json.toArray();
                for(Object object : domainArray){
                    domains.add(domainUtility((JSONObject) object));
                }
            }
            return true;
        }catch (Exception e){
            return false;
        }
    }

    /**
     * Fetch and Return Domain List
     * @return List<Domain>
     * @see me.shivzee.util.Domain
     * @see me.shivzee.exceptions.DomainNotFoundException
     */
    public static List<Domain> fetchDomains(){
        domains = new ArrayList<>();

           try{
               Response response = IO.requestGET(baseUrl+"/domains?page=1");
               if(response.getResponseCode() == 200){
                   JSONArray json = (JSONArray) parser.parse(response.getResponse());
                   Object [] domainArray = json.toArray();
                   for(Object object : domainArray){
                       domains.add(domainUtility((JSONObject) object));
                   }
               }
               return domains;
           }catch (Exception e){
               System.out.println("|NO LOGGER| Something went wrong! Contact Developer "+e);
           }
           return domains;


    }

    /**
     * Fetches the Domain information by DomainID
     * @param id The domain ID to fetch
     * @return me.shivzee.util.Domain
     * @see me.shivzee.util.Domain
     * @see me.shivzee.exceptions.DomainNotFoundException
     * @throws DomainNotFoundException
     */
    public static Domain fetchDomainById(String id) throws DomainNotFoundException {
        try{

            Response response = IO.requestGET(baseUrl+"/domains/"+id);

            if(response.getResponseCode() == 200){
                JSONObject json = (JSONObject) parser.parse(response.getResponse());
                return domainUtility(json);
            }else{
                throw new DomainNotFoundException("ID Specified can not be Found!");
            }


        }catch (Exception e){
            throw new DomainNotFoundException("Some error occurred !" + e);
        }
    }

    /**
     * Get a Random Domain From List
     * @return me.shivzee.util.Domain
     * @see me.shivzee.util.Domain
     */
    public static Domain getRandomDomain(){
        updateDomains();
        return domains.get(0);
    }

}
