package edu.pdx.cs410J.benlutz;

import edu.pdx.cs410J.ParserException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;

public class TextParserTest {

  @Test
  void validTextFileCanBeParsed() throws ParserException, invalidOwnerException, invalidDescriptionException {
    InputStream resource = getClass().getResourceAsStream("valid-apptbook.txt");
    assertNotNull(resource, "Resource file should exist");

    TextParser parser = new TextParser(new InputStreamReader(resource));
    AppointmentBook book = parser.parse();
    assertNotNull(book, "Book should not be null");
    assertEquals("Test Appointment Book", book.getOwnerName(), "Owner name should match");

    // Assert the book contains at least one appointment
    assertFalse(book.getAppointments().isEmpty(), "Appointment book should contain at least one appointment");
  }


  @Test
  void invalidTextFileThrowsParserExceptionForInvalidAppointmentFormat() {
    // Load the resource file
    InputStream resource = getClass().getResourceAsStream("invalid-appt-format-apptbook.txt");
    assertNotNull(resource, "Resource file should exist");

    // Create a TextParser instance with the resource file
    TextParser parser = new TextParser(new InputStreamReader(resource));

    // Assert that parsing the file throws a ParserException with the expected message
    Exception exception = assertThrows(ParserException.class, parser::parse, "Parsing should fail due to invalid appointment format");
    assertTrue(exception.getMessage().contains("Invalid appointment format"), "Exception message should indicate invalid appointment format");
  }


  @Test
  void invalidTextFileThrowsParserExceptionForInvalidDateTime() {
    InputStream resource = getClass().getResourceAsStream("invalid-datetime-apptbook.txt");
    assertNotNull(resource, "Resource file should exist");

    TextParser parser = new TextParser(new InputStreamReader(resource));
    Exception exception = assertThrows(ParserException.class, parser::parse, "Parsing should fail due to invalid date/time parsing");
    assertTrue(exception.getMessage().contains("Error parsing date/time"), "Exception message should indicate date/time parsing error");
  }

  @Test
  void emptyAppointmentsTextFileCanBeParsed() throws ParserException, invalidOwnerException {
    InputStream resource = getClass().getResourceAsStream("empty-apptbook.txt"); // Assuming this file contains only an owner name
    assertNotNull(resource, "Resource file should exist");

    TextParser parser = new TextParser(new InputStreamReader(resource));
    AppointmentBook book = parser.parse();
    assertNotNull(book, "Book should not be null");
    assertTrue(book.getAppointments().isEmpty(), "Appointment book should be empty");
  }

}
