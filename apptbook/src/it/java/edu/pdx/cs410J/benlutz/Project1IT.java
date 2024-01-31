package edu.pdx.cs410J.benlutz;

import edu.pdx.cs410J.InvokeMainTestCase;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Integration tests for the {@link Project1} main class.
 */
class Project1IT extends InvokeMainTestCase {

  @Test
  void invokingMainWithNoArgumentsPrintsMissingArgumentsToStandardError() {
    InvokeMainTestCase.MainMethodResult result = invokeMain(Project1.class);
    assertThat(result.getTextWrittenToStandardError(), containsString("Error: No command line arguments"));
  }

  //When there are less than 6 arguments
  @Test
  void tooManyCommandLineArgumentsPrintsErrorToStandardError() {
    InvokeMainTestCase.MainMethodResult result = invokeMain(Project1.class, "Opt1", "Opt2", "Arg3", "Arg4", "Arg5",
            "Arg6", "Arg7", "Arg8", "Arg9");
    assertThat(result.getTextWrittenToStandardError(), containsString("Too many command line arguments"));
  }

  @Test
  void missingCommandLineArgumentsPrintsErrorToStandardError() {
    InvokeMainTestCase.MainMethodResult result = invokeMain(Project1.class, "-print", "owner", "description",
            "11/30/2000", "07:00", "11/30/2000");
    assertThat(result.getTextWrittenToStandardError(), containsString("All fields are required (i.e. Owner Name, " +
            "Description, Begin Date/Time, End Date/Time)"));
  }

}