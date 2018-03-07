package com.ngc.seaside.systemdescriptor.validation;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.validation.Check;

import com.google.inject.Inject;
import com.ngc.seaside.systemdescriptor.systemDescriptor.BooleanValue;
import com.ngc.seaside.systemdescriptor.systemDescriptor.DblValue;
import com.ngc.seaside.systemdescriptor.systemDescriptor.EnumPropertyValue;
import com.ngc.seaside.systemdescriptor.systemDescriptor.IntValue;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PrimitiveDataType;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PrimitivePropertyFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PropertyFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PropertyValue;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PropertyValueAssignment;
import com.ngc.seaside.systemdescriptor.systemDescriptor.ReferencedPropertyFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.StringValue;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;;

public class PropertyValueValidator extends AbstractUnregisteredSystemDescriptorValidator {

	@Inject
	private IQualifiedNameProvider nameProvider;

	@Check
	public void checkPrimitivePropertyValue(PropertyValue value) {
		PropertyFieldDeclaration declaration = ((PropertyValueAssignment) value.eContainer()).getDeclaration();

		switch (value.eClass().getClassifierID()) {
		case SystemDescriptorPackage.INT_VALUE:
			checkIntMatchesPropertyType(declaration, (IntValue) value);
			break;
		case SystemDescriptorPackage.DBL_VALUE:
			checkFloatMatchesPropertyType(declaration, (DblValue) value);
			break;
		case SystemDescriptorPackage.BOOLEAN_VALUE:
			checkBooleanMatchesPropertyType(declaration, (BooleanValue) value);
			break;
		case SystemDescriptorPackage.STRING_VALUE:
			checkStringMatchesPropertyType(declaration, (StringValue) value);
			break;
		case SystemDescriptorPackage.ENUM_PROPERTY_VALUE:
			checkEnumValueTypeMatchesPropertyType(declaration, (EnumPropertyValue) value);
			checkEnumValueIsValidConstant((EnumPropertyValue) value);
			break;
		}
	}

	private void checkIntMatchesPropertyType(PropertyFieldDeclaration declaration, IntValue value) {
		Object propertyType = getPropertyType(declaration);
		if (!propertyType.equals(PrimitiveDataType.INT_VALUE)) {
			declareInvalidPropertyTypeError(value,
					declaration,
					propertyType,
					SystemDescriptorPackage.Literals.INT_VALUE__VALUE);
		}
	}

	private void checkFloatMatchesPropertyType(PropertyFieldDeclaration declaration, DblValue value) {
		Object propertyType = getPropertyType(declaration);
		if (!propertyType.equals(PrimitiveDataType.FLOAT_VALUE)) {
			declareInvalidPropertyTypeError(value,
					declaration,
					propertyType,
					SystemDescriptorPackage.Literals.DBL_VALUE__VALUE);
		}
	}

	private void checkBooleanMatchesPropertyType(PropertyFieldDeclaration declaration, BooleanValue value) {
		Object propertyType = getPropertyType(declaration);
		if (!propertyType.equals(PrimitiveDataType.BOOLEAN)) {
			declareInvalidPropertyTypeError(value,
					declaration,
					propertyType,
					SystemDescriptorPackage.Literals.BOOLEAN_VALUE__VALUE);
		}
	}

	private void checkStringMatchesPropertyType(PropertyFieldDeclaration declaration, StringValue value) {
		Object propertyType = getPropertyType(declaration);
		if (!propertyType.equals(PrimitiveDataType.STRING)) {
			declareInvalidPropertyTypeError(value,
					declaration,
					propertyType,
					SystemDescriptorPackage.Literals.STRING_VALUE__VALUE);
		}
	}

	private void checkEnumValueTypeMatchesPropertyType(PropertyFieldDeclaration declaration, EnumPropertyValue value) {
		Object propertyType = getPropertyType(declaration);
		if (!propertyType.equals(value.getEnumeration())) {
			declareInvalidPropertyTypeError(
					value,
					declaration,
					propertyType,
					SystemDescriptorPackage.Literals.ENUM_PROPERTY_VALUE__VALUE);
		}
	}

	private void checkEnumValueIsValidConstant(EnumPropertyValue value) {
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

	private void declareInvalidPropertyTypeError(
			PropertyValue value,
			PropertyFieldDeclaration declaration,
			Object propertyType,
			EStructuralFeature feature) {
		String propertyTypeName = propertyType instanceof Enum
				? propertyType.toString()
				: nameProvider.getFullyQualifiedName((EObject) propertyType).toString();
		String msg = String.format(
				"Expected a value of type '%s' for the property '%s'.",
				propertyTypeName,
				declaration.getName());
		error(msg, value, feature);
	}

	private static Object getPropertyType(PropertyFieldDeclaration declaration) {
		Object type = null;
		switch (declaration.eClass().getClassifierID()) {
		case SystemDescriptorPackage.PRIMITIVE_PROPERTY_FIELD_DECLARATION:
			type = ((PrimitivePropertyFieldDeclaration) declaration).getType();
			break;
		case SystemDescriptorPackage.REFERENCED_PROPERTY_FIELD_DECLARATION:
			type = ((ReferencedPropertyFieldDeclaration) declaration).getDataModel();
			break;
		default:
			throw new IllegalStateException(
					"update this method to support the new property declaration " + declaration);
		}
		return type;
	}

}
