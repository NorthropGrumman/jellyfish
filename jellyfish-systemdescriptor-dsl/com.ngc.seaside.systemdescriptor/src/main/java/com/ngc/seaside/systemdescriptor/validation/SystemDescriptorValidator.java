/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
 */
package com.ngc.seaside.systemdescriptor.validation;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.extension.IValidatorExtension;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.xtext.validation.Check;
import org.eclipse.xtext.validation.ComposedChecks;

import java.util.ArrayList;
import java.util.Collection;

/**
 * This is the main validator for the System Descriptor language. This validator
 * does not actually do any work; it delegates to more specific validators
 * declared in the {@code ComposedChecks} annotation as well as validator
 * extensions that have been registered.
 */
@ComposedChecks(validators = {
      ImportValidator.class,
      DuplicateElementValidator.class,
      ModelValidator.class,
      RefinedModelValidator.class,
      ScenarioValidator.class,
      LinkValidator.class,
      DataValidator.class,
      PackageValidator.class,
      PartsValidator.class,
      RefinedLinkValidator.class,
      RequiresValidator.class,
      PropertiesValidator.class,
      PropertyValueValidator.class,
      UnsetPropertiesValidator.class})
public class SystemDescriptorValidator extends AbstractSystemDescriptorValidator
      implements IValidatorExtension.ValidationHelper {

   /**
    * Registered validator extensions.
    */
   private final Collection<IValidatorExtension> validators = new ArrayList<>();

   /**
    * Invokes validators registered at runtime.
    */
   @Check
   public void invokeValidatorExtensions(EObject object) {
      for (IValidatorExtension v : validators) {
         v.validate(object, this);
      }
   }

   /**
    * Add a validator extension that will be invoke to perform validation as a
    * system descriptor is being parsed.
    *
    * @param validator the validator to add
    */
   public void addValidatorExtension(IValidatorExtension validator) {
      validators.add(Preconditions.checkNotNull(validator, "validator may not be null!"));
   }

   /**
    * Removes a validator extension.
    *
    * @param validator the validator to remove
    * @return true if the validator was removed, false if the validator was not
    * added
    */
   public boolean removeValidatorExtension(IValidatorExtension validator) {
      return validators.remove(Preconditions.checkNotNull(validator, "validator may not be null!"));
   }

   @Override
   public void error(String message, EObject source, EStructuralFeature feature) {
      super.error(message, source, feature);
   }

   @Override
   public void warning(String message, EObject source, EStructuralFeature feature) {
      super.warning(message, source, feature);
   }

   @Override
   public void info(String message, EObject source, EStructuralFeature feature) {
      super.info(message, source, feature);
   }

   @Override
   protected void checkIsFromCurrentlyCheckedResource(EObject object) {
      // This was overriden so that the budget property validator could declare errors on
      // properties of models that were parts of the original model being validated.
   }

}
