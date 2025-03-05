package me.shivzee.util;

import me.shivzee.exceptions.DateTimeParserException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.function.Supplier;

/**
 * The Utility class provides various helper methods for common operations.
 * <p>
 * This class includes methods for generating random strings, parsing dates, and safely
 * evaluating expressions that might throw NullPointerException.
 * </p>
 */
public class Utility {

    private static final String regex = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static final Random random = new Random();

    /**
     * Generates a random string of specified length using alphanumeric characters.
     *
     * @param length the desired length of the random string
     * @return a randomly generated string containing only lowercase letters and numbers
     */
    public static String createRandomString(int length){
        String randomString = "";
        for(int i=0;i<length;i++){
            randomString += regex.charAt(random.nextInt(regex.length()));
        }
        return randomString;
    }

    /**
     * Parses a date string to a ZonedDateTime object using the specified pattern.
     * <p>
     * The resulting ZonedDateTime will be in the system's default time zone.
     * </p>
     *
     * @param dateTime the date string to parse
     * @param pattern the pattern to use for parsing the date
     * @return a ZonedDateTime object representing the parsed date
     * @throws DateTimeParserException if the date string cannot be parsed using the given pattern
     */
    public static ZonedDateTime parseToDefaultTimeZone(String dateTime, String pattern) throws DateTimeParserException {
        ZonedDateTime time = null;
        try {
            time = LocalDateTime.parse(dateTime, DateTimeFormatter
                            .ofPattern(pattern))
                    .atZone(ZoneId.systemDefault());
        }catch(Exception ex) {
           throw new DateTimeParserException("Unable to parse Date for :" + dateTime + " With Pattern " + pattern);
        }
        return time;
    }

    /**
     * Safely evaluates a supplier that might throw a NullPointerException.
     * <p>
     * If the supplier throws a NullPointerException, this method returns null instead
     * of propagating the exception.
     * </p>
     *
     * @param <T> the type of the value returned by the supplier
     * @param supplier the supplier to evaluate
     * @return the result of the supplier, or null if a NullPointerException occurs
     */
    public static <T> T safeEval(Supplier<T> supplier) {
        try {
            return supplier.get();
        } catch (NullPointerException ex) {
            return null;
        }
    }

}
