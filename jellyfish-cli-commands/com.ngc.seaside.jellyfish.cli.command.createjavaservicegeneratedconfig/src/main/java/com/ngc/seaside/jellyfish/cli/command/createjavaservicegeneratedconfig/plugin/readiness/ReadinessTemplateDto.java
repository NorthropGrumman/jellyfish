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
package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.readiness;

import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.BaseConfigurationDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.ConfigurationContext;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.ClassDto;

import java.util.LinkedHashSet;
import java.util.Set;

public class ReadinessTemplateDto extends BaseConfigurationDto {
   private final Set<String> classes = new LinkedHashSet<>();
   private final Set<String> eventSubscribers = new LinkedHashSet<>();
   private final Set<String> components = new LinkedHashSet<>();
   private ClassDto adviser;

   public ReadinessTemplateDto(ConfigurationContext context) {
      super(context);
   }

   public String getPackageName() {
      return getBasePackage();
   }

   public Set<String> getClasses() {
      return classes;
   }

   public Set<String> getEventSubscribers() {
      return eventSubscribers;
   }

   public Set<String> getComponents() {
      return components;
   }

   public ReadinessTemplateDto addClass(String cls) {
      classes.add(cls);
      return this;
   }

   public ReadinessTemplateDto addEventSubscriber(String eventSubscriber) {
      eventSubscribers.add(eventSubscriber);
      return this;
   }

   public ReadinessTemplateDto addComponent(String component) {
      components.add(component);
      return this;
   }

   public ClassDto getAdviser() {
      return adviser;
   }

   public ReadinessTemplateDto setAdviser(ClassDto adviser) {
      this.adviser = adviser;
      return this;
   }
}
