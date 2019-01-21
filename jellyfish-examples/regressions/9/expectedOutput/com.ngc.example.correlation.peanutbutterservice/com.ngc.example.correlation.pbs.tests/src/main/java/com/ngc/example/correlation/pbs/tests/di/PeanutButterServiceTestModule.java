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
package com.ngc.example.correlation.pbs.tests.di;

import com.google.inject.AbstractModule;

import com.ngc.example.correlation.pbs.api.IPeanutButterServiceAdviser;
import com.ngc.example.correlation.pbs.tests.config.PeanutButterServiceTestTransportConfiguration;

/**
 * This module configures Guice bindings for the PeanutButterService steps.
 */
public class PeanutButterServiceTestModule extends AbstractModule {

   @Override
   protected void configure() {
      bind(PeanutButterServiceTestTransportConfiguration.class).asEagerSingleton();
      bind(IPeanutButterServiceAdviser.class).to(PeanutButterServiceTestAdviser.class);
   }
}
