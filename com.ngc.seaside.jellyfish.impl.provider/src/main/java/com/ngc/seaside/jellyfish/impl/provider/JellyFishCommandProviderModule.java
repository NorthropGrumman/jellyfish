package com.ngc.seaside.jellyfish.impl.provider;

import com.google.inject.AbstractModule;
import com.ngc.seaside.command.api.IUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandProvider;

/**
 * Guice wrapper around the {@link JellyFishCommandProvider} implementation.
 *
 * TODO abstract the isReady and Update technique to a reusable software pattern
 */
public class JellyFishCommandProviderModule extends AbstractModule implements IJellyFishCommandProvider {

   @Override
   protected void configure() {
      // TODO Auto-generated method stub
      
   }

   public void run(String[] args) {
      // TODO Auto-generated method stub
      
   }

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


}