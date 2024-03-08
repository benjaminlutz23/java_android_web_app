package edu.pdx.cs410J.benlutz;

/**
 * Class for formatting messages on the server side.  This is mainly to enable
 * test methods that validate that the server returned expected strings.
 */
public class Messages
{
    public static String missingRequiredParameter( String parameterName )
    {
        return String.format("The required parameter \"%s\" is missing", parameterName);
    }

    public static String createdAppointment(String owner, String description )
    {
        return String.format( "Created new Appointment in Book for %s: %s", owner, description );
    }

    public static String allAppointmentBooksDeleted() {
        return "All dictionary entries have been deleted";
    }

}
