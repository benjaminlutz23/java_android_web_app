CS 510J Project 5: A REST-ful Appointment Book Web Service

Developed by: Benjamin Lutz

Overview:
This application has been enhanced to include a RESTful web service, allowing users to manage their appointment books
through a server. The client-server architecture enables operations such as adding new appointments and searching for
appointments within specific time frames via HTTP requests.

Usage:
To get started, compile and build the project with the command: ./mvnw verify

To run the client application, execute:
java -jar target/apptbook-client.jar [options] <args>

Arguments:
Arguments must be provided in the following sequence:
- owner: Name of the appointment book owner.
- description: Description of the appointment.
- begin: Start date and time of the appointment in "MM/dd/yyyy hh:mm a VV" format.
- end: End date and time of the appointment in the same format as begin.

Options:
Options can be used in any order:
- -print: Prints details of the newly added appointment.
- -README: Displays README information and exits.
- -host hostname: Specifies the server's host name.
- -port port: Specifies the port on which the server is listening.
- -search: Searches for appointments within a given time range.

Examples of Use:
    Add an appointment:

        java -jar target/apptbook-client.jar -host localhost -port 8080 "Ben" "Teach Java Class" 10/19/2024 6:00 pm
            America/Los_Angeles 10/19/2024 9:30 pm America/Los_Angeles

    Search for appointments:

        java -jar target/apptbook-client.jar -host localhost -port 8080 -search "Ben" 11/01/2024 12:00 am America/Los_Angeles
            11/30/2024 11:59 pm America/Los_Angeles

    Pretty print all appointments:

        java -jar target/apptbook-client.jar -host localhost -port 8080 -search "Ben"

Error Handling:
The application provides user-friendly error messages for various conditions, such as incorrect command-line syntax, invalid date/time formats, failure to connect to the server, or improperly formatted data for the REST URL.
