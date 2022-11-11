package me.shivzee.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

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

    public static ZonedDateTime parseDateTimeToDefaultTimeZone(String dateTime, String pattern) {
        try {
            dateTime = dateTime.replace("am", "AM");
            dateTime = dateTime.replace("pm", "PM");
            LocalDateTime objLocalDateTime = LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern(pattern));
            return objLocalDateTime.atZone(ZoneId.systemDefault());
        }catch(Exception ex) {
            try {
                dateTime = dateTime.replace("AM", "am");
                dateTime = dateTime.replace("PM", "pm");
                LocalDateTime objLocalDateTime = LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern(pattern));
                return objLocalDateTime.atZone(ZoneId.systemDefault());
            }catch(Throwable t) {
            }
        }
        return null;
    }

}
