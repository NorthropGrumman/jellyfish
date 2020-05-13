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
package com.ngc.seaside.jellyfish.service.name.api;

/**
 * Contains a collection of project related information.
 */
public interface IProjectInformation {

   /**
    * Gets the name of the directory that will contain a project.  This can be referred in the {@code include} line of
    * {@code settings.gradle} file.
    */
   String getDirectoryName();

   /**
    * Gets the artifact ID of the project.
    */
   String getArtifactId();

   /**
    * Gets the group ID of the project.
    */
   String getGroupId();

   /**
    * Gets the name of a dynamic property that can be used to represent the version of this project.  This can be useful
    * for commands that need to generate a {@code build.gradle} file, particular a top level {@code build.gradle} file
    * that contains properties for versions.  As an example, a project with an artifact ID of {@code someservice.events}
    * would have a version property name of {@code someServiceEventsVersion}.
    */
   String getVersionPropertyName();

   /**
    * Gets a GAV formatted string that can be used in a Gradle file when declaring dependencies.  This string contains
    * the group ID, artifact ID, and uses the {@link #getVersionPropertyName() version property} as the version.  For
    * example, a project with a group ID of {@code com.ngc.foo} and an artifact ID of {@code someservice.events} would
    * have a GAV formatted string of {@code "com.ngc.foo:someservice.events:$someServiceEventsVersion"};
    */
   String getGavFormattedString();
}
