package edu.pdx.cs410J.benlutz;

import edu.pdx.cs410J.AppointmentBookParser;
import edu.pdx.cs410J.ParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


/**
 * A parser that reads and parses an {@link AppointmentBook} from a text source.
 * This implementation of {@link AppointmentBookParser} is designed to parse
 * appointment book data from a {@link Reader} object
 */
public class TextParser implements AppointmentBookParser<AppointmentBook> {
  private final Reader reader;
  private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("M/d/yyyy h:mm a VV");


  /**
   * Creates a new parser that reads an appointment book from a given reader
   *
   * @param reader The source from which the appointment book data is read
   */
  public TextParser(Reader reader) {
    this.reader = reader;
  }

  /**
   * Parses an appointment book from the source reader
   * <p>
   * This method reads lines from the reader and constructs an appointment book.
   * The owner of the appointment book is read from the first line of the source.
   *
   * @return The parsed {@link AppointmentBook}
   * @throws ParserException If there is a problem reading from the reader
   *                         or if the appointment book data is invalid
   */
  @Override
  public AppointmentBook parse() throws ParserException {
    try (BufferedReader br = new BufferedReader(this.reader)) {
      String owner = br.readLine();
      if (owner == null || owner.isEmpty()) {
        owner = null;
      }

      AppointmentBook book = new AppointmentBook(owner);

      String line;
      while ((line = br.readLine()) != null) {
        String[] parts = line.split(", ");
        if (parts.length != 3) {
          throw new ParserException("Invalid appointment format in text file");
        }
        try {
          String description = parts[0];
          ZonedDateTime beginTime = ZonedDateTime.parse(parts[1], DATE_TIME_FORMATTER);
          ZonedDateTime endTime = ZonedDateTime.parse(parts[2], DATE_TIME_FORMATTER);

          Appointment appointment = new Appointment(description, beginTime, endTime);
          book.addAppointment(appointment);
        } catch (DateTimeParseException e) {
          throw new ParserException("Error parsing date/time: " + e.getMessage());
        } catch (invalidDescriptionException e) {
          throw new ParserException("Invalid appointment description: " + e.getMessage());
        }
      }
      return book;

    } catch (IOException e) {
      throw new ParserException("Error reading from file: " + e.getMessage());
    } catch (invalidOwnerException e) {
      throw new ParserException("Invalid owner name: " + e.getMessage());
    }
  }
}
