package com.ngc.seaside.bootstrap.service.impl.promptuserservice;

import com.ngc.seaside.bootstrap.service.promptuser.api.IPromptUserService;

import org.osgi.service.component.annotations.Component;

import java.util.function.Predicate;

/**
 * @author justan.provence@ngc.com
 */
@Component(service = IPromptUserService.class)
public class PromptUserService implements IPromptUserService {

   private final PromptUserServiceDelegate delegate = new PromptUserServiceDelegate();

   @Override
   public String prompt(String parameter, String defaultValue, Predicate<String> validator) {
      return delegate.prompt(parameter, defaultValue, validator);
   }
}
