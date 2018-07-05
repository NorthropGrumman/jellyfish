package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.readiness;

import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.BaseConfigurationDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.ConfigurationContext;

import java.util.LinkedHashSet;
import java.util.Set;

public class ReadinessTemplateDto extends BaseConfigurationDto {
   private final Set<String> classes = new LinkedHashSet<>();
   private final Set<String> eventSubscribers = new LinkedHashSet<>();
   private final Set<String> components = new LinkedHashSet<>();

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
}
