package com.ngc.seaside.systemdescriptor.scoping;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.scoping.impl.ImportedNamespaceAwareLocalScopeProvider;

import com.ngc.seaside.systemdescriptor.systemDescriptor.Data;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Element;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Import;

public class PackageBasedImportedNamespaceAwareLocalScopeProvider extends ImportedNamespaceAwareLocalScopeProvider {

	@Override
	protected String getImportedNamespace(EObject object) {
		return doGetImportedNamespaceForModelElementInput(object);
	}
	
	private String doGetImportedNamespaceForModelElementInput(EObject object) {
		String namespace = null;
		EObject o = object;
		System.out.println("objec t= " + object.eClass().getName());

		if (object instanceof Data) {
			namespace = "clocks.datatypes";
		} else {
			do {
				if (o instanceof Import) {
					EStructuralFeature feature = o.eClass().getEStructuralFeature("importedNamespace");
					// Note we can't use object.eGet(feature) because that will
					// cause
					// "Cyclic resolution of lazy links" errors. Use the code
					// below to just get the value of the text.
					List<INode> nodes = NodeModelUtils.findNodesForFeature(object, feature);
					namespace = NodeModelUtils.getTokenText(nodes.get(0));
					namespace = "clocks.datatypes";
				} else {
					o = o.eContainer();
				}
			} while (namespace == null && o != null);
		}

		System.out.println("namespace = " + namespace);
		return namespace;
	}
	
//	private String doGetImportedNamespaceForModelElementInput(EObject object) {
//		String namespace = null;
//		// Get the "importedNamespace" attribute.
//		EStructuralFeature feature = object.eClass().getEStructuralFeature("importedNamespace");
//		// If the feature is set and the value of the feature is a ModelElement, then 
//		// get the string value of the attribute.
//		if (feature != null && Element.class.getName().equals(feature.getEType().getInstanceClassName())) {
//			// Note we can't use object.eGet(feature) because that will cause 
//			// "Cyclic resolution of lazy links" errors.  Use the code below to just get the value of the text.
//			List<INode> nodes = NodeModelUtils.findNodesForFeature(object, feature);
//			namespace =  NodeModelUtils.getTokenText(nodes.get(0));
//		}
//		return namespace;
//	}
}
