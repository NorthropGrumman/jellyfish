package com.ngc.seaside.systemdescriptor.model.api;

public interface INamedChild<T> {

  String getName();

  T getParent();
}
