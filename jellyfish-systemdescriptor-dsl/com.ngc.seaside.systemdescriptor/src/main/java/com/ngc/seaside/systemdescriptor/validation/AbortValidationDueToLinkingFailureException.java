package com.ngc.seaside.systemdescriptor.validation;

/**
 * A type of exception that can be thrown by a validator to indicate it should stop
 * validating because the objects are already invalid due to a linking or cross
 * referencing error.  This is usually the case when an unresolvable proxy object
 * is encountered.
 */
public class AbortValidationDueToLinkingFailureException extends RuntimeException {
}
