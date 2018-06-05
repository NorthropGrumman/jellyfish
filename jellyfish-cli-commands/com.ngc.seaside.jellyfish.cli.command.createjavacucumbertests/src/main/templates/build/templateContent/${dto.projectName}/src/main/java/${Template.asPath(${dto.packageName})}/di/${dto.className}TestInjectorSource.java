package ${dto.packageName}.di;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;

import com.ngc.blocs.guice.module.LogServiceModule;
import com.ngc.blocs.guice.module.ResourceServiceModule;
import com.ngc.blocs.guice.module.ThreadServiceModule;
#if ($dto.isConfigGenerated())
import ${dto.configModulePackage}.${dto.configModuleType};
#else
import com.ngc.seaside.service.transport.impl.defaultz.module.DefaultTransportServiceModule;
#end

import cucumber.api.guice.CucumberModules;
import cucumber.runtime.java.guice.InjectorSource;

/**
 * This class handles the creation of the Guice injector.
 */
public class ${dto.className}TestInjectorSource implements InjectorSource {

   @Override
   public Injector getInjector() {
      return Guice.createInjector(Stage.PRODUCTION, CucumberModules.SCENARIO,
                                  new LogServiceModule(),
                                  new ResourceServiceModule(),
                                  new ThreadServiceModule(),
#if ($dto.isConfigGenerated())
                                  new ${dto.configModuleType}(),
#else
                                  new DefaultTransportServiceModule(),
#end
                                  new ${dto.className}TestModule());
   }
}
