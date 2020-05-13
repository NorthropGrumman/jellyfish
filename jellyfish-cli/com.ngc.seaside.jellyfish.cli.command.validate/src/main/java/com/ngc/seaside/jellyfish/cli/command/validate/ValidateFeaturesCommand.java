/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.ngc.seaside.jellyfish.cli.command.validate;

import com.google.inject.Inject;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.CommandException;
import com.ngc.seaside.jellyfish.api.DefaultUsage;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.utilities.command.AbstractJellyfishCommand;
import com.ngc.seaside.jellyfish.utilities.parsing.ParsingResultLogging;
import com.ngc.seaside.systemdescriptor.service.gherkin.api.IGherkinParsingResult;

/**
 * Validates that feature files associated with a project are valid.
 */
public class ValidateFeaturesCommand extends AbstractJellyfishCommand {

   /**
    * The name of the command.
    */
   public static final String NAME = "validate-features";

   /**
    * Creates a new command.
    * 
    * @param logService used to log issues
    */
   @Inject
   public ValidateFeaturesCommand(ILogService logService) {
      super(NAME);
      setLogService(logService);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected IUsage createUsage() {
      return new DefaultUsage("Validates the feature files assoicated with a project are formatted correctly.");
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected void doRun() {
      IGherkinParsingResult result = getOptions().getGherkinParsingResult();
      if (result.isSuccessful()) {
         logService.info(getClass(), "Feature files are valid.");
      } else {
         // The formatting of the logging line with "%s" avoids issues if the line in the feature file contains a
         // "%s" symbol.
         ParsingResultLogging.logErrors(result).forEach(l -> logService.error(getClass(), "%s", l));
         ParsingResultLogging.logWarnings(result).forEach(l -> logService.warn(getClass(), "%s", l));
         throw new CommandException("feature files failed validation!");
      }
   }
}
