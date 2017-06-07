package com.ngc.seaside.bootstrap.service.impl.promptuserservice;

import com.ngc.seaside.bootstrap.service.promptuser.api.IPromptUserService;

import org.osgi.service.component.annotations.Component;

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

   @Override
   public String prompt(String parameter, String defaultValue, Predicate<String> validator) {
      if (validator == null) {
         validator = __ -> true;
      }
      Scanner scanner = new Scanner(inputStream);
      final String defaultString = defaultValue == null ? "" : " (" + defaultValue + ")";

      //This will prompt the user again if they enter an invalid value. If they don't enter anything and the
      //default value isn't null then it will just return the default value.
      while (true) {

         System.out.print("Enter value for " + parameter + defaultString + ": ");
         String line = "";

         try {
            line = scanner.nextLine();
         } catch (NoSuchElementException e) {
            //let it try to return the default value otherwise ask again.
         }

         if ((line == null || line.trim().isEmpty())) {
            if(defaultValue != null) {
               return defaultValue;
            }
         } else {
            if(validator.test(line)) {
               return line;
            }
         }
      }
   }

   /**
    * Set the input stream in order to prompt the user. This is set to System.in by default.
    *
    * @param inputStream the new input stream.
    */
   public void setInputStream(InputStream inputStream) {
      this.inputStream = inputStream;
   }
}
