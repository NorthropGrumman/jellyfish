package com.ngc.seaside.systemdescriptor.scoping;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.scoping.impl.ImportedNamespaceAwareLocalScopeProvider;

import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;

/**
 * This scope provider is not currently used.
 * 
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
