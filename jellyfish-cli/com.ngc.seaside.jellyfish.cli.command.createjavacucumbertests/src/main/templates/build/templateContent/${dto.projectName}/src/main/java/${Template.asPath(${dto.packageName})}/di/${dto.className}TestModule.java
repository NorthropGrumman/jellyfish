package ${dto.packageName}.di;

import com.google.inject.AbstractModule;

import ${dto.configPackageName}.${dto.className}TestConfiguration;

/**
 * This module configures Guice bindings for the ${dto.className} steps.
 */
public class ${dto.className}TestModule extends AbstractModule {

   @Override
   protected void configure() {
      bind(${dto.className}TestConfiguration.class).asEagerSingleton();
   }
}