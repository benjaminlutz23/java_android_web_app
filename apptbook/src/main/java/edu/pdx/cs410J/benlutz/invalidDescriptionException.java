package edu.pdx.cs410J.benlutz;

/**
 * Exception thrown when an invalid appointment description is encountered
 * This exception is used to indicate that the description field of an appointment is invalid or empty
 */
public class invalidDescriptionException extends Throwable {
    /**
     * Constructs an instance of <code>invalidDescriptionException</code> with a detailed error message.
     */
    public invalidDescriptionException(String descriptionFieldCannotBeEmpty) {
    }
}
