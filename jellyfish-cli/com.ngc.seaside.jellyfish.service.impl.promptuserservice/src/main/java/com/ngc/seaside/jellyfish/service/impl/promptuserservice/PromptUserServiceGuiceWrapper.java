/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
 */
package com.ngc.seaside.jellyfish.service.impl.promptuserservice;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.service.promptuser.api.IPromptUserService;

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
