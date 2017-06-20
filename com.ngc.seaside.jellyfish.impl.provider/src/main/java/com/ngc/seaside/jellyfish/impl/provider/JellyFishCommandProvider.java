package com.ngc.seaside.jellyfish.impl.provider;

import com.ngc.seaside.command.api.IUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandProvider;

import org.osgi.service.component.annotations.Component;

/**
 * Default implementation of the IJellyFishCommandProvider interface.
 */
@Component(service = IJellyFishCommandProvider.class)
public class JellyFishCommandProvider implements IJellyFishCommandProvider {

   @Override
   public IUsage getUsage() {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public void addCommand(IJellyFishCommand command) {
      // TODO Auto-generated method stub
      
   }

   @Override
   public void removeCommand(IJellyFishCommand command) {
      // TODO Auto-generated method stub
      
   }

   @Override
   public void run(String[] arguments) {
      // TODO Auto-generated method stub
      
   }

}
