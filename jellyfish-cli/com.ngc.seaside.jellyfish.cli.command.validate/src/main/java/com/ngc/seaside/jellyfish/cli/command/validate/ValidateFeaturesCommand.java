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
