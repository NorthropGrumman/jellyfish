package com.ngc.seaside.systemdescriptor.validation;

import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.validation.Check;

import com.google.inject.Inject;
import com.ngc.seaside.systemdescriptor.systemDescriptor.EnumPropertyValue;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;

public class PropertyValueValidator extends AbstractUnregisteredSystemDescriptorValidator {

	@Inject
	private IQualifiedNameProvider nameProvider;
	
	@Check
	public void checkEnumValueIsValidConstant(EnumPropertyValue value) {
		boolean valid = value.getEnumeration().getValues()
				.stream()
				.anyMatch(d -> d.getValue().equals(value.getValue()));
		if (!valid) {
			String msg = String.format(
					"The enumeration '%s' contains no constant named '%s'.",
					nameProvider.getFullyQualifiedName(value.getEnumeration()),
					value.getValue());
			error(msg, value, SystemDescriptorPackage.Literals.ENUM_PROPERTY_VALUE__VALUE);
		}
	}

}
