package com.ngc.seaside.systemdescriptor.model.impl.xtext;

import org.eclipse.emf.ecore.EObject;

/**
 * A interface that all types that wrap an XText type implement to enable unwrapping.
 *
 * @param <T> the type of object being wrapped
 */
public interface IUnwrappable<T extends EObject> {

   /**
    * Gets the wrapped object.
    */
   T unwrap();
}
