package com.ngc.seaside.jellyfish.cli.command.validate;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.command.api.CommandException;
import com.ngc.seaside.command.api.DefaultUsage;
import com.ngc.seaside.command.api.IUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.systemdescriptor.service.api.IParsingIssue;
import com.ngc.seaside.systemdescriptor.service.api.IParsingResult;
import com.ngc.seaside.systemdescriptor.validation.api.Severity;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * This class provides the implementation of the validate command.
 */
@Component(service = IJellyFishCommand.class)
public class ValidateCommand implements IJellyFishCommand {

   private static final String NAME = "validate";
   private static final IUsage
         COMMAND_USAGE =
         new DefaultUsage("Validates the System Descriptor. Requires a system descriptor project within src/main/sd");

   private ILogService logService;

   @Activate
   public void activate() {
      logService.trace(getClass(), "Activated");
   }

   @Deactivate
   public void deactivate() {
      logService.trace(getClass(), "Deactivated");
   }

   /**
    * Sets log service.
    *
    * @param ref the ref
    */
   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "removeLogService")
   public void setLogService(ILogService ref) {
      this.logService = ref;
   }

   /**
    * Remove log service.
    */
   public void removeLogService(ILogService ref) {
      setLogService(null);
   }

   @Override
   public String getName() {
      return NAME;
   }

   @Override
   public IUsage getUsage() {
      return COMMAND_USAGE;
   }

   @Override
   public void run(IJellyFishCommandOptions commandOptions) {
      IParsingResult result = commandOptions.getParsingResult();
      if (commandOptions.getParsingResult().isSuccessful()) {
         logService.info(ValidateCommand.class, "System Descriptor project is valid.");
      } else {
         logErrors(result);
         logWarnings(result);
         throw new CommandException("System Descriptor failed validation!");
      }
   }

   @Override
   public String toString() {
      return getName();
   }

   private void logErrors(IParsingResult result) {
      Collection<IParsingIssue> errors = result.getIssues()
            .stream()
            .filter(i -> i.getSeverity() == Severity.ERROR)
            .collect(Collectors.toList());
      logService.error(ValidateCommand.class, "System Descriptor project contains %d errors.", errors.size());
      errors.forEach(this::printIssueError);
   }

   private void logWarnings(IParsingResult result) {
      Collection<IParsingIssue> warnings = result.getIssues()
            .stream()
            .filter(i -> i.getSeverity() == Severity.WARNING)
            .collect(Collectors.toList());
      logService.warn(ValidateCommand.class, "System Descriptor project contains %d warnings.", warnings.size());
      warnings.forEach(this::printIssueWarning);
   }

   private void printIssueError(IParsingIssue issue) {
      Path offendingFile = issue.getOffendingFile();
      logService.error(ValidateCommand.class, "----------------------------------------");
      logService.error(ValidateCommand.class, "File: %s", offendingFile == null ? "unknown"
                                                                                : offendingFile.toAbsolutePath());
      logService.error(ValidateCommand.class, "%s: %s", issue.getSeverity(), issue.getMessage());
      if (offendingFile != null && offendingFile.toFile().isFile()) {
         printOffendingLineError(issue);
      }
   }

   private void printIssueWarning(IParsingIssue issue) {
      Path offendingFile = issue.getOffendingFile();
      logService.warn(ValidateCommand.class, "----------------------------------------");
      logService.warn(ValidateCommand.class, "File: %s", offendingFile == null ? "unknown"
                                                                               : offendingFile.toAbsolutePath());
      logService.warn(ValidateCommand.class, "%s: %s", issue.getSeverity(), issue.getMessage());
      if (offendingFile != null && offendingFile.toFile().isFile()) {
         printOffendingLineWarning(issue);
      }
   }

   private void printOffendingLineError(IParsingIssue issue) {
      try {
         String line = Files.readLines(issue.getOffendingFile().toFile(),
                                       Charsets.UTF_8,
                                       new LineFinder(issue.getLineNumber()));
         if (line != null) {
            logService.error(ValidateCommand.class, "");
            // Note the "%s" format string ensures there will not be logging errors if the line in the file contains
            // a format string.
            logService.error(ValidateCommand.class, "%s", line);
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < issue.getColumn(); i++) {
               sb.append(' ');
            }
            logService.error(ValidateCommand.class, sb.append("^"));
         }
      } catch (IOException e) {
         // Do nothing.
      }
   }

   private void printOffendingLineWarning(IParsingIssue issue) {
      try {
         String line = Files.readLines(issue.getOffendingFile().toFile(),
                                       Charsets.UTF_8,
                                       new LineFinder(issue.getLineNumber()));
         if (line != null) {
            logService.warn(ValidateCommand.class, "");
            // Note the "%s" format string ensures there will not be logging errors if the line in the file contains
            // a format string.
            logService.warn(ValidateCommand.class, "%s", line);
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < issue.getColumn(); i++) {
               sb.append(' ');
            }
            logService.warn(ValidateCommand.class, sb.append("^"));
         }
      } catch (IOException e) {
         // Do nothing.
      }
   }

   private static class LineFinder implements LineProcessor<String> {

      private final int targetLineNumber;
      private int currentLineNumber = 0;
      private String line = null;

      public LineFinder(int targetLineNumber) {
         this.targetLineNumber = targetLineNumber;
      }

      @Override
      public boolean processLine(String line) throws IOException {
         currentLineNumber++;
         boolean isTargetLine = currentLineNumber == targetLineNumber;
         if (isTargetLine) {
            this.line = line;
         }
         return !isTargetLine;
      }

      @Override
      public String getResult() {
         return line;
      }
   }
}
