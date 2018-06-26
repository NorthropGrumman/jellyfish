package com.ngc.seaside.jellyfish.service.analysis.api;

import com.ngc.seaside.systemdescriptor.service.source.api.ISourceLocation;

import java.util.Optional;

/**
 * Represents and documents a finding found within a System Descriptor project. A finding could represent an error or
 * warning found in the project, or it could simply document information about the System Descriptor project itself.
 */
public class SystemDescriptorFinding {

   /**
    * Returns an identifier for categorizing this finding with other findings.
    * 
    * @return an identifier for categorizing this finding with other findings
    */
   public String getId() {
      return null;
   }

   /**
    * Returns a message detailing the finding, formatted in markdown. The message should begin with a header
    * representing the finding's title.
    * 
    * @return a message detailing the finding, formatted in markdown
    */
   public String getMessage() {
      return null;
   }

   /**
    * Returns the source code location of the finding, or {@link Optional#empty()} if the finding is not associated
    * with any particular source code.
    * 
    * @return the source code location of the finding
    */
   public Optional<ISourceLocation> getLocation() {
      return null;
   }

   /**
    * Returns the severity of the finding.
    * 
    * @return the severity of the finding
    */
   public Severity getSeverity() {
      return null;
   }

   /**
    * Returns a measure of the complexity required to fix this finding. While the value of the complexity is
    * discretionary, the values of different findings should be relatively proportional to their perceived complexity.
    * A complexity of {@code 0} represents no fix is necessary and should be returned when the {@link #getSeverity()
    * severity} is {@link Severity#INFO}.
    * 
    * @return a measure of the complexity required to fix this finding
    */
   public int getComplexity() {
      return 0;
   }

   /**
    * An enumeration describing the severity of a finding.
    */
   public static enum Severity {

      /**
       * Represents a finding that is not problematic.
       */
      INFO,

      /**
       * Represents a finding that, while not an error, may be problematic or represents bad practice.
       */
      WARNING,

      /**
       * Represents a finding that is an error.
       */
      ERROR

   }

}
