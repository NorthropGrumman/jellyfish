package com.ngc.seaside.starfish.bootstrap;

/**
 * Exception to throw when a script would normally exit
 */
public class ExitException extends RuntimeException
{

   private final int code;

   public ExitException()
   {
      this(0);
   }

   public ExitException(String message)
   {
      this(message, 1);
   }

   public ExitException(int code)
   {
      this.code = code;
   }

   public ExitException(String message, int code)
   {
      super(message);
      this.code = code;
   }

   /**
    * Returns whether or not the this exception represents a failed script.
    *
    * @return whether or not the this exception represents a failed script
    */
   public boolean failed()
   {
      return code != 0;
   }

   /**
    * Returns the error code.
    * 
    * @return the error code
    */
   public int getCode()
   {
      return code;
   }

}
