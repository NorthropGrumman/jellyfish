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
