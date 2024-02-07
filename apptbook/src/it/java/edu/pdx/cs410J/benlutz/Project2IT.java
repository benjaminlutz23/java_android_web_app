package edu.pdx.cs410J.benlutz;

import edu.pdx.cs410J.InvokeMainTestCase;
import edu.pdx.cs410J.ParserException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for the {@link Project2} main class.
 */
class Project2IT extends InvokeMainTestCase {

  @Test
  public void textFileOptionWithoutFileNameShowsError() throws invalidDescriptionException, invalidOwnerException {
    ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    System.setErr(new PrintStream(errContent));

    String[] args = {"-textFile"};
    Project2.main(args);

    String expectedError = "Error: -textFile option requires a file name";
    assertTrue(errContent.toString().contains(expectedError));

    System.setErr(System.err); // Reset System.err to its original stream
  }

  @Test
  public void invalidFilePathFormatShowsError() {
    ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    System.setErr(new PrintStream(errContent));

    String[] args = {"-textFile", "invalid/?path.txt", "owner", "description", "01/01/2020", "12:00", "01/01/2020", "13:00"};
    try {
      Project2.main(args);
    } catch (Exception | invalidDescriptionException | invalidOwnerException e) {
      // Handle or log exceptions if necessary
    }

    String expectedError = "Error while creating new file"; // or any other specific error message you log for invalid paths
    assertTrue(errContent.toString().contains(expectedError));

    System.setErr(System.err); // Reset System.err to its original stream
  }

  @Test
  public void absoluteFilePathWorksCorrectly() throws invalidDescriptionException, invalidOwnerException {
    String fileName = Paths.get(System.getProperty("java.io.tmpdir"), "tempAppointmentBookAbsolute.txt").toString();
    File file = new File(fileName);
    file.delete(); // Ensure the file does not exist before the test

    String[] args = {"-textFile", fileName, "owner", "description", "01/01/2020", "12:00", "01/01/2020", "13:00"};
    Project2.main(args);

    assertTrue(file.exists());

    file.delete(); // Cleanup after test
  }

  @Test
  public void relativeFilePathWorksCorrectly() throws invalidDescriptionException, invalidOwnerException {
    String fileName = "tempAppointmentBookRelative.txt";
    File file = new File(fileName);
    file.delete(); // Ensure the file does not exist before the test

    String[] args = {"-textFile", fileName, "owner", "description", "01/01/2020", "12:00", "01/01/2020", "13:00"};
    Project2.main(args);

    assertTrue(file.exists());

    file.delete(); // Cleanup after test
  }


  @Test
  public void nonExistentTextFileCreatesNewFile() {
    String fileName = "tempAppointmentBook.txt";
    File file = new File(fileName);
    file.delete(); // Ensure the file does not exist before the test

    String[] args = {"-textFile", fileName, "owner", "description", "01/01/2020", "12:00", "01/01/2020", "13:00"};
    try {
      Project2.main(args);
    } catch (invalidDescriptionException | invalidOwnerException e) {
      fail("Unexpected exception thrown: " + e.getMessage());
    }

    assertTrue(file.exists());

    file.delete(); // Cleanup after test
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
    String content = "Test Owner\nTest Appointment, 01/01/2020 12:00, 01/01/2020 13:00";
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
  public void mismatchedOwnerNamePrintsErrorMessage() throws invalidDescriptionException, invalidOwnerException {
    ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    System.setErr(new PrintStream(errContent));

    Project2.main(new String[]{"-textFile", "src/test/resources/edu/pdx/cs410J/benlutz/valid-apptbook.txt", "MismatchedOwner", "Meeting", "01/01/2023", "09:00", "01/01/2023", "10:00"});

    String expectedError = "The owner name you provided does not match the owner name in the text file.";
    assertTrue(errContent.toString().contains(expectedError));

    System.setErr(System.err); // Reset System.err to its original stream
  }

  @Test
  void missingEndTimePrintsErrorToStandardError() {
    String[] args = {"Owner", "Description", "12/01/2020", "12:00", "12/01/2020"};
    MainMethodResult result = invokeMain(Project2.class, args);
    assertThat(result.getTextWrittenToStandardError(), containsString("Error: Missing end time"));
  }

  @Test
  void missingEndDateAndTimePrintsErrorToStandardError() {
    String[] args = {"Owner", "Description", "12/01/2020", "12:00"};
    MainMethodResult result = invokeMain(Project2.class, args);
    assertThat(result.getTextWrittenToStandardError(), containsString("Error: Missing end time"));
  }

  @Test
  void invalidBeginDateFormatPrintsErrorToStandardError() {
    String[] args = {"Owner", "Description", "12-01-2020", "12:00", "12/01/2020", "13:00"};
    MainMethodResult result = invokeMain(Project2.class, args);
    assertThat(result.getTextWrittenToStandardError(), containsString("Invalid begin date/time format:"));
  }

  @Test
  void invalidBeginTimeFormatPrintsErrorToStandardError() {
    String[] args = {"Owner", "Description", "12/01/2020", "12:XX", "12/01/2020", "13:00"};
    MainMethodResult result = invokeMain(Project2.class, args);
    assertThat(result.getTextWrittenToStandardError(), containsString("Invalid begin date/time format:"));
  }

  @Test
  void invalidEndDateTimeFormatPrintsErrorToStandardError() {
    String[] args = {"Owner", "Description", "12/01/2020", "12:00", "12-01-2020", "13:00"};
    MainMethodResult result = invokeMain(Project2.class, args);
    assertThat(result.getTextWrittenToStandardError(), containsString("Invalid end date/time format:"));
  }

  @Test
  void invokingMainWithNoArgumentsPrintsMissingArgumentsToStandardError() {
    InvokeMainTestCase.MainMethodResult result = invokeMain(Project2.class);
    assertThat(result.getTextWrittenToStandardError(), containsString("Error: No command line arguments"));
  }

  //When there are less than 6 arguments not including options
  @Test
  void unknownCommandLineOptionPrintsErrorToStandardError() {
    InvokeMainTestCase.MainMethodResult result = invokeMain(Project2.class, "-print", "-unknownOption", "owner", "description",
            "begin date", "begin time", "end date", "end time");
    assertThat(result.getTextWrittenToStandardError(), containsString("Error: Invalid command line option"));
  }

  @Test
  void tooManyCommandLineArgumentsPrintsErrorToStandardError() {
    InvokeMainTestCase.MainMethodResult result = invokeMain(Project2.class, "Arg1", "Arg2", "Arg3", "Arg4", "Arg5",
            "Arg6", "Arg7", "Arg8", "Arg9", "Arg10", "Arg11");
    assertThat(result.getTextWrittenToStandardError(), containsString("Too many command line arguments"));
  }

}