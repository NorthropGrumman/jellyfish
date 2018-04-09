package com.ngc.seaside.systemdescriptor.validation;

import org.eclipse.xtext.validation.ComposedChecks;
import org.eclipse.xtext.validation.EValidatorRegistrar;

/**
 * Abstract class for system descriptor validators that don't register themselves because they
 * are included in a {@link ComposedChecks} annotation. This prevents errors for being reported twice.
 */
public class AbstractUnregisteredSystemDescriptorValidator extends AbstractSystemDescriptorValidator {

   @Override
   public void register(EValidatorRegistrar registrar) {
   }

}
