package com.ngc.seaside.systemdescriptor.validation;

import org.eclipse.xtext.validation.ComposedChecks;

/**
 * This is the main validator for the System Descriptor language. This validator
 * does not actually do any work; it delegates to more specific validators
 * declared in the {@code ComposedChecks} annotation.
 */
@ComposedChecks(validators = {
		DuplicateElementValidator.class,
		ModelValidator.class,
		ScenarioValidator.class,
		LinkValidator.class}
)
public class SystemDescriptorValidator extends AbstractSystemDescriptorValidator {

}
