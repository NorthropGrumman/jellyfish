package com.ngc.seaside.bootstrap.service.impl.promptuserservice;

import com.ngc.seaside.bootstrap.service.promptuser.api.IPromptUserService;
import com.ngc.seaside.bootstrap.service.promptuser.api.PromptUserServiceException;

import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.function.Predicate;

/**
 * @author justan.provence@ngc.com
 */
public class PromptUserServiceDelegate implements IPromptUserService {

   private Scanner scanner = new Scanner(System.in);

   @Override
   public String prompt(String parameter, String defaultValue, Predicate<String> validator) {
      if (validator == null) {
         validator = __ -> true;
      }
      final String defaultString = defaultValue == null ? "" : " (" + defaultValue + ")";
      while (true) {
         System.out.print("Enter value for " + parameter + defaultString + ": ");
         String line;
         try {
            line = scanner.nextLine();
         } catch (NoSuchElementException e) {
            throw new PromptUserServiceException("Unable to get value for parameter " + parameter);
         }
         if (line.isEmpty() && defaultValue != null) {
            return defaultValue;
         }
         if (validator.test(line)) {
            return line;
         } else {
            System.out.println("Invalid value, please try again");
         }
      }
   }
}
