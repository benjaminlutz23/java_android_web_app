package edu.pdx.cs410J.benlutz;

import edu.pdx.cs410J.ParserException;

import java.io.IOException;
import java.io.PrintStream;
import java.io.StringWriter;
import java.util.Map;

/**
 * The main class that parses the command line and communicates with the
 * Appointment Book server using REST.
 */
public class Project5 {

    public static void main(String[] args) {
        String host = null;
        String portString = null;
        boolean search = false;
        boolean print = false;
        boolean readme = false;
        String owner = null;
        String description = null;
        String begin = null;
        String end = null;

        // Parse command-line options
        int index = 0;
        while (index < args.length && args[index].startsWith("-")) {
            switch (args[index]) {
                case "-host":
                    host = args[++index];
                    break;
                case "-port":
                    portString = args[++index];
                    break;
                case "-search":
                    search = true;
                    break;
                case "-print":
                    print = true;
                    break;
                case "-README":
                    readme = true;
                    printReadme();
                    return;
                default:
                    printError("Unknown option: " + args[index]);
                    return;
            }
            index++;
        }

        // Validate host and port
        if ((host == null) != (portString == null)) {
            printError("Both host and port must be specified together");
            return;
        }

        // Additional arguments based on the operation mode
        if (!search) {
            // Add or print appointment
            if (index < args.length) owner = args[index++];
            if (index < args.length) description = args[index++];
            if (index < args.length) begin = args[index++];
            if (index < args.length) end = args[index++];
        } else {
            // Search mode
            if (index < args.length) owner = args[index++];
            if (index < args.length) begin = args[index++];
            if (index < args.length) end = args[index++];
        }

        // Error handling for required fields
        if (owner == null) {
            printError("Owner is required.");
            return;
        }

        // Perform action based on the mode
        if (search) {
            // Validate begin and end dates
            if (begin == null || end == null) {
                printError("Both begin and end times are required for search.");
                return;
            }
            searchAppointments(host, portString, owner, begin, end);
        } else {
            // Validate for adding a new appointment
            if (description == null || begin == null || end == null) {
                printError("Description, begin time, and end time are required for adding a new appointment.");
                return;
            }
            addAppointment(host, portString, owner, description, begin, end);
        }

        if (print) {
            // Printing logic here, if needed
        }
    }

    private static void printReadme() {
        // Print README information
    }

    private static void printError(String message) {
        System.err.println(message);
        // Optionally print usage information
    }

    private static void searchAppointments(String host, String port, String owner, String begin, String end) {
        // Logic to send a search request to the server
    }

    private static void addAppointment(String host, String port, String owner, String description, String begin, String end) {
        // Logic to send a request to add a new appointment to the server
    }
}
