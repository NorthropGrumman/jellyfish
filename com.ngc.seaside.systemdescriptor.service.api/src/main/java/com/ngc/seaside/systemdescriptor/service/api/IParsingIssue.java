package com.ngc.seaside.systemdescriptor.service.api;

import java.io.BufferedReader;
import java.util.Optional;

/**
 * An issues that is generated during parsing.  Issues may be syntax or validation related.
 */
public interface IParsingIssue {

  /**
   * The various serenity levels associated with issues.
   */
  enum Severity {
    /**
     * The highest serenity level; indicates an fatal error and the descriptor is invalid.
     */
    ERROR,
    /**
     * Indicates a potential problem but the descriptor is valid.
     */
    WARNING,
    /**
     * Indicates a suggested fix to a valid descriptor.
     */
    SUGGESTION
  }

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
   * Gets the resource that caused this issue.
   *
   * @return the resource that caused this issue
   */
  BufferedReader getOffendingResouce();

  // TODO TH: add location to file (ie, the file name is needed).
}
