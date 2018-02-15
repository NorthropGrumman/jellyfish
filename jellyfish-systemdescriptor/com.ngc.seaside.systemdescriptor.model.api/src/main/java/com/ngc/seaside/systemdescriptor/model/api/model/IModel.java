package com.ngc.seaside.systemdescriptor.model.api.model;

import com.ngc.seaside.systemdescriptor.model.api.INamedChild;
import com.ngc.seaside.systemdescriptor.model.api.INamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.api.model.link.IModelLink;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;

import java.util.Collection;
import java.util.Optional;

/**
 * Represents a model.  Operations that change the state of this object may throw {@code UnsupportedOperationException}s
 * if the object is immutable.
 */
public interface IModel extends INamedChild<IPackage> {

   /**
    * Gets the metadata associated with this model type.
    *
    * @return the metadata associated with this model type
    */
   IMetadata getMetadata();

   /**
    * Sets the metadata associated with this model type.
    *
    * @param metadata the metadata associated with this model type
    * @return this model type
    */
   IModel setMetadata(IMetadata metadata);

   /**
    * Gets the data inputs declared by this model.  The returned collection may not be modifiable if this object is
    * immutable.
    *
    * @return the data inputs declared by this model
    */
   INamedChildCollection<IModel, IDataReferenceField> getInputs();

   /**
    * Gets the data outputs declared by this model.  The returned collection may not be modifiable if this object is
    * immutable.
    *
    * @return the data outputs declared by this model
    */
   INamedChildCollection<IModel, IDataReferenceField> getOutputs();

   /**
    * Gets the models that this model requires.  The returned collection may not be modifiable if this object is
    * immutable.
    *
    * @return the models that this model requires
    */
   INamedChildCollection<IModel, IModelReferenceField> getRequiredModels();

   /**
    * Gets the parts or model sub-components declared by this model.  The returned collection may not be modifiable if
    * this object is immutable.
    *
    * @return the parts or model sub-components declared by this model
    */
   INamedChildCollection<IModel, IModelReferenceField> getParts();

   /**
    * Gets the scenarios declared by this model.  The returned collection may not be modifiable if this object is
    * immutable.
    *
    * @return the scenarios declared by this model
    */
   INamedChildCollection<IModel, IScenario> getScenarios();

   /**
    * Gets the links declared by this model.  The returned collection may not be modifiable if this object is immutable.
    *
    * @return the links declared by this model
    */
   Collection<IModelLink<?>> getLinks();

   /**
    * Gets the link with the given name.  Since links do not require names, a link with the specified name may not exist
    * in the model.
    *
    * @param name The name of the link to get.
    * @return the optional value containing a link with the given name, if one exists.
    */
   Optional<IModelLink<?>> getLink(String name);

   /**
    * Gets the fully qualified name of this model type.  The fully qualified name is the name of the parent package,
    * appended with ".", appended with the name of this model type.  For example, the fully qualified name of a model
    * type named "HelloWorld" which resides in the package named "my.package" would be "my.package.HelloWorld".
    *
    * @return the fully qualified name of this model type
    */
   String getFullyQualifiedName();
}
