package me.shivzee.exceptions;

/**
 * Created By vigneshwaranthangavelu  @ 13/11/22 - 8:34 pm
 */

public class DateTimeParserException extends Exception {
    private static final long serialVersionUID = -1589190382167071207L;

	public DateTimeParserException(String errorMessage){
        super(errorMessage);
    }
}
