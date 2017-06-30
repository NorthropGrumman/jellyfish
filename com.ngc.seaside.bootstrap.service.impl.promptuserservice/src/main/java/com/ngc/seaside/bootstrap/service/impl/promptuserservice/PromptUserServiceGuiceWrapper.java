package com.ngc.seaside.bootstrap.service.impl.promptuserservice;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.bootstrap.service.promptuser.api.IPromptUserService;

import java.util.function.Predicate;

/**
 * Wrap the service using Guice Injection
 */
@Singleton
public class PromptUserServiceGuiceWrapper implements IPromptUserService {

   private final PromptUserService delegate = new PromptUserService();

   @Inject
   public PromptUserServiceGuiceWrapper(ILogService logService) {
      delegate.setLogService(logService);
      delegate.activate();
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
