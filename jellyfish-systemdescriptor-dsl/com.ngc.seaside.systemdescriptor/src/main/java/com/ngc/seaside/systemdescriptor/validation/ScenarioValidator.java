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

import com.ngc.seaside.systemdescriptor.systemDescriptor.Scenario;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;

import org.eclipse.xtext.validation.Check;

public class ScenarioValidator extends AbstractUnregisteredSystemDescriptorValidator {


   /**
    * Validates that the user did not try to escape a keyword with ^ in the
    * name of the model.
    *
    * @param scenario is the scenario to evaluate
    */
   @Check
   public void checkUsageOfEscapeHatCharacter(Scenario scenario) {
      // Verify the data name doesn't not have the escape hat
      if (scenario.getName().indexOf('^') >= 0) {
         String msg = String.format(
               "Cannot use '^' to escape the scenario name %s.",
               scenario.getName());
         error(msg, scenario, SystemDescriptorPackage.Literals.SCENARIO__NAME);
      }

   }

}
