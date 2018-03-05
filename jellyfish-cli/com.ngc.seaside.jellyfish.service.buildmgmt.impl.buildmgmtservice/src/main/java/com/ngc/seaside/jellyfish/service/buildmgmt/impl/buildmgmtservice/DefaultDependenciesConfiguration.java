package com.ngc.seaside.jellyfish.service.buildmgmt.impl.buildmgmtservice;

import com.ngc.seaside.jellyfish.service.buildmgmt.api.DependencyScope;
import com.ngc.seaside.jellyfish.service.buildmgmt.impl.buildmgmtservice.config.DependenciesConfiguration;

public class DefaultDependenciesConfiguration {

   private DefaultDependenciesConfiguration() {
   }

   public static DependenciesConfiguration getConfig() {
      DependenciesConfiguration config = new DependenciesConfiguration();

      config.addGroup()
            .versionPropertyName("starfishVersion")
            .version("2.1.0")
            .defaultGroupId("com.ngc.seaside")
            .defaultScope(DependencyScope.BUILD);


//      DependenciesConfiguration config = new DependenciesConfiguration()
//            .addGroup()
//            .versionPropertyName("starfishVersion")
//            .version("2.1.0")
//            .version(config.currentJellyfishVersion())
//            .withDefaultGropuId("mygroupId")
//            .defaultScope(DependencyScope.BUILD)
//            .includes()
//            .inScope(DependencyScope.BUILD)
//            .groupId("com.ngc.seaside")
//            .artifactId("service.api")
//

      return null;
   }
}
