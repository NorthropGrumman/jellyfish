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
