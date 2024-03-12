package edu.pdx.cs410J.benlutz;

import edu.pdx.cs410J.InvokeMainTestCase;
import edu.pdx.cs410J.ParserException;
import edu.pdx.cs410J.UncaughtExceptionInMain;
import edu.pdx.cs410J.family.XmlDumper;
import edu.pdx.cs410J.web.HttpRequestHelper.RestException;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.*;
import java.net.HttpURLConnection;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

/**
 * An integration test for {@link Project5} that invokes its main method with
 * various arguments
 */
@TestMethodOrder(MethodName.class)
class Project5IT extends InvokeMainTestCase {
    private static final String HOSTNAME = "localhost";
    private static final String PORT = System.getProperty("http.port", "8080");

      @Test
    public void beginTimeAfterEndTimeShowsError() throws invalidDescriptionException, invalidOwnerException {
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errContent));

        // Providing command line arguments that simulate begin time after end time
        String[] args = {
                "-host", "localhost", "-port", "8080",
                "owner", "description",
                "01/01/2023", "10:00", "AM", "America/New_York",
                "01/01/2023", "9:00", "AM", "America/New_York"
        };
        Project5.main(args);

        // The expected error message when the begin time is after the end time
        String expectedError = "Error: The begin time must be before the end time.";
        assertTrue(errContent.toString().contains(expectedError), "The expected error message was not found.");

        System.setErr(System.err); // Reset System.err to its original stream
    }

    @Test
    void dumpingAppointmentBookWritesCorrectContent() throws invalidOwnerException, invalidDescriptionException {
        String owner = "Test Owner";
        ZonedDateTime beginTime = ZonedDateTime.now().withZoneSameInstant(ZoneId.systemDefault());
        ZonedDateTime endTime = beginTime.plusHours(1);
        String description = "Test Appointment";

        AppointmentBook book = new AppointmentBook(owner);
        Appointment appointment = new Appointment(description, beginTime, endTime);
        book.addAppointment(appointment);

        StringWriter sw = new StringWriter();
        TextDumper dumper = new TextDumper(sw);
        dumper.dump(book);

        String dumpedContent = sw.toString();
        assertTrue(dumpedContent.contains(owner));
        assertTrue(dumpedContent.contains(description));
    }

    @Test
    void testParsingAppointmentBookReadsCorrectContent() throws IOException, ParserException {
        String content = "Test Owner\nTest Appointment, 01/01/2020 12:00 PM America/Los_Angeles, 01/01/2020 1:00 PM America/Los_Angeles";
        File tempFile = File.createTempFile("testAppointmentBook", ".txt");
        try (Writer writer = new FileWriter(tempFile)) {
            writer.write(content);
        }

        AppointmentBook parsedBook;
        try (Reader reader = new FileReader(tempFile)) {
            TextParser parser = new TextParser(reader);
            parsedBook = parser.parse();
        }
        assertEquals("Test Owner", parsedBook.getOwnerName());
        assertFalse(parsedBook.getAppointments().isEmpty());

        // Cleanup
        tempFile.delete();
    }


    @Test
    void missingEndTimePrintsErrorToStandardError() {
        String[] args = {
                "-host", "localhost", "-port", "8080",
                "owner", "description",
                "01/01/2023",
                "01/01/2023", "10:00", "AM", "America/New_York"
        };
        MainMethodResult result = invokeMain(Project5.class, args);
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("Error: Missing end time"));
    }

    @Test
    void missingEndDateAndTimePrintsErrorToStandardError() {
        String[] args = {
                "-host", "localhost", "-port", "8080",
                "owner", "description",
                "01/01/2023", "10:00", "AM", "America/New_York",
        };
        MainMethodResult result = invokeMain(Project5.class, args);
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("Error: Missing end time"));
    }

    @Test
    void invalidBeginDateFormatPrintsErrorToStandardError() {
        String[] args = {"-host", "localhost", "-port", "8080", "Owner", "Description", "12-01-2020", "12:00", "12/01/2020", "13:00"};
        MainMethodResult result = invokeMain(Project5.class, args);
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("Invalid begin date/time format:"));
    }

    @Test
    void invalidBeginTimeFormatPrintsErrorToStandardError() {
        String[] args = {"-host", "localhost", "-port", "8080", "Owner", "Description", "12/01/2020", "12:XX", "12/01/2020", "13:00"};
        MainMethodResult result = invokeMain(Project5.class, args);
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("Invalid begin date/time format:"));
    }

    @Test
    void invalidEndDateTimeFormatPrintsErrorToStandardError() {
        String[] args = {"-host", "localhost", "-port", "8080", "Owner", "Description", "01/01/2020", "12:00", "PM", "America/Los_Angeles"};
        MainMethodResult result = invokeMain(Project5.class, args);
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("Invalid end date/time format:"));
    }

    @Test
    void invokingMainWithNoArgumentsPrintsMissingArgumentsToStandardError() {
        InvokeMainTestCase.MainMethodResult result = invokeMain(Project5.class);
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("Error: No command line arguments"));
    }

    //When there are less than 6 arguments not including options
    @Test
    void unknownCommandLineOptionPrintsErrorToStandardError() {
        InvokeMainTestCase.MainMethodResult result = invokeMain(Project5.class, "-print", "-unknownOption", "owner", "description",
                "begin date", "begin time", "end date", "end time");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("Error: Invalid command line option"));
    }

    @Test
    void tooManyCommandLineArgumentsPrintsErrorToStandardError() {
        InvokeMainTestCase.MainMethodResult result = invokeMain(Project5.class, "Arg1", "Arg2", "Arg3", "Arg4", "Arg5",
                "Arg6", "Arg7", "Arg8", "Arg9", "Arg10", "Arg11", "Arg12", "Arg13", "Arg14", "Arg15", "Arg16", "Arg17");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("Too many command line arguments"));
    }


    @Test
    void test1NoCommandLineArguments() {
        MainMethodResult result = invokeMain( Project5.class );
        assertThat(result.getTextWrittenToStandardError(), containsString(Project5.MISSING_ARGS));
    }

    @Test
    @Disabled
    void test3NoAppointmentThrowsAppointmentBookRestException() {
        String owner = "OWNER";
        try {
            invokeMain(Project5.class, HOSTNAME, PORT, owner);
            fail("Expected a RestException to be thrown");

        } catch (UncaughtExceptionInMain ex) {
            RestException cause = (RestException) ex.getCause();
            assertThat(cause.getHttpStatusCode(), equalTo(HttpURLConnection.HTTP_NOT_FOUND));
        }
    }

    @Test
    @Disabled
    void test4AddAppointment() {
        String owner = "OWNER";
        String description = "DESCRIPTION";

        MainMethodResult result = invokeMain( Project5.class, HOSTNAME, PORT, owner, description );

        assertThat(result.getTextWrittenToStandardError(), equalTo("** Incomplete date/time for the beginning of the appointment\n"));

        String out = result.getTextWrittenToStandardOut();
        //assertThat(out, out, containsString(Messages.definedWordAs(owner, description)));

        result = invokeMain( Project5.class, HOSTNAME, PORT, owner );

        //assertThat(result.getTextWrittenToStandardError(), equalTo(""));

        out = result.getTextWrittenToStandardOut();
        assertThat(out, out, containsString(PrettyPrinter.formatAppointmentDescription(owner, description)));
    }
}