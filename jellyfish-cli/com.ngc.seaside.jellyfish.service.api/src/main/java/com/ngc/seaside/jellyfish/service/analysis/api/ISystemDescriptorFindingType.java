/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.ngc.seaside.jellyfish.service.analysis.api;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.service.source.api.ISourceLocation;

/**
 * Represents a type of finding for a System Descriptor project.  Create new types by implementing this interface or
 * using {@link #createFindingType}.  Types are identified by their {@link #getId() IDs}.
 *
 * <p/>
 * A finding could represent an error or warning found in the project, or it could simply document information about the
 * System Descriptor project itself. Actual findings are created using {@link #createFinding}.
 */
public interface ISystemDescriptorFindingType {

   /**
    * Gets the unique ID of this finding type.  Each type must have a unique ID.
    */
   String getId();

   /**
    * Returns a description, in markdown, describing this type of finding. The description should begin with a header
    * representing the type's title.
    *
    * @return a description of this type of finding
    */
   String getDescription();

   /**
    * Returns the severity of this type of finding.
    *
    * @return the severity of this type of finding
    */
   Severity getSeverity();

   /**
    * An enumeration describing the severity of this type of finding.
    */
   enum Severity {

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

   /**
    * Creates a finding for this type.
    *
    * @param message    {@link SystemDescriptorFinding#getMessage() message} of the finding, formatted in markdown
    * @param location   the source code {@link SystemDescriptorFinding#getLocation() location} of the finding, may be
    *                   null
    * @param complexity the {@link SystemDescriptorFinding#getComplexity() complexity} of the finding
    * @return a finding for this type
    */
   default SystemDescriptorFinding<ISystemDescriptorFindingType> createFinding(String message,
                                                                               ISourceLocation location,
                                                                               double complexity) {
      return new SystemDescriptorFinding<>(this, message, location, complexity);
   }

   /**
    * Creates a new finding type with the given ID, description, and severity.  The returned {@code
    * ISystemDescriptorFindingType} contains an implementation of {@code hashCode} and {@code equals} such that any
    * finding types with the same ID are considered equal.
    *
    * @param id          the unique ID of the finding type
    * @param description the description of the finding type
    * @param severity    the severity of the finding type
    */
   static ISystemDescriptorFindingType createFindingType(String id, String description, Severity severity) {
      Preconditions.checkNotNull(id, "id may not be null!");
      Preconditions.checkArgument(!id.trim().isEmpty(), "id may not be empty!");
      Preconditions.checkNotNull(description, "description) may not be null!");
      Preconditions.checkArgument(!description.trim().isEmpty(), "description may not be empty!");
      Preconditions.checkNotNull(severity, "severity may not be null!");
      return new ISystemDescriptorFindingType() {
         @Override
         public String getId() {
            return id;
         }

         @Override
         public String getDescription() {
            return description;
         }

         @Override
         public Severity getSeverity() {
            return severity;
         }

         @Override
         public boolean equals(Object o) {
            if (this == o) {
               return true;
            }
            if (!(o instanceof ISystemDescriptorFindingType)) {
               return false;
            }
            ISystemDescriptorFindingType that = (ISystemDescriptorFindingType) o;
            return id.equals(that.getId());
         }

         @Override
         public int hashCode() {
            return id.hashCode();
         }

         @Override
         public String toString() {
            return id;
         }
      };
   }
}
