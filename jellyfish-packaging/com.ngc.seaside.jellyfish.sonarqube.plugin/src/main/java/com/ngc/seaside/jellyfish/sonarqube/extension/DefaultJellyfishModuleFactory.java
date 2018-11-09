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
package com.ngc.seaside.jellyfish.sonarqube.extension;

import com.google.common.base.Preconditions;
import com.google.inject.Module;

import com.ngc.blocs.guice.module.LogServiceModule;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.sonarqube.module.JellyfishSonarqubePluginModule;
import com.ngc.seaside.jellyfish.sonarqube.module.NoOpLogServiceModule;
import com.ngc.seaside.jellyfish.sonarqube.module.SonarqubeLogServiceModule;

import org.sonar.api.batch.ScannerSide;
import org.sonar.api.server.ServerSide;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

@ScannerSide
@ServerSide
public class DefaultJellyfishModuleFactory implements IJellyfishModuleFactory {

   /**
    * All configured modules.
    */
   private final Collection<Module> configuredModules = new HashSet<>();

   /**
    * If true, include the default Jellyfish modules.
    */
   private boolean includeDefaultModules = true;

   @Override
   public Collection<Module> getJellyfishModules(boolean loggingEnabled) {
      Collection<Module> modules = new HashSet<>(configuredModules);
      modules.add(getLoggingModule(loggingEnabled));
      modules.add(new JellyfishSonarqubePluginModule()
                        .setIncludeDefaultModules(includeDefaultModules)
                        .setFilter(this::shouldModuleBeExcluded));
      modules.removeIf(this::shouldModuleBeExcluded);
      return modules;
   }

   /**
    * Includes the given modules in the configuration when running Jellyfish.
    *
    * @param module  the module to include when running Jellyfish
    * @param modules the modules to include when running Jellyfish
    * @return this factory
    */
   public DefaultJellyfishModuleFactory addModules(Module module, Module... modules) {
      this.configuredModules.add(Preconditions.checkNotNull(module, "module may not be null!"));
      if (modules != null) {
         this.configuredModules.addAll(Arrays.asList(modules));
      }
      return this;
   }

   /**
    * Includes the given modules in the configuration when running Jellyfish.
    *
    * @param modules the modules to include when running Jellyfish
    * @return this factory
    */
   public DefaultJellyfishModuleFactory addModules(Collection<Module> modules) {
      this.configuredModules.addAll(Preconditions.checkNotNull(modules, "modules may not be null!"));
      return this;
   }

   /**
    * Sets if the default Jellyfish modules should be included.  The default is true.  Use this operation to change
    * the default Jellyfish modules used when running Jellyfish.  Note that if a value of false is used, users must
    * use {@link #addModules(Module, Module...) add} <i>all</i> modules needed by Jellyfish including any modules
    * usually included by default.
    *
    * @param includeDefaultModules true if the default modules should be included, false otherwise
    * @return this factory
    */
   public DefaultJellyfishModuleFactory setIncludeDefaultModules(boolean includeDefaultModules) {
      this.includeDefaultModules = includeDefaultModules;
      return this;
   }

   /**
    * Invoked to get the module that contains a binding for the {@link ILogService}.  Sometimes, a plugin may wish to
    * suppress all Jellyfish logging for certain operations.  In this case, {@code loggingEnabled} will be false and
    * the returned module should suppress logging.
    *
    * @param loggingEnabled true if logging should be enabled, false if logging should be disabled
    * @return the logging module
    */
   protected Module getLoggingModule(boolean loggingEnabled) {
      return loggingEnabled ? new SonarqubeLogServiceModule()
                            : new NoOpLogServiceModule();
   }

   /**
    * Invoked to determine if the given module should be included when running Jellyfish to perform Sonarqube analysis.
    * The default implementation filters out {@link LogServiceModule} so that the Sonarqube logging bridges can be used.
    * Extending lasses can override this method to exclude additional modules.
    *
    * @param module the module to test
    * @return true if the module should not be used to run Jellyfish, true otherwise
    */
   protected boolean shouldModuleBeExcluded(Module module) {
      return module.getClass() == LogServiceModule.class;
   }
}
