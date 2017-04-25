package com.ngc.seaside.starfish.bootstrap;

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

   public boolean failed()
   {
      return code != 0;
   }

   public int getCode()
   {
      return code;
   }

}
