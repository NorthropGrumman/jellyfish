/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE: All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
 */
package com.ngc.seaside.systemdescriptor.extension;

import com.ngc.seaside.systemdescriptor.systemDescriptor.Step;

import java.util.Set;

/**
 * A plugin interface to add custom scenario step code completion at runtime.
 */
public interface IScenarioStepCompletionExtension {

   /**
    * Returns the list of possible keywords that can be used for the step, or an empty list if there are no possible
    * keywords.
    * 
    * @param step step currently being implemented
    * @param partialKeyword a non-null, potentially empty, keyword prefix
    * @return a list of possible keywords that can be used for the step
    */
   Set<String> completeKeyword(Step step, String partialKeyword);

   /**
    * Returns the list of possible values that can be used to complete the parameter at the given index, or an empty
    * list if there are no possible keywords.
    * 
    * @param step step currently being implemented
    * @param parameterIndex 0-based parameter of the step
    * @param insert if {@code true}, a new parameter is being inserted at the given index; otherwise, the parameter
    *           already at the given index should be completed
    * @return the list of possible parameter completion values
    */
   Set<String> completeStepParameter(Step step, int parameterIndex, boolean insert);
}
