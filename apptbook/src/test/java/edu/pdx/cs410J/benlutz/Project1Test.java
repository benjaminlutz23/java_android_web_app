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
 * A unit test for code in the <code>Project1</code> class.  This is different
 * from <code>Project1IT</code> which is an integration test (and can capture data
 * written to {@link System#out} and the like.
 */
class Project1Test extends InvokeMainTestCase{

  @Test
  void tooManyArgumentsExcludingOptionsPrintsErrorToStandardError() {
    String[] args = {"-print", "owner", "description", "01/01/2024", "12:00", "01/01/2024", "13:00", "extra argument"};
    assertThrows(IllegalArgumentException.class, () -> Project1.main(args));
  }

  @Test
  void readmeCanBeReadAsResource() throws IOException {
    try (
      InputStream readme = Project1.class.getResourceAsStream("README.txt")
    ) {
      assertThat(readme, not(nullValue()));
      BufferedReader reader = new BufferedReader(new InputStreamReader(readme));
      String line = reader.readLine();
      assertThat(line, containsString("CS 510J Project 1: Appointment Book Application"));
    }
  }

  @Test
  void readmeFlagPrintsReadmeToStandardOut() {
    InvokeMainTestCase.MainMethodResult result = invokeMain(Project1.class, "-README");
    assertThat(result.getTextWrittenToStandardOut(), containsString("CS 510J Project 1: Appointment Book Application"));
  }

  @Test
  void printFlagPrintsAppointmentDetails() {
    String[] args = {"-print", "owner", "description", "01/01/2024", "12:00", "01/01/2024", "13:00"};
    InvokeMainTestCase.MainMethodResult result = invokeMain(Project1.class, args);
    assertThat(result.getTextWrittenToStandardOut(), containsString("description"));
  }

  @Test
  void invalidOptionPrintsErrorToStandardError() {
    InvokeMainTestCase.MainMethodResult result = invokeMain(Project1.class, "-invalidOption");
    assertThat(result.getTextWrittenToStandardError(), containsString("Error: Invalid command line option"));
  }
}
