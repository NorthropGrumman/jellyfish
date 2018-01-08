package com.ngc.seaside.systemdescriptor.model.impl.xtext.exception;

/**
 * A type of exception that indicates a bridge type could not be converted to an XText type.
 */
public class UnconvertableTypeException extends RuntimeException {

   private final Object object;

   public UnconvertableTypeException(Object object) {
      super(String.format("unable to convert instance of %s to an XText type!", object));
      this.object = object;
   }

   public Object getObject() {
      return object;
   }
}
