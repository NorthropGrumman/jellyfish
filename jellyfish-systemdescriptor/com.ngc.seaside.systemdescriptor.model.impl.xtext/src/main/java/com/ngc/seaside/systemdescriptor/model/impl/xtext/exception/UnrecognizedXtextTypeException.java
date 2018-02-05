package com.ngc.seaside.systemdescriptor.model.impl.xtext.exception;

/**
 * A type of exception that indicates the adapting code doesn't recognize some XText type.  These type of errors usually
 * indicate that the packaged adapters and the XText DSL are not in sync or one is out of date.
 */
public class UnrecognizedXtextTypeException extends RuntimeException {

   private final Object xtextType;

   public UnrecognizedXtextTypeException(Object xtextType) {
      super(String.format("unrecognized XText type: %s", xtextType.getClass().getName()));
      this.xtextType = xtextType;
   }

   public Object getXtextType() {
      return xtextType;
   }
}
