package com.ngc.seaside.bootstrap;

/**
 * Exception to throw when a script would normally exit
 */
public class ExitException extends RuntimeException {
   private static final long serialVersionUID = -2159928081319923575L;
   private final int code;

   /**
    * Default constructor with default exit code.
    */
   public ExitException() {
      this(0);
   }

   /**
    * Error exception constructor with a message.
    *
    * @param message the exception description
    */
   public ExitException(String message) {
      this(message, 1);
   }

   /**
    * Exception constructor in which the code is provided by the caller.
    *
    * @param code the code
    */
   public ExitException(int code) {
      this.code = code;
   }

   /**
    * Exception constructor in which the message and code is provided by the caller.
    *
    * @param message the message
    * @param code    the code
    */
   public ExitException(String message, int code) {
      super(message);
      this.code = code;
   }

   /**
    * Returns whether or not the this exception represents a failed script.
    *
    * @return whether or not the this exception represents a failed script
    */
   public boolean failed() {
      return code != 0;
   }

   /**
    * Returns the error code.
    *
    * @return the error code
    */
   public int getCode() {
      return code;
   }

}
