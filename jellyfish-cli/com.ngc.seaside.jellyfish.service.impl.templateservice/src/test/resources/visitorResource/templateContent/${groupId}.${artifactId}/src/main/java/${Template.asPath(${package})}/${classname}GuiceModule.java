package ${package};

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.ICommand;

/**
 * Configure the service for use in Guice
 */
public class ${classname}GuiceModule extends AbstractModule  {

   @Override
   protected void configure() {
      Multibinder.newSetBinder(binder(), IJellyFishCommand.class)
               .addBinding().to(${classname}GuiceWrapper.class);
      Multibinder.newSetBinder(binder(), ICommand.class)
               .addBinding().to(${classname}GuiceWrapper.class);
   }
}
