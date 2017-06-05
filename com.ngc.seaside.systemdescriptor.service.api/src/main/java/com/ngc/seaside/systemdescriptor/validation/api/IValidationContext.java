package com.ngc.seaside.systemdescriptor.validation.api;

public interface IValidationContext<T> {

   T getObject();

   T declare(Severity severity, String message, T object);
}
