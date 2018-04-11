package com.ngc.seaside.systemdescriptor.ui.quickfix.imports;

import com.google.inject.Inject;

import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.resource.IResourceDescriptions;
import org.eclipse.xtext.resource.IResourceDescriptionsProvider;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

class DefaultReferenceResolver implements IReferenceResolver {

   /**
    * A provider that is used to get the IResourceDescriptions which serves as
    * the main index or cache for Xtext. This is used do reverse lookups to
    * elements using their qualified names.
    */
   @Inject
   private IResourceDescriptionsProvider resourceDescriptionsProvider;

   @Override
   public Set<QualifiedName> findPossibleTypes(String reference, ResourceSet resourceSet,
                                               Predicate<? super EObject> filter) {
      Set<QualifiedName> imports = new HashSet<>();
      if (filter == null) {
         filter = __ -> true;
      }

      IResourceDescriptions descriptions = resourceDescriptionsProvider.getResourceDescriptions(
            resourceSet);

      for (IEObjectDescription description : descriptions.getExportedObjectsByType(
            SystemDescriptorPackage.Literals.ELEMENT)) {
         if (filter.test(description.getEObjectOrProxy())) {
            QualifiedName name = description.getQualifiedName();
            if (name.getLastSegment().equals(reference)) {
               imports.add(name);
            }
         }
      }

      return imports;
   }

}
