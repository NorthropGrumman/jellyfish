package com.ngc.seaside.jellyfish.service.buildmgmt.impl.buildmgmtservice;

import com.ngc.seaside.jellyfish.service.buildmgmt.api.DependencyScope;
import com.ngc.seaside.jellyfish.service.buildmgmt.impl.buildmgmtservice.config.DependenciesConfiguration;

import static com.ngc.seaside.jellyfish.service.buildmgmt.impl.buildmgmtservice.config.DependenciesConfiguration.currentJellyfishVersion;

public class DefaultDependenciesConfiguration {

   private DefaultDependenciesConfiguration() {
   }

   public static DependenciesConfiguration getConfig() {
      DependenciesConfiguration config = new DependenciesConfiguration();

      config.addGroup()
            .usingVersionPropertyNamed("starfishVersion")
            .atVersion("2.1.0")
            .withDefaultGroupId("com.ngc.seaside")
            .withDefaultScope(DependencyScope.BUILD);


//      DependenciesConfiguration config = new DependenciesConfiguration()
//            .addGroup()
//            .usingVersionPropertyNamed("starfishVersion")
//            .atVersion("2.1.0")
//            .atVersion(config.currentJellyfishVersion())
//            .withDefaultGropuId("mygroupId")
//            .withDefaultScope(DependencyScope.BUILD)
//            .includes()
//            .inScope(DependencyScope.BUILD)
//            .groupId("com.ngc.seaside")
//            .artifactId("service.api")
//

      return null;
   }
}
