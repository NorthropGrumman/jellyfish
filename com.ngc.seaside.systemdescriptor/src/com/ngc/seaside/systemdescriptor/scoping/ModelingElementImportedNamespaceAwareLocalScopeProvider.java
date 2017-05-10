package com.ngc.seaside.systemdescriptor.scoping;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.scoping.impl.ImportedNamespaceAwareLocalScopeProvider;

import com.ngc.seaside.systemdescriptor.systemDescriptor.Element;

public class ModelingElementImportedNamespaceAwareLocalScopeProvider extends ImportedNamespaceAwareLocalScopeProvider {

	@Override
	protected String getImportedNamespace(EObject object) {
		return doGetImportedNamespaceForModelElementInput(object);
	}
	
	private String doGetImportedNamespaceForModelElementInput(EObject object) {
		String namespace = null;
		// Get the "importedNamespace" attribute.
		EStructuralFeature feature = object.eClass().getEStructuralFeature("importedNamespace");
		// If the feature is set and the value of the feature is a ModelElement, then 
		// get the string value of the attribute.
		if (feature != null && Element.class.getName().equals(feature.getEType().getInstanceClassName())) {
			// Note we can't use object.eGet(feature) because that will cause 
			// "Cyclic resolution of lazy links" errors.  Use the code below to just get the value of the text.
			List<INode> nodes = NodeModelUtils.findNodesForFeature(object, feature);
			namespace =  NodeModelUtils.getTokenText(nodes.get(0));
		}
		return namespace;
	}
}
