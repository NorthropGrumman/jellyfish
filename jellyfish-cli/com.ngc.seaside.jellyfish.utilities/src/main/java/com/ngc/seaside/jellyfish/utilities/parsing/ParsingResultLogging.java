package com.ngc.seaside.jellyfish.utilities.parsing;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;

import com.ngc.seaside.systemdescriptor.service.api.IParsingIssue;
import com.ngc.seaside.systemdescriptor.service.api.IParsingResult;
import com.ngc.seaside.systemdescriptor.validation.api.Severity;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Aids in logging the results of parsing a System Descriptor project.
 */
public class ParsingResultLogging {

   private ParsingResultLogging() {
   }

   /**
    * Gets a collection of strings that should be printed one per line that describes the parsing errors in the given
    * result.
    */
   public static Collection<String> logErrors(IParsingResult result) {
      Collection<String> lines = new ArrayList<>();

      Collection<IParsingIssue> errors = result.getIssues()
            .stream()
            .filter(i -> i.getSeverity() == Severity.ERROR)
            .collect(Collectors.toList());
      lines.add(String.format("System Descriptor project contains %d errors.", errors.size()));
      for (IParsingIssue error : errors) {
         lines.addAll(printIssue(error));
      }

      return lines;
   }

   /**
    * Gets a collection of strings that should be printed one per line that describes the parsing warnings in the given
    * result.
    */
   public static Collection<String> logWarnings(IParsingResult result) {
      Collection<String> lines = new ArrayList<>();

      Collection<IParsingIssue> warnings = result.getIssues()
            .stream()
            .filter(i -> i.getSeverity() == Severity.WARNING)
            .collect(Collectors.toList());
      lines.add(String.format("System Descriptor project contains %d warnings.", warnings.size()));
      for (IParsingIssue warning : warnings) {
         lines.addAll(printIssue(warning));
      }

      return lines;
   }

   private static Collection<String> printIssue(IParsingIssue issue) {
      Collection<String> lines = new ArrayList<>();

      Path offendingFile = issue.getOffendingFile();
      lines.add("----------------------------------------");
      lines.add(String.format("File: %s", offendingFile == null ? "unknown"
                                                                : offendingFile.toAbsolutePath()));
      lines.add(String.format("%s: %s", issue.getSeverity(), issue.getMessage()));
      if (offendingFile != null && offendingFile.toFile().isFile()) {
         lines.addAll(printOffendingLine(issue));
      }

      return lines;
   }

   private static Collection<String> printOffendingLine(IParsingIssue issue) {
      Collection<String> lines = new ArrayList<>();

      try {
         String line = Files.asCharSource(issue.getOffendingFile().toFile(),
                                          Charsets.UTF_8)
               .readLines(new LineFinder(issue.getLineNumber()));
         if (line != null) {
            lines.add("");
            lines.add(line);
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < issue.getColumn(); i++) {
               sb.append(' ');
            }
            lines.add(sb.append("^").toString());
         }
      } catch (IOException e) {
         // Do nothing.
      }

      return lines;
   }

   private static class LineFinder implements LineProcessor<String> {

      private final int targetLineNumber;
      private int currentLineNumber = 0;
      private String line = null;

      private LineFinder(int targetLineNumber) {
         this.targetLineNumber = targetLineNumber;
      }

      @Override
      public boolean processLine(String line) {
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
