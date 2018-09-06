/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
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
package com.ngc.seaside.systemdescriptor.ui.quickfix.imports;

import com.google.inject.ImplementedBy;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.naming.QualifiedName;

import java.util.Set;
import java.util.function.Predicate;

/**
 * Interface for resolving an unknown reference.
 */
@FunctionalInterface
@ImplementedBy(DefaultReferenceResolver.class)
public interface IReferenceResolver {

   /**
    * Returns the possible qualified names that the given reference could be referring to.
    *
    * @param reference   type reference
    * @param resourceSet resource set
    * @param filter      a predicate for determining which EObjects to consider
    * @return the set of possible qualified names for the given reference
    */
   Set<QualifiedName> findPossibleTypes(String reference, ResourceSet resourceSet,
                                        Predicate<? super EObject> filter);
}
