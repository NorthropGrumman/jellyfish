package com.ngc.seaside.systemdescriptor.model.impl.xtext.exception;

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
