package edu.pdx.cs410J.benlutz;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Unit tests for the Student class.  In addition to the JUnit annotations,
 * they also make use of the <a href="http://hamcrest.org/JavaHamcrest/">hamcrest</a>
 * matchers for more readable assertion statements.
 */
public class StudentTest
{

  @Test
  void studentNamedPatIsNamedPat() throws InvalidGpaException {
    // GIVEN: I've created a Student with a name
    String name = "Pat";

    var pat = new Student(name, new ArrayList<>(), 0.0, "Doesn't matter");
    assertThat(pat.getName(), equalTo(name));
  }

  @Test
  void studentWithGpaGreaterThan4ThrowsInvalidGpaException() {
    // Given: A student named "Sharon" who is
    //    taking "Java" and "Front end" has GPA
    //    of 5.00, has "female" gender
    ArrayList<String> classes = new ArrayList<>();
    classes.add("Java");
    classes.add("Front end");
    try {
      new Student("Sharon", classes, 5.0, "female");
      fail("Should have thrown an invalidGpaException");
    } catch (InvalidGpaException ex) {
      // We expect this exception
    }
  }

}
