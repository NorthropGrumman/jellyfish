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
