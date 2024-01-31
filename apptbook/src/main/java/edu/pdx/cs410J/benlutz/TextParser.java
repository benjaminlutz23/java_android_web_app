package edu.pdx.cs410J.benlutz;

import edu.pdx.cs410J.AppointmentBookParser;
import edu.pdx.cs410J.ParserException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;


/**
 * A parser that reads and parses an {@link AppointmentBook} from a text source.
 * This implementation of {@link AppointmentBookParser} is designed to parse
 * appointment book data from a {@link Reader} object
 */
public class TextParser implements AppointmentBookParser<AppointmentBook> {
  private final Reader reader;

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
    try (
      BufferedReader br = new BufferedReader(this.reader)
    ) {

      String owner = br.readLine();

      if (owner == null) {
        throw new ParserException("Missing owner");
      }

      return new AppointmentBook(owner);

    } catch (IOException e) {
      throw new ParserException("While parsing appointment book text", e);
    } catch (invalidOwnerException e) {
      System.err.println("Invalid owner name");
    }

      return null;
  }
}
