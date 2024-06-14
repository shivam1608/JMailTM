package me.shivzee.util;

import me.shivzee.exceptions.DateTimeParserException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.function.Supplier;

/**
 * The Utility Class for utility ig lmao
 */
public class Utility {

    private static final String regex = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static final Random random = new Random();

    /**
     * Generates a Random String
     *
     * @param length the String Length
     * @return a randomly generated string
     */
    public static String createRandomString(int length){
        String randomString = "";
        for(int i=0;i<length;i++){
            randomString += regex.charAt(random.nextInt(regex.length()));
        }
        return randomString;
    }

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

    public static <T> T safeEval(Supplier<T> supplier) {
        try {
            return supplier.get();
        } catch (NullPointerException ex) {
            return null;
        }
    }

}
