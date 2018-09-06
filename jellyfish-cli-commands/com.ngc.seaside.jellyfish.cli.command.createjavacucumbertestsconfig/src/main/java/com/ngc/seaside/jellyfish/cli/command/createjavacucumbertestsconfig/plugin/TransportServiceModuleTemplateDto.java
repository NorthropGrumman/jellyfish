/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
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
package com.ngc.seaside.jellyfish.cli.command.createjavacucumbertestsconfig.plugin;

import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.BaseConfigurationDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.ConfigurationContext;

import java.util.LinkedHashSet;
import java.util.Set;

public class TransportServiceModuleTemplateDto extends BaseConfigurationDto {

   private final Set<String> modules = new LinkedHashSet<>();

   public TransportServiceModuleTemplateDto(ConfigurationContext context) {
      super(context);
   }

   public TransportServiceModuleTemplateDto addModule(String module) {
      this.modules.add(module);
      return this;
   }

   public String getPackageName() {
      return getBasePackage();
   }

   public Set<String> getModules() {
      return modules;
   }

}
