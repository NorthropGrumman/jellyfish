package com.ngc.seaside.bootstrap.api.command.impl.projectcommand;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

import com.ngc.seaside.bootstrap.api.IBootstrapCommand;
import com.ngc.seaside.command.api.ICommand;

/**
 * @author justan.provence@ngc.com
 */
public class ProjectBootstrapCommandModule extends AbstractModule {

   @Override
   protected void configure() {
      Multibinder.newSetBinder(binder(), IBootstrapCommand.class)
               .addBinding()
               .to(ProjectBootstrapCommand.class);

      Multibinder.newSetBinder(binder(), ICommand.class)
               .addBinding()
               .to(ProjectBootstrapCommand.class);
   }
}
