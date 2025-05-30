package edu.pdx.cs410J.benlutz;

import edu.pdx.cs410J.InvokeMainTestCase;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * A unit test for code in the <code>Project5</code> class.  This is different
 * from <code>Project5IT</code> which is an integration test (and can capture data
 * written to {@link System#out} and the like.)
 */
class Project4Test extends InvokeMainTestCase {

    @Test
    void readmeCanBeReadAsResource() throws IOException {
        try (
                InputStream readme = Project5.class.getResourceAsStream("README.txt")
        ) {
            assertThat(readme, not(nullValue()));
            BufferedReader reader = new BufferedReader(new InputStreamReader(readme));
            String line = reader.readLine();
            assertThat(line, containsString("CS 510J Project 5: A REST-ful Appointment Book Web Service"));
        }
    }

    @Test
    void readmeFlagPrintsReadmeToStandardOut() {
        InvokeMainTestCase.MainMethodResult result = invokeMain(Project5.class, "-README");
        assertThat(result.getTextWrittenToStandardOut(), containsString("CS 510J Project 5: A REST-ful Appointment Book Web Service"));
    }
}