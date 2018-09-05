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
package com.ngc.seaside.jellyfish.service.name.module;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import com.ngc.seaside.jellyfish.service.name.api.IPackageNamingService;
import com.ngc.seaside.jellyfish.service.name.api.IProjectNamingService;
import com.ngc.seaside.jellyfish.service.name.packagez.impl.PackageNamingService;
import com.ngc.seaside.jellyfish.service.name.project.impl.ProjectNamingService;

public class NameServicesGuiceModule extends AbstractModule {

   @Override
   protected void configure() {
      bind(IPackageNamingService.class).to(PackageNamingService.class).in(Singleton.class);
      bind(IProjectNamingService.class).to(ProjectNamingService.class).in(Singleton.class);
   }
}
