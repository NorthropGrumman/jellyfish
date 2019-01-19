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
package com.ngc.seaside.jellyfish.sonarqube;

import com.ngc.seaside.jellyfish.sonarqube.extension.DefaultJellyfishModuleFactory;
import com.ngc.seaside.jellyfish.sonarqube.language.SystemDescriptorLanguage;
import com.ngc.seaside.jellyfish.sonarqube.profile.DefaultSystemDescriptorQualityProfile;
import com.ngc.seaside.jellyfish.sonarqube.properties.SystemDescriptorProperties;
import com.ngc.seaside.jellyfish.sonarqube.rule.SystemDescriptorRulesDefinition;
import com.ngc.seaside.jellyfish.sonarqube.sensor.SystemDescriptorSensor;

import org.sonar.api.Plugin;
import org.sonar.api.utils.log.Loggers;

/**
 * The main entry point for the Sonarqube Jellyfish plugin.  Users can extend this plugin to include custom Jellyfish
 * analysis, profiles, and other features.  To do this, create a new Sonarqube plugin project that applies the
 * 'sonar-packaging' plugin, declares the JAR with class as a dependency, and configures the main plugin class:
 *
 * <pre>{@code
 * apply plugin: 'com.iadams.sonar-packaging'
 *
 * dependency {
 *    compile "com.ngc.seaside:jellyfish.sonarqube.plugin:$jellyfishVersion"
 * }
 *
 * sonarPackaging {
 *    pluginClass = 'my.custom.sonarqube.plugin.CustomSonarqubeJellyfishPlugin'
 * }
 * } </pre>
 * Add any dependencies that contains custom Jellyfish scenario verb handlers or analysis plugins.
 *
 * <p>
 * Next, create a new class named {@code my.custom.sonarqube.plugin.CustomSonarqubeJellyfishPlugin}.  This class should
 * extend {@code JellyfishPlugin}.  Override {@link #configureDefaultModuleFactory(DefaultJellyfishModuleFactory)} to
 * add any modules for custom verbs or analysis like so:
 *
 * <pre>{@code
 *   @Override
 *   protected void configureDefaultModuleFactory(DefaultJellyfishModuleFactory factory) {
 *     super.configureDefaultModuleFactory(factory);
 *     // Add any modules here.
 *     factory.addModules(new CustomFooAnalysisModule(), CustomBarScenarioVerbHandleModule());
 *   }
 * }</pre>
 *
 * <p>
 * Finally, override {@link #registerProfiles(Context)} to add any new profiles.  Make sure the profile is associated
 * with the System Descriptor language like this:
 *
 * <pre>{@code
 *  @Override
 *  public void define(Context context) {
 *    NewBuiltInQualityProfile profile = context.createBuiltInQualityProfile(NAME, SystemDescriptorLanguage.KEY);
 *  }
 * }</pre>
 */
public class JellyfishPlugin implements Plugin {

   @Override
   public void define(Context c) {
      // Register all extension points here.
      registerLanguage(c);
      registerRules(c);
      registerProfiles(c);
      registerSensor(c);
      registerProperties(c);
      registerModuleFactory(c);

      Loggers.get(getClass()).info(getClass().getName() + " successfully installed.");
   }

   protected void configureDefaultModuleFactory(DefaultJellyfishModuleFactory factory) {
      factory.setIncludeDefaultModules(true);
   }

   protected void registerModuleFactory(Context c) {
      DefaultJellyfishModuleFactory factory = new DefaultJellyfishModuleFactory();
      configureDefaultModuleFactory(factory);
      c.addExtension(factory);
   }

   protected void registerRules(Context c) {
      c.addExtension(SystemDescriptorRulesDefinition.class);
   }

   protected void registerProfiles(Context c) {
      c.addExtension(DefaultSystemDescriptorQualityProfile.class);
   }

   protected void registerProperties(Context c) {
      c.addExtensions(SystemDescriptorProperties.getProperties());
   }

   protected void registerSensor(Context c) {
      c.addExtension(SystemDescriptorSensor.class);
   }

   protected void registerLanguage(Context c) {
      c.addExtension(SystemDescriptorLanguage.class);
   }
}
