package com.ngc.seaside.systemdescriptor.validation;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.xtext.validation.Check;
import org.eclipse.xtext.validation.ComposedChecks;

import com.google.common.base.Preconditions;
import com.ngc.seaside.systemdescriptor.extension.IValidatorExtension;

/**
 * This is the main validator for the System Descriptor language. This validator
 * does not actually do any work; it delegates to more specific validators
 * declared in the {@code ComposedChecks} annotation as well as validator extensions
 * that have been registered.
 */
@ComposedChecks(validators = {
		DuplicateElementValidator.class,
		ModelValidator.class,
		ScenarioValidator.class,
		LinkValidator.class}
)
public class SystemDescriptorValidator extends AbstractSystemDescriptorValidator
	implements IValidatorExtension.ValidationHelper {

	private final Collection<IValidatorExtension> validators = new ArrayList<>();
	
	@Check
	public void invokeValidatorExtensions(EObject object) {
		for(IValidatorExtension v : validators) {
			v.validate(object, this);
		}
	}

	public void addValidatorExtension(IValidatorExtension validator) {
		validators.add(Preconditions.checkNotNull(validator, "validator may not be null!"));
	}
	
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
}
