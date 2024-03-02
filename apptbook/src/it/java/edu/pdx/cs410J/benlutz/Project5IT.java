package edu.pdx.cs410J.benlutz;

// A change

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
 * Integration tests for the {@link Project5} main class.
 */
class Project5IT extends InvokeMainTestCase {
  @Test
  public void xmlFileOptionWithoutFileNameShowsError() throws invalidDescriptionException, invalidOwnerException {
    ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    System.setErr(new PrintStream(errContent));

    String[] args = {"-xmlFile"};
    Project5.main(args);

    String expectedError = "Error: -xmlFile option requires a file name";
    assertTrue(errContent.toString().contains(expectedError));

    System.setErr(System.err); // Reset System.err to its original stream
  }
  @Test
  public void invalidXmlFilePathFormatShowsError() {
    ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    System.setErr(new PrintStream(errContent));

    String[] args = {"-xmlFile", "invalid/?path.txt", "owner", "description", "01/01/2020", "12:00", "PM", "America/Los_Angeles", "01/01/2020", "1:00", "PM", "America/Los_Angeles"};
    try {
      Project5.main(args);
    } catch (Exception | invalidDescriptionException | invalidOwnerException e) {
      // Handle or log exceptions if necessary
    }

    String expectedError = "Error while creating new file"; // or any other specific error message you log for invalid paths
    assertTrue(errContent.toString().contains(expectedError));

    System.setErr(System.err); // Reset System.err to its original stream
  }

  @Test
  public void absoluteXmlFilePathWorksCorrectly() throws invalidDescriptionException, invalidOwnerException {
    String fileName = Paths.get(System.getProperty("java.io.tmpdir"), "tempAppointmentBookAbsolute.xml").toString();
    File file = new File(fileName);
    file.delete(); // Ensure the file does not exist before the test

    String[] args = {"-xmlFile", fileName, "owner", "description",  "01/01/2020", "12:00", "PM", "America/Los_Angeles", "01/01/2020", "1:00", "PM", "America/Los_Angeles"};
    Project5.main(args);

    assertTrue(file.exists());

    file.delete(); // Cleanup after test
  }

  @Test
  public void relativeXmlFilePathWorksCorrectly() throws invalidDescriptionException, invalidOwnerException {
    String fileName = "tempAppointmentBookRelative.xml";
    File file = new File(fileName);
    file.delete(); // Ensure the file does not exist before the test

    String[] args = {"-xmlFile", fileName, "owner", "description", "01/01/2020", "12:00", "PM", "America/Los_Angeles", "01/01/2020", "1:00", "PM", "America/Los_Angeles"};
    Project5.main(args);

    assertTrue(file.exists());

    file.delete(); // Cleanup after test
  }


  @Test
  public void nonExistentXmlFileCreatesNewFile() {
    String fileName = "tempAppointmentBook.xml";
    File file = new File(fileName);
    file.delete(); // Ensure the file does not exist before the test

    String[] args = {"-xmlFile", fileName, "owner", "description",  "01/01/2020", "12:00", "PM", "America/Los_Angeles", "01/01/2020", "1:00", "PM", "America/Los_Angeles"};
    try {
      Project5.main(args);
    } catch (invalidDescriptionException | invalidOwnerException e) {
      fail("Unexpected exception thrown: " + e.getMessage());
    }

    assertTrue(file.exists());

    file.delete(); // Cleanup after test
  }


  @Test
  void dumpingAppointmentBookToXmlWritesCorrectContent() throws invalidOwnerException, invalidDescriptionException, IOException {
    String owner = "Test Owner";
    ZonedDateTime beginTime = ZonedDateTime.now().withZoneSameInstant(ZoneId.systemDefault());
    ZonedDateTime endTime = beginTime.plusHours(1);
    String description = "Test Appointment";

    AppointmentBook book = new AppointmentBook(owner);
    Appointment appointment = new Appointment(description, beginTime, endTime);
    book.addAppointment(appointment);

    StringWriter sw = new StringWriter();
    XmlDumper dumper = new XmlDumper(sw);
    dumper.dump(book);

    String dumpedContent = sw.toString();
    assertTrue(dumpedContent.contains(owner));
    assertTrue(dumpedContent.contains(description));
  }

  @Test
  @Disabled
  public void mismatchedOwnerNameInXmlPrintsErrorMessage() throws invalidDescriptionException, invalidOwnerException {
    ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    System.setErr(new PrintStream(errContent));

    Project4.main(new String[]{"-xmlFile", "src/test/resources/edu/pdx/cs410J/benlutz/valid-apptbook.xml", "MismatchedOwner", "Meeting", "01/01/2020", "12:00", "PM", "America/Los_Angeles", "01/01/2020", "1:00", "PM", "America/Los_Angeles"});

    String expectedError = "The owner name in the XML file does not match the provided owner name.";
    assertTrue(errContent.toString().contains(expectedError));

    System.setErr(System.err);
  }

  @Test
  public void beginTimeAfterEndTimeShowsError() throws invalidDescriptionException, invalidOwnerException {
    ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    System.setErr(new PrintStream(errContent));

    // Providing command line arguments that simulate begin time after end time
    String[] args = {
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
  public void textFileOptionWithoutFileNameShowsError() throws invalidDescriptionException, invalidOwnerException {
    ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    System.setErr(new PrintStream(errContent));

    String[] args = {"-textFile"};
    Project5.main(args);

    String expectedError = "Error: -textFile option requires a file name";
    assertTrue(errContent.toString().contains(expectedError));

    System.setErr(System.err); // Reset System.err to its original stream
  }

  @Test
  public void invalidFilePathFormatShowsError() {
    ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    System.setErr(new PrintStream(errContent));

    String[] args = {"-textFile", "invalid/?path.txt", "owner", "description", "01/01/2020", "12:00", "PM", "America/Los_Angeles", "01/01/2020", "1:00", "PM", "America/Los_Angeles"};
    try {
      Project5.main(args);
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

    String[] args = {"-textFile", fileName, "owner", "description",  "01/01/2020", "12:00", "PM", "America/Los_Angeles", "01/01/2020", "1:00", "PM", "America/Los_Angeles"};
    Project5.main(args);

    assertTrue(file.exists());

    file.delete(); // Cleanup after test
  }

  @Test
  public void relativeFilePathWorksCorrectly() throws invalidDescriptionException, invalidOwnerException {
    String fileName = "tempAppointmentBookRelative.txt";
    File file = new File(fileName);
    file.delete(); // Ensure the file does not exist before the test

    String[] args = {"-textFile", fileName, "owner", "description", "01/01/2020", "12:00", "PM", "America/Los_Angeles", "01/01/2020", "1:00", "PM", "America/Los_Angeles"};
    Project5.main(args);

    assertTrue(file.exists());

    file.delete(); // Cleanup after test
  }


  @Test
  public void nonExistentTextFileCreatesNewFile() {
    String fileName = "tempAppointmentBook.txt";
    File file = new File(fileName);
    file.delete(); // Ensure the file does not exist before the test

    String[] args = {"-textFile", fileName, "owner", "description",  "01/01/2020", "12:00", "PM", "America/Los_Angeles", "01/01/2020", "1:00", "PM", "America/Los_Angeles"};
    try {
      Project5.main(args);
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
  public void mismatchedOwnerNamePrintsErrorMessage() throws invalidDescriptionException, invalidOwnerException {
    ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    System.setErr(new PrintStream(errContent));

    Project5.main(new String[]{"-textFile", "src/test/resources/edu/pdx/cs410J/benlutz/valid-apptbook.txt", "MismatchedOwner", "Meeting", "01/01/2020", "12:00", "PM", "America/Los_Angeles", "01/01/2020", "1:00", "PM", "America/Los_Angeles"});

    String expectedError = "The owner name you provided does not match the owner name in the text file.";
    assertTrue(errContent.toString().contains(expectedError));

    System.setErr(System.err); // Reset System.err to its original stream
  }

  @Test
  void missingEndTimePrintsErrorToStandardError() {
    String[] args = {"Owner", "Description", "12/01/2020", "12:00", "12/01/2020"};
    MainMethodResult result = invokeMain(Project5.class, args);
    assertThat(result.getTextWrittenToStandardError(), containsString("Error: Missing end time"));
  }

  @Test
  void missingEndDateAndTimePrintsErrorToStandardError() {
    String[] args = {"Owner", "Description", "01/01/2020", "12:00", "PM", "America/Los_Angeles"};
    MainMethodResult result = invokeMain(Project5.class, args);
    assertThat(result.getTextWrittenToStandardError(), containsString("Error: Missing end time"));
  }

  @Test
  void invalidBeginDateFormatPrintsErrorToStandardError() {
    String[] args = {"Owner", "Description", "12-01-2020", "12:00", "12/01/2020", "13:00"};
    MainMethodResult result = invokeMain(Project5.class, args);
    assertThat(result.getTextWrittenToStandardError(), containsString("Invalid begin date/time format:"));
  }

  @Test
  void invalidBeginTimeFormatPrintsErrorToStandardError() {
    String[] args = {"Owner", "Description", "12/01/2020", "12:XX", "12/01/2020", "13:00"};
    MainMethodResult result = invokeMain(Project5.class, args);
    assertThat(result.getTextWrittenToStandardError(), containsString("Invalid begin date/time format:"));
  }

  @Test
  void invalidEndDateTimeFormatPrintsErrorToStandardError() {
    String[] args = {"Owner", "Description", "01/01/2020", "12:00", "PM", "America/Los_Angeles"};
    MainMethodResult result = invokeMain(Project5.class, args);
    assertThat(result.getTextWrittenToStandardError(), containsString("Invalid end date/time format:"));
  }

  @Test
  void invokingMainWithNoArgumentsPrintsMissingArgumentsToStandardError() {
    InvokeMainTestCase.MainMethodResult result = invokeMain(Project5.class);
    assertThat(result.getTextWrittenToStandardError(), containsString("Error: No command line arguments"));
  }

  //When there are less than 6 arguments not including options
  @Test
  void unknownCommandLineOptionPrintsErrorToStandardError() {
    InvokeMainTestCase.MainMethodResult result = invokeMain(Project5.class, "-print", "-unknownOption", "owner", "description",
            "begin date", "begin time", "end date", "end time");
    assertThat(result.getTextWrittenToStandardError(), containsString("Error: Invalid command line option"));
  }

  @Test
  void specifyingBothXmlFileAndTextFileOptionsPrintsError() {
    InvokeMainTestCase.MainMethodResult result = invokeMain(Project5.class, "-xmlFile", "xmlFileName.xml", "-textFile", "textFileName.txt", "Owner Name", "Description", "01/01/2024 10:00 AM", "America/Los_Angeles", "01/01/2024 11:00 AM", "America/Los_Angeles");
    assertThat(result.getTextWrittenToStandardError(), containsString("Error: It is invalid to specify both the -xmlFile and -textFile options"));
  }


  @Test
  void tooManyCommandLineArgumentsPrintsErrorToStandardError() {
    InvokeMainTestCase.MainMethodResult result = invokeMain(Project5.class, "Arg1", "Arg2", "Arg3", "Arg4", "Arg5",
            "Arg6", "Arg7", "Arg8", "Arg9", "Arg10", "Arg11", "Arg12", "Arg13", "Arg14", "Arg15", "Arg16", "Arg17");
    assertThat(result.getTextWrittenToStandardError(), containsString("Too many command line arguments"));
  }

}