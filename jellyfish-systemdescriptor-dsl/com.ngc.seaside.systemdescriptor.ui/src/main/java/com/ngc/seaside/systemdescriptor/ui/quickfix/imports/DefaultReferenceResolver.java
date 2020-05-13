/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
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
