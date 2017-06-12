package com.ngc.seaside.bootstrap.service.impl.promptuserservice;

import com.google.inject.AbstractModule;

import com.ngc.seaside.bootstrap.service.promptuser.api.IPromptUserService;

import java.util.function.Predicate;

/**
 * @author justan.provence@ngc.com
 */
public class PromptUserServiceModule extends AbstractModule implements IPromptUserService {

   private final PromptUserService delegate = new PromptUserService();

   @Override
   protected void configure() {
      bind(IPromptUserService.class).toInstance(this);
   }

   @Override
   public String prompt(String parameter, String defaultValue, Predicate<String> validator) {
      return delegate.prompt(parameter, defaultValue, validator);
   }

   @Override
   public String promptDataEntry(String question, String note, String defaultValue, Predicate<String> validator) {
      return delegate.promptDataEntry(question, note, defaultValue, validator);
   }
}
