package com.ngc.seaside.systemdescriptor.model.api.model.link;

import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperties;

import java.util.Optional;

/**
 * Represents a link between two fields of a model.  Links usually join together two {@link IModel}s or {@link
 * com.ngc.seaside.systemdescriptor.model.api.data.IData}s types.  Operations that change the state of this object may
 * throw {@code UnsupportedOperationException}s if the object is immutable.
 *
 * @param <T> the type of reference field that link is connecting
 */
public interface IModelLink<T extends IReferenceField> {

   /**
    * Gets the metadata for this link.
    *
    * @return the metadata for this link
    */
   IMetadata getMetadata();

   /**
    * Sets the metadata for this link.
    *
    * @param metadata the metadata for this link
    * @return this link
    */
   IModelLink<T> setMetadata(IMetadata metadata);

   /**
    * Gets the properties of this link.
    *
    * @return the properties of this link (never {@code null})
    */
   default IProperties getProperties() {
      // TODO: make this method abstract once implemented
      throw new UnsupportedOperationException("Not implemented");
   }

   /**
    * Sets the properties of this link.
    *
    * @param properties the properties of this link
    * @return this link
    */
   default IModelLink<T> setProperties(IProperties properties) {
      // TODO: make this method abstract once implemented
      throw new UnsupportedOperationException("Not implemented");
   }

   /**
    * Gets the source of the link.
    *
    * @return the source of the link
    */
   T getSource();

   /**
    * Sets the source of the link
    *
    * @param source the source of the link
    * @return this link
    */
   IModelLink<T> setSource(T source);

   /**
    * Gets the target of the link.
    */
   T getTarget();

   /**
    * Sets the target of the link
    *
    * @param target the target of the link
    * @return this link
    */
   IModelLink<T> setTarget(T target);

   /**
    * Gets the link's name, if it has one.
    *
    * @return the name of the link, if one exists.
    */
   Optional<String> getName();

   /**
    * Sets the link name.
    *
    * @param name the name for the link
    * @return this link
    */
   IModelLink<T> setName(String name);

   /**
    * Gets the link that this link refines. If this link does not refine a link ,the optional is empty.
    *
    * @return the link this link is refining
    */
   Optional<IModelLink<T>> getRefinedLink();

   /**
    * Gets the link that this link is refining.
    *
    * @param refinedLink the link that this lin is refining
    * @return this link
    */
   IModelLink<T> setRefinedLink(IModelLink<T> refinedLink);

   /**
    * Gets the parent model that contains this link.
    *
    * @return the parent model that contains this link
    */
   IModel getParent();
}
