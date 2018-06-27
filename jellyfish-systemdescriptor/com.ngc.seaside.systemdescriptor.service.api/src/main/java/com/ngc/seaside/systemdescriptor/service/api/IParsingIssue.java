package com.ngc.seaside.systemdescriptor.service.api;

import com.ngc.seaside.systemdescriptor.service.source.api.ISourceLocation;
import com.ngc.seaside.systemdescriptor.validation.api.Severity;

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
    * Gets the source location to the root cause of this issue.
    * 
    * @return the source location to the root cause of this issue
    */
   ISourceLocation getLocation();

}
