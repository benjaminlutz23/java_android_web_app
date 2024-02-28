package edu.pdx.cs410J.benlutz;

import edu.pdx.cs410J.ParserException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConverterTest {

    @Test
    public void testConvertTextFileToXml() throws IOException {
        // Create a temporary text file
        Path textFilePath = Files.createTempFile("appointmentBook", ".txt");
        try (BufferedWriter writer = Files.newBufferedWriter(textFilePath)) {
            writer.write("Owner\n");
            // Include a timezone ID in the date and time strings
            writer.write("Meeting, 01/01/2020 09:00 AM America/New_York, 01/01/2020 10:00 AM America/New_York");
        }

        // Path for the XML file
        Path xmlFilePath = Files.createTempFile("appointmentBook", ".xml");

        // Convert text to XML
        Converter.main(new String[]{textFilePath.toString(), xmlFilePath.toString()});

        // Read XML file and assert its contents
        assertTrue(Files.exists(xmlFilePath), "XML file should exist after conversion");
        String xmlContent = new String(Files.readAllBytes(xmlFilePath));
        assertTrue(xmlContent.contains("<owner>Owner</owner>"), "XML should contain the owner");
        assertTrue(xmlContent.contains("<description>Meeting</description>"), "XML should contain the appointment description");

        // Cleanup
        Files.deleteIfExists(textFilePath);
        Files.deleteIfExists(xmlFilePath);
    }
}
