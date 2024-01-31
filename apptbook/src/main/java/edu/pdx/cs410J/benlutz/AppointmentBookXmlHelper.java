package edu.pdx.cs410J.benlutz;

import edu.pdx.cs410J.ProjectXmlHelper;

/**
 * Helper class for handling XML operations for {@link AppointmentBook}.
 * This class extends {@link ProjectXmlHelper} and provides XML parsing and serialization
 * capabilities specific to the {@link AppointmentBook} class
 */
public class AppointmentBookXmlHelper extends ProjectXmlHelper {

  /**
   * The System ID for the Family Tree DTD
   */
  protected static final String SYSTEM_ID =
    "http://www.cs.pdx.edu/~whitlock/dtds/apptbook.dtd";

  /**
   * The Public ID for the Family Tree DTD
   */
  protected static final String PUBLIC_ID =
    "-//Portland State University//DTD CS410J Appointment Book//EN";

  /**
   * Constructs an instance of AppointmentBookXmlHelper
   * This constructor initializes the XML helper with specific DTD information for appointment books
   */
  protected AppointmentBookXmlHelper() {
    super(PUBLIC_ID, SYSTEM_ID, "apptbook.dtd");
  }
}
