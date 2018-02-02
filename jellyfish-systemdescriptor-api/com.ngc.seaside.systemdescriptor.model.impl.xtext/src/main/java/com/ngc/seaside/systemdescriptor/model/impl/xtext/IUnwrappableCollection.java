package com.ngc.seaside.systemdescriptor.model.impl.xtext;

import org.eclipse.emf.ecore.EObject;

import java.util.Collection;

/**
 * Similar to {@link IUnwrappable} but wraps a <i>collection</i> of XText object.
 *
 * @param <T> the type of object being wrapped
 */
public interface IUnwrappableCollection<T extends EObject> {

   /**
    * Gets the wrapped objects.
    */
   Collection<? extends T> unwrapAll();
}
