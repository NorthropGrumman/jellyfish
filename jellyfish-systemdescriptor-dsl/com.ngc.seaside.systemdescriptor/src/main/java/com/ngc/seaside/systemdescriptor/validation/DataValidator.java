package com.ngc.seaside.systemdescriptor.validation;

import com.google.inject.Inject;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Data;
import com.ngc.seaside.systemdescriptor.systemDescriptor.DataFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;

import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.validation.Check;

import java.util.HashSet;
import java.util.Set;

public class DataValidator extends AbstractUnregisteredSystemDescriptorValidator {

   /**
    * A provider that is used to get the IResourceDescriptions which serves as
    * the main index or cache for Xtext. This is used do reverse lookups to
    * elements using their qualified names.
    */
   @Inject
   private IQualifiedNameProvider nameProvider;
   
	/**
    * Validates that a Data object does not extend itself.
    * 
    * @param data data object to evaluate
    */
	@Check
	public void checkHierarchy(Data data) {
	   Set<QualifiedName> superClasses = new HashSet<>();
	   
	   final QualifiedName name = nameProvider.getFullyQualifiedName(data);
	   Data parent = data.getExtendedDataType();
	   while (parent != null) {
	      QualifiedName parentName = nameProvider.getFullyQualifiedName(parent);
	      if (parentName.equals(name)) {
	        String msg = String.format("Cycle detected: a cycle exists in the type hierarchy of %s", name.getLastSegment());
            error(msg, data, SystemDescriptorPackage.Literals.DATA__EXTENDED_DATA_TYPE);
            return;
	      }
	      if (!superClasses.add(nameProvider.getFullyQualifiedName(parent))) {
	         return;
	      }
	      parent = parent.getExtendedDataType();
	   }
	}
	
	/**
	 * Validates that the user did not try to escape a keyword with ^ in any of 
	 * the data fields or the data name.
	 * 
	 * @param data is the Data object to evaluate
	 */	
	@Check
	public void checkUsageOfEscapeHatCharacter(Data data) {
		// Verify the data name doesn't not have the escape hat
		if (data.getName().indexOf('^') >= 0) {
			String msg = String.format(
					"Cannot use '^' to escape the data name %s.",
					data.getName());
			error(msg, data, SystemDescriptorPackage.Literals.ELEMENT__NAME);
		}
		
		// then loop through the fields and check the typename and the field name
		for (DataFieldDeclaration f : data.getFields()) {
			if (f.getName().indexOf('^') >= 0) {
				// Report error
				String msg = String.format(
						"Cannot use '^' to escape the field named %s in data %s.",
						f.getName(),
						data.getName());
				error(msg, f, SystemDescriptorPackage.Literals.DATA_FIELD_DECLARATION__NAME);
			}
			
		}
	}
}
