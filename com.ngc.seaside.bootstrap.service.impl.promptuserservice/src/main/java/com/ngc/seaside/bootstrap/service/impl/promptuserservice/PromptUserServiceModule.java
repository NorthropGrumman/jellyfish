package com.ngc.seaside.bootstrap.service.impl.promptuserservice;

import com.google.inject.AbstractModule;

import com.ngc.seaside.bootstrap.service.promptuser.api.IPromptUserService;

/**
 * @author justan.provence@ngc.com
 */
public class PromptUserServiceModule extends AbstractModule {
   @Override
   protected void configure() {
      bind(IPromptUserService.class).to(PromptUserService.class);
   }
}
