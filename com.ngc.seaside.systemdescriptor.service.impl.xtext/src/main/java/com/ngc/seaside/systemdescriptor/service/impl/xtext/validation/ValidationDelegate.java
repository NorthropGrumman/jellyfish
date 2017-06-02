package com.ngc.seaside.systemdescriptor.service.impl.xtext.validation;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;

import com.ngc.seaside.systemdescriptor.validation.SystemDescriptorValidator;

public class ValidationDelegate {

   private final SystemDescriptorValidator validator;

   @Inject
   public ValidationDelegate(SystemDescriptorValidator validator) {
      this.validator =     Preconditions.checkNotNull(validator, "validator may not be null!");
   }
}
