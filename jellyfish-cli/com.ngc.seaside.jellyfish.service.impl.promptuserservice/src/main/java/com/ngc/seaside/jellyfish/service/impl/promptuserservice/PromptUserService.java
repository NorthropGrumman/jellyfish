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

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.service.promptuser.api.IPromptUserService;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.function.Predicate;

/**
 * @author justan.provence@ngc.com
 */
@Component(service = IPromptUserService.class)
public class PromptUserService implements IPromptUserService {

   private InputStream inputStream = System.in;
   private ILogService logService;

   @Activate
   public void activate() {
      logService.trace(getClass(), "activated");
   }

   @Deactivate
   public void deactivate() {
      logService.trace(getClass(), "deactivated");
   }

   /**
    * Sets log service.
    *
    * @param ref the ref
    */
   @Reference(cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.STATIC,
            unbind = "removeLogService")
   public void setLogService(ILogService ref) {
      this.logService = ref;
   }

   /**
    * Remove log service.
    */
   public void removeLogService(ILogService ref) {
      setLogService(null);
   }

   /**
    * Set the input stream in order to prompt the user. This is set to System.in by default.
    *
    * @param inputStream the new input stream.
    */
   public void setInputStream(InputStream inputStream) {
      this.inputStream = inputStream;
   }

   @Override
   public String prompt(String parameter, String defaultValue, Predicate<String> validator) {
      final String defaultString = defaultValue == null ? "" : " (" + defaultValue + ")";
      String prompt = "Enter value for " + parameter + defaultString + ": ";
      return this.prompter(prompt, defaultValue, validator);
   }

   @Override
   public String promptDataEntry(String question, String note, String defaultValue, Predicate<String> validator) {
      String prompt = question + " " + note + ": ";
      return this.prompter(prompt, defaultValue, validator);
   }

   private String prompter(String prompt, String defaultValue, Predicate<String> validator) {
      Predicate<String> inputValidator = validator;
      if (validator == null) {
         inputValidator = __ -> true;
      }
      @SuppressWarnings("resource")
      Scanner scanner = new Scanner(inputStream);

      //This will prompt the user again if they enter an invalid value. If they don't enter anything and the
      //default value isn't null then it will just return the default value.
      String returnStr = null;
      while (returnStr == null) {

         System.out.print(prompt);
         String line = "";

         try {
            line = scanner.nextLine();
         } catch (NoSuchElementException e) {
            //let it try to return the default value otherwise ask again.
         }

         if ((line == null || line.trim().isEmpty())) {
            if (defaultValue != null) {
               returnStr = defaultValue;
            }
         } else {
            if (inputValidator.test(line)) {
               returnStr = line;
            }
         }
      }

      return returnStr;
   }

}
