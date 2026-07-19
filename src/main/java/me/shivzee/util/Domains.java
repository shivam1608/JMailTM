package me.shivzee.util;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import me.shivzee.Config;
import me.shivzee.exceptions.DomainNotFoundException;
import me.shivzee.io.IO;

/**
 * The Domains class provides functionality for managing email domains.
 * <p>
 * This class handles operations related to email domains, including fetching and updating
 * the list of available domains from the mail.tm API.
 * </p>
 * <p>
 * For more information about the API, see <a href="https://api.mail.tm">API Documentation</a>.
 * </p>
 */
public class Domains {

    private static final String baseUrl = Config.BASEURL;
    private static final Gson gson = new Gson();
    private static List<Domain> domains = new ArrayList<>();

    /**
     * Gets the list of available domains.
     *
     * @return the list of domain objects
     * @see me.shivzee.util.Domain
     */
    public static List<Domain> getDomainList() {
        return domains;
    }

    /**
     * Updates and returns the list of available domains.
     * <p>
     * This method updates the domain list from the server and returns the updated list.
     * On failure, it throws {@link DomainNotFoundException}
     * </p>
     *
     * @return the list of available domain objects
     * @see me.shivzee.util.Domain
     * @see me.shivzee.exceptions.DomainNotFoundException
     * @throws DomainNotFoundException if the domain list cannot be fetched or no domains are available
     */
    public static List<Domain> fetchDomains() throws DomainNotFoundException{
    	updateDomains();
        return getDomainList();
    }

    /**
     * Updates the list of available domains from the server.
     * <p>
     * This method fetches the latest list of domains from the mail.tm API and updates
     * the internal domain list.
     * </p>
     *
     * @return {@code true} if the domain list was successfully updated
     * @see me.shivzee.exceptions.DomainNotFoundException
     * @throws DomainNotFoundException if the domain list cannot be fetched or no domains are available
     */
    public static boolean updateDomains() throws DomainNotFoundException {
        domains = new ArrayList<>();
        try {
            int page = 1;
            while (true) {
                Response response = IO.requestGET(baseUrl + "/domains?page=" + page);
                if (response.getResponseCode() != 200)
                    throw new DomainNotFoundException(baseUrl + "/domains?page=" + page + " responded : " + response.getResponseCode());

                JsonArray array = JsonParser.parseString(response.getResponse()).getAsJsonArray();
                if (array.isEmpty()) break;

                for (JsonElement domain : array) {
                    domains.add(gson.fromJson(domain, Domain.class));
                }
                page++;
            }

            if (domains.isEmpty())
                throw new DomainNotFoundException("No available domains found!");

            return true;
        } catch (DomainNotFoundException e) {
            throw e;
        } catch (Exception other) {
            throw new DomainNotFoundException("Failed to parse domain list: " + other.getMessage(), other);
        }
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
     * @throws DomainNotFoundException if the domain list cannot be fetched or is empty
     * @see me.shivzee.util.Domain
     * @see me.shivzee.exceptions.DomainNotFoundException
     */
    public static Domain getRandomDomain() throws DomainNotFoundException {
        updateDomains();
        return domains.get(0);
    }

}
