/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
 */
package com.ngc.seaside.systemdescriptor.service.gherkin.api;

import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.service.api.IParsingResult;

import java.nio.file.Path;

/**
 * Provides operations to parse Gherkin feature files.
 */
public interface IGherkinService {

   /**
    * Parses the feature files associated with then given parsed System Descriptor project.  This is the most common way
    * to obtain information about feature files.
    *
    * @param parsingResult the result of parsing the System Descriptor project
    * @return the result of parsing the feature files
    */
   default IGherkinParsingResult parseProject(IParsingResult parsingResult) {
      return parseRecursively(parsingResult.getTestSourcesRoot(), parsingResult.getSystemDescriptor());
   }

   /**
    * Recursively searches for feature files in the given directory and parses them.  If no feature files are
    * discovered, the result will contain an empty list of features.
    *
    * @param directoryContainingFeatureFiles the directory to recursively search for feature files
    * @param systemDescriptor                the System Descriptor object used to map System Descriptor scenarios to
    *                                        feature files and
    *                                        vice versa
    * @return the result of parsing any discovered feature files
    */
   IGherkinParsingResult parseRecursively(Path directoryContainingFeatureFiles, ISystemDescriptor systemDescriptor);

}
