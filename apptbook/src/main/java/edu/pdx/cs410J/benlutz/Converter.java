package edu.pdx.cs410J.benlutz;

import edu.pdx.cs410J.ParserException;

import java.io.*;

/**
 * The {@code Converter} class provides functionality to convert an appointment book from a text file format to an XML file format.
 * It reads an appointment book from a text file using {@link TextParser}, then writes the appointment book to an XML file using {@link XmlDumper}.
 */
public class Converter {

    /**
     * The main method performs the conversion from text file to XML file.
     *
     * @param args Command-line arguments: the first argument is the path to the source text file,
     *             and the second argument is the path to the destination XML file.
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: java edu.pdx.cs410J.benlutz.Converter textFile xmlFile");
            System.exit(1);
        }

        String textFilePath = args[0];
        String xmlFilePath = args[1];

        try {
            // Parse the text file
            FileReader textFileReader = new FileReader(textFilePath);
            TextParser parser = new TextParser(textFileReader);
            AppointmentBook book = parser.parse();

            // Dump to XML file
            FileWriter xmlFileWriter = new FileWriter(xmlFilePath);
            XmlDumper dumper = new XmlDumper(xmlFileWriter);
            dumper.dump(book);

            System.out.println("Successfully converted " + textFilePath + " to " + xmlFilePath);

        } catch (FileNotFoundException ex) {
            System.err.println("File not found: " + ex.getMessage());
            System.exit(1);
        } catch (IOException ex) {
            System.err.println("Error writing to XML file: " + ex.getMessage());
            System.exit(1);
        } catch (ParserException ex) {
            System.err.println("Error parsing text file: " + ex.getMessage());
            System.exit(1);
        }
    }
}
