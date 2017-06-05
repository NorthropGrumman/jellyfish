package com.ngc.seaside.systemdescriptor.service.impl.xtext.validation;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;

import com.ngc.seaside.systemdescriptor.extension.IValidatorExtension;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.systemDescriptor.DataFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;
import com.ngc.seaside.systemdescriptor.validation.SystemDescriptorValidator;
import com.ngc.seaside.systemdescriptor.validation.api.ISystemDescriptorValidator;

import org.eclipse.emf.ecore.EObject;

import java.util.ArrayList;
import java.util.Collection;

public class ValidationDelegate {

   private final Collection<ISystemDescriptorValidator> validators = new ArrayList<>();
   private final SystemDescriptorValidator validator;

   @Inject
   public ValidationDelegate(SystemDescriptorValidator validator) {
      this.validator = Preconditions.checkNotNull(validator, "validator may not be null!");
   }

   public void addValidator(ISystemDescriptorValidator validator) {
      validators.add(Preconditions.checkNotNull(validator, "validator may not be null!"));

      this.validator.addValidatorExtension(new IValidatorExtension() {
         @Override
         public void validate(EObject source, ValidationHelper helper) {
//            if(source instanceof DataFieldDeclaration) {
//               DataFieldDeclaration f = (DataFieldDeclaration) source;
//               if(f.getName().equals("second")) {
//                  helper.error("blah blah", source, SystemDescriptorPackage.Literals.DATA_FIELD_DECLARATION__NAME);
//               }
//            }
         }
      });
   }

   public boolean removeValidator(ISystemDescriptorValidator validator) {
      return validators.remove(Preconditions.checkNotNull(validator, "validator may not be null!"));
   }
}
