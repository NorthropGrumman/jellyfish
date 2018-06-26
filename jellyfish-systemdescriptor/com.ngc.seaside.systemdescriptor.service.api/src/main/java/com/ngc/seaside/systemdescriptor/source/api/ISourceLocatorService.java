package com.ngc.seaside.systemdescriptor.source.api;

import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.link.IModelLink;

/**
 * Service for locating where a System Descriptor element is located in the source code.
 */
public interface ISourceLocatorService {

   /**
    * Returns the source location of the given system descriptor element, such as an {@link IModel},
    * {@link IModelLink}, or {@link IDataField}. If {@code entireElement} is {@code false}, the service will attempt to
    * narrow the location of the element, such as the location of just the name of the model for an {@link IModel}.
    * 
    * @param element system descriptor element
    * @param entireElement whether or not to return the location of the entire element
    * @return the source location of the given element
    */
   ISourceLocation getLocation(Object element, boolean entireElement);

}
