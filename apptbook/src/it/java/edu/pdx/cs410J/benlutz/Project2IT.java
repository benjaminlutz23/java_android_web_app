package edu.pdx.cs410J.benlutz;

import edu.pdx.cs410J.InvokeMainTestCase;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Integration tests for the {@link Project2} main class.
 */
class Project2IT extends InvokeMainTestCase {

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