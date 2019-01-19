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
package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.telemetry;

import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.BaseConfigurationDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.ConfigurationContext;

public class RestTelemetryTemplateDto extends BaseConfigurationDto {

   private String className;

   public RestTelemetryTemplateDto(ConfigurationContext context, String className) {
      super(context);
      this.className = className;
   }

   public String getPackageName() {
      return getBasePackage();
   }

   public String getClassName() {
      return className;
   }

   public String getFullyQualifiedName() {
      return getPackageName() + '.' + getClassName();
   }

}
