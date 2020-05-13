/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
