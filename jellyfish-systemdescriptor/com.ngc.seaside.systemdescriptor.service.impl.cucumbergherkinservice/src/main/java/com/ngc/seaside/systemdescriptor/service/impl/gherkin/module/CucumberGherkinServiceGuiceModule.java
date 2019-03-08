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
package com.ngc.seaside.systemdescriptor.service.impl.gherkin.module;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import com.ngc.seaside.systemdescriptor.service.gherkin.api.IGherkinService;
import com.ngc.seaside.systemdescriptor.service.impl.gherkin.CucumberGherkinService;

/**
 * Registers the {@link CucumberGherkinService}.
 */
public class CucumberGherkinServiceGuiceModule extends AbstractModule {

   @Override
   protected void configure() {
      bind(CucumberGherkinService.class).in(Singleton.class);
      bind(IGherkinService.class).to(CucumberGherkinService.class);
   }
}
