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

import com.google.inject.Module;

import com.ngc.seaside.jellyfish.sonarqube.JellyfishPlugin;

import java.util.Collection;

/**
 * A basic extension point that can allow Sonarqube plugin writers to create a new Sonarqube plugin that contains
 * additional analysis, profiles, etc.  This factory should produce the modules used to run Jellyfish.  Most of the
 * time,
 * users can configure the {@link DefaultJellyfishModuleFactory} via
 * {@link JellyfishPlugin#configureDefaultModuleFactory(DefaultJellyfishModuleFactory)}
 * instead of implementing this interface directly.
 */
public interface IJellyfishModuleFactory {

   /**
    * Gets the modules that should be used when running Jellyfish.
    *
    * @param loggingEnabled if true, a module that enables logging should included.  If this value is false, a module
    *                       that disabled logging should be included
    * @return the modules used to run Jellyfish
    */
   Collection<Module> getJellyfishModules(boolean loggingEnabled);


}
