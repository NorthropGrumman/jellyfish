package com.ngc.blocs.json.resource.impl.common.json;

import com.ngc.blocs.service.resource.api.IReadableFileResource;
import com.ngc.blocs.service.resource.api.IWritableFileResource;

public interface IJsonResource<T> extends IReadableFileResource, IWritableFileResource {

   /**
    * Gets the Java object converted from JSON.  Only call this method after this resource has been read via {@link
    * com.ngc.blocs.service.resource.api.IResourceService#readFileResource(com.ngc.blocs.service.resource.api.IReadableFileResource)}.
    */
   T get();

   /**
    * Sets the object that will be converted to JSON on the next call to {@link com.ngc.blocs.service.resource.api.IResourceService#writeFileResource(com.ngc.blocs.service.resource.api.IWritableFileResource)}.
    */
   void set(T toSave);

   /**
    * Gets any error that occurred during conversion to or from JSON  Use this operation after {@code
    * IResourceService.readFileResource} or {@code IResourceService.writeFileResource} returns {@code false}.
    */
   Throwable getError();
}
