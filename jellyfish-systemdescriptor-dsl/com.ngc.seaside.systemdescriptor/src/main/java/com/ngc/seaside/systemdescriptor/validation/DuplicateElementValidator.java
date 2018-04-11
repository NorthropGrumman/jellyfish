package com.ngc.seaside.systemdescriptor.validation;

import com.ngc.seaside.systemdescriptor.systemDescriptor.Data;

import org.eclipse.xtext.validation.Check;

public class DuplicateElementValidator extends AbstractUnregisteredSystemDescriptorValidator {

   @Check
   public void checkForDuplicateDataDeclarations(Data data) {
      // TODO TH: implement this later.
   }
}
