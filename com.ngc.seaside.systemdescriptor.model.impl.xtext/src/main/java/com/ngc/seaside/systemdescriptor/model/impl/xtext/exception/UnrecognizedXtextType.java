package com.ngc.seaside.systemdescriptor.model.impl.xtext.exception;

public class UnrecognizedXtextType extends RuntimeException {

  private final Object xtextType;

  public UnrecognizedXtextType(Object xtextType) {
    super(String.format("unrecognized XText type: %s", xtextType.getClass().getName()));
    this.xtextType = xtextType;
  }

  public Object getXtextType() {
    return xtextType;
  }
}
