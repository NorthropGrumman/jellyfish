/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE: All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
 */
package com.ngc.seaside.jellyfish.cli.command.analyzestyle;

import com.ngc.seaside.jellyfish.service.analysis.api.ISystemDescriptorFindingType;
import com.ngc.seaside.jellyfish.utilities.command.AbstractJellyfishAnalysisCommand;

/**
 * Defines the different finding types for style analysis.
 */
public enum StyleFindingTypes implements ISystemDescriptorFindingType {

   // Use the singleton enum pattern for finding types. Use the markdown files under the docs directory for the
   // descriptions.

   /**
    * The type of finding for bad naming.
    */
   BAD_STYLE("badStyle", "docs/badStyle.md", Severity.WARNING),

   /**
    * The type of finding for bad project structure.
    */
   BAD_PROJECT("badProject", "docs/badProjectStructure.md", Severity.WARNING);

   private final String id;
   private final String description;
   private final Severity severity;

   StyleFindingTypes(String id,
                     String resource,
                     Severity severity) {
      this.id = id;
      this.description = AbstractJellyfishAnalysisCommand.getResource(resource, StyleFindingTypes.class);
      this.severity = severity;
   }

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
}
