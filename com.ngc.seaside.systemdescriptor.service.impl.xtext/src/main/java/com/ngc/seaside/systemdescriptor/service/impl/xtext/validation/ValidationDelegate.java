package com.ngc.seaside.systemdescriptor.service.impl.xtext.validation;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;

import com.ngc.seaside.systemdescriptor.extension.IValidatorExtension;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;
import com.ngc.seaside.systemdescriptor.validation.SystemDescriptorValidator;
import com.ngc.seaside.systemdescriptor.validation.api.ISystemDescriptorValidator;
import com.ngc.seaside.systemdescriptor.validation.api.IValidationContext;

import org.eclipse.emf.ecore.EObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class ValidationDelegate implements IValidatorExtension {

   private final Collection<ISystemDescriptorValidator> validators = Collections.synchronizedList(new ArrayList<>());
   private final SystemDescriptorValidator validator;

   @Inject
   public ValidationDelegate(SystemDescriptorValidator validator) {
      this.validator = Preconditions.checkNotNull(validator, "validator may not be null!");

   }

   @Override
   public void validate(EObject source, ValidationHelper helper) {
      // Figure out how we need to wrap this object.
      switch (source.eClass().getClassifierID()) {
         case SystemDescriptorPackage.PACKAGE:
         default:
            // Do nothing, this is not a type we want to validate.
      }
   }

   public void addValidator(ISystemDescriptorValidator validator) {
      validators.add(Preconditions.checkNotNull(validator, "validator may not be null!"));

//      this.validator.addValidatorExtension(new IValidatorExtension() {
//         @Override
//         public void validate(EObject source, ValidationHelper helper) {
////            if(source instanceof DataFieldDeclaration) {
////               DataFieldDeclaration f = (DataFieldDeclaration) source;
////               if(f.getName().equals("second")) {
////                  helper.error("blah blah", source, SystemDescriptorPackage.Literals.DATA_FIELD_DECLARATION__NAME);
////               }
////            }
//         }
//      });
   }

   public boolean removeValidator(ISystemDescriptorValidator validator) {
      return validators.remove(Preconditions.checkNotNull(validator, "validator may not be null!"));
   }

   protected <T> IValidationContext<T> newContext(T object, ValidationHelper helper) {
      return new ProxyingValidationContext<>(object, helper);
   }

   protected void doValidation(IValidationContext<?> context) {
      synchronized (validators) {
         for (ISystemDescriptorValidator validator : validators) {
            validator.validate(context);
         }
      }
   }

   //private static safely invoke
}
