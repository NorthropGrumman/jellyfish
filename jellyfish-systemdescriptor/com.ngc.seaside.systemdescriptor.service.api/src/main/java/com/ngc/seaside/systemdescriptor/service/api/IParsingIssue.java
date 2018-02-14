package com.ngc.seaside.systemdescriptor.service.api;

import com.ngc.seaside.systemdescriptor.validation.api.Severity;

import java.nio.file.Path;
import java.util.Optional;

/**
 * An issues that is generated during parsing.  Issues may be syntax or validation related.
 */
public interface IParsingIssue {

   /**
    * Gets the message associated with this issue.
    *
    * @return the message associated with this issue (never {@code null})
    */
   String getMessage();

   /**
    * Gets the severity associated with this issue.
    *
    * @return the severity associated with this issue
    */
   Severity getSeverity();

   /**
    * Gets an optional error code that is associated with this issue.
    */
   Optional<String> getErrorCode();

   /**
    * Gets the line number of the offending resource that caused this issue.
    *
    * @return the line number of the offending resource that caused this issue
    */
   int getLineNumber();

   /**
    * Gets the column number on the line of the offending resource that caused this issue.
    *
    * @return the column number on the line of the offending resource that caused this issue
    */
   int getColumn();

   int getOffset();

   /**
    * Gets the position of the character where the issue is "out of scope".
    *
    * @return the position of the character where the issue is "out of scope"
    */
   int getLength();

   /**
    * Gets a path to the file that is the root of this issue.
    *
    * @return the path to the file that is the root of this issue
    */
   Path getOffendingFile();
}
