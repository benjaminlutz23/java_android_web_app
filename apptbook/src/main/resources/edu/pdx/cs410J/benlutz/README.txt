CS 510J Project 1: Appointment Book Application

Developed by: Benjamin Lutz

Overview: This is an application that creates a book of appointments for the user.  The program is currently run from
the command line, and the user must enter their name, a description of their appointment, and the start and end times.
The appointment will then be added to an appointment book under their name.

Usage: Please build the project using the command: ./mvnw verify

When you're ready to run the program, type the follow in this order:

    java -jar target/apptbook-1.0.0.jar [options] <args>

Arguments: All arguments are required, and must be entered in the following order:
    owner: The name of the person who owns the appointment book. May be first and last name, or just a single name.
    description: A brief description of the appointment.
    begin: The date and time that the appointment begins in the format mm/dd/yyyy hh:mm. Note that the time must be
        in 24hr clock notation as "am/pm" will not be interpreted by the program.
    end: Same as "begin," but for the end time of the appointment.

Options: These may appear in any order:
    -print: Prints information about the appointment you just entered.
    -README: Prints a README for this project and exits. Note that even if other options/arguments were specified,
        nothing will happen besides displaying the README.

Here are some examples of how the program might be used:

    java -jar target/apptbook-1.0.0.jar -README

    java -jar target/apptbook-1.0.0.jar "Benjamin" "Meet with Jake" 11/30/2022 11:00 11/30/2022 14:00

    java -jar target/apptbook-1.0.0.jar -print "Benjamin" "Meet with Jake" 11/30/2022 11:00 11/30/2022 14:00

    java -jar target/apptbook-1.0.0.jar -print "Benjamin Lutz" "Meet with Jake" 11/30/2022 11:00 11/30/2022 14:00

Error Handling: This program handles user errors.  If there is an error caused by input from the command line,
an error message will be displayed, and you may have to try running the program again in order to continue.

