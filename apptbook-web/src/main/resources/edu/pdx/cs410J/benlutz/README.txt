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
    begin: The date and time that the appointment begins in the format MM/dd/yyyy hh:mm a VV
        (e.g., 01/02/2023 9:16 PM America/Los_Angeles). The time must include AM/PM and a valid timezone ID.
    end: Same format as "begin," but for when the appointment ends.

Options: These may appear in any order:
    -print: Prints information about the appointment you just entered.
    -README: Prints a README for this project and exits. Note that even if other options/arguments were specified,
        nothing will happen besides displaying the README.
    -textFile file: Specifies a file path to read/write the appointment book information.
    -pretty file: Pretty prints the appointment book to a specified file, or standard out if the file argument is -.
        This includes the duration of each appointment in minutes.
    -xmlFile file: Specifies a file path to read/write the appointment book information as XML.


Here are some examples of how the program might be used:

    java -jar target/apptbook-1.0.0.jar -README

    java -jar target/apptbook-1.0.0.jar "Benjamin" "Meet with Jake" 01/30/2023 9:00 AM America/Los_Angeles 01/30/2023 10:00 AM America/Los_Angeles

    java -jar target/apptbook-1.0.0.jar -print "Benjamin" "Meet with Jake" 01/30/2023 9:00 AM America/Los_Angeles 01/30/2023 10:00 AM America/Los_Angeles

    java -jar target/apptbook-1.0.0.jar -pretty myFile "Benjamin Lutz" "Strategy Meeting" 02/01/2023 1:30 PM America/New_York 02/01/2023 2:30 PM America/New_York

    java -jar target/apptbook-1.0.0.jar -pretty "Benjamin Lutz" "Strategy Meeting" 02/01/2023 1:30 PM America/New_York 02/01/2023 2:30 PM America/New_York

    java -jar target/apptbook-1.0.0.jar -textFile myFile "Benjamin Lutz" "Strategy Meeting" 02/01/2023 1:30 PM America/New_York 02/01/2023 2:30 PM America/New_York

    java -jar target/apptbook-1.0.0.jar -xmlFile myFile.xml "Benjamin Lutz" "Strategy Meeting" 02/01/2023 1:30 PM America/New_York 02/01/2023 2:30 PM America/New_York


To convert from a text file to an xml file, the program needs to be run like this:

    java -cp target/apptbook-1.0.0.jar edu.pdx.cs410J.benlutz.Converter myTextFile.txt myXmlFile.xml

where myTextFile.txt is the text file you are converting and myXmlFile.xml is the xml file you are converting to.


Error Handling: This program handles user errors.  If there is an error caused by input from the command line,
an error message will be displayed, and you may have to try running the program again in order to continue.

