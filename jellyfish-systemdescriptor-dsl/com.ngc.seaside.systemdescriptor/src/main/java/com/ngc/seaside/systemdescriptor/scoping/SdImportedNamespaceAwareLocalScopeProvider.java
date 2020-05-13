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
package com.ngc.seaside.systemdescriptor.scoping;

import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.scoping.impl.ImportedNamespaceAwareLocalScopeProvider;

import java.util.List;

/**
 * This scope provider is not currently used.
 * This scope provider behaves the same as
 * {@code ImportedNamespaceAwareLocalScopeProvider} except it allows the value
 * of the importedNamespace feature to be something other than a string. This is
 * used when processing import statements. The grammar has been constructed so
 * that the thing imported must exists and be an {@code Element}. This breaks
 * the behavior of the {@code ImportedNamespaceAwareLocalScopeProvider} because
 * that provider demands the type of the of the {@code importedNamespace}
 * feature be just a string. This code works around that issues.
 */
@Deprecated
public class SdImportedNamespaceAwareLocalScopeProvider extends ImportedNamespaceAwareLocalScopeProvider {

   @Override
   protected String getImportedNamespace(EObject object) {
      String namespace = null;
      // Get the "importedNamespace" attribute.
      EStructuralFeature feature = object.eClass().getEStructuralFeature("importedNamespace");
      // If the feature is set and the object is an import block, add the
      // value of the feature to the current scope.
      if (feature != null && SystemDescriptorPackage.Literals.IMPORT.equals(object.eClass())) {
         // Note we can't use object.eGet(feature) because that will cause
         // "Cyclic resolution of lazy links" errors. Use the code below to
         // just get the value of the text.
         List<INode> nodes = NodeModelUtils.findNodesForFeature(object, feature);
         namespace = NodeModelUtils.getTokenText(nodes.get(0));
      }
      return namespace;
   }

   public boolean isNamespaceRelevant(EObject object) {
      return object.eClass().getEStructuralFeature("importedNamespace") != null
            && SystemDescriptorPackage.Literals.IMPORT.equals(object.eClass());
   }
}
