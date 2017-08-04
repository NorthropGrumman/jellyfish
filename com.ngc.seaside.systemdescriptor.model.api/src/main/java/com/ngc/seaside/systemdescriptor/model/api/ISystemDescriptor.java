package com.ngc.seaside.systemdescriptor.model.api;

import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IEnumeration;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.traversal.IVisitor;
import com.ngc.seaside.systemdescriptor.model.api.traversal.IVisitorContext;
import com.ngc.seaside.systemdescriptor.model.api.traversal.Traversals;

import java.util.Optional;

/**
 * The top level system descriptor object.  This object contains all known packages.  The description below shows the
 * tree like structure of this data structure:
 *
 * <pre>
 * <ul>
 *   <li>{@link ISystemDescriptor System Descriptor}</li>
 *   <ul>
 *     <li>{@link IPackage packages}</li>
 *     <ul>
 *       <li>{@link IData data}</li>
 *       <ul>
 *          <li>{@link com.ngc.seaside.systemdescriptor.model.api.data.IDataField data fields}</li>
 *       </ul>
 *       <li>{@link IModel models}</li>
 *       <ul>
 *         <li>{@link com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField input}</li>
 *         <li>{@link com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField output}</li>
 *         <li>{@link com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField required models}</li>
 *         <li>{@link com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField parts}</li>
 *         <li>{@link com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario scenarios}</li>
 *         <li>{@link com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep steps}</li>
 *       </ul>
 *     </ul>
 *   </ul>
 * </ul>
 * </pre>
 *
 * Operations that change the state of this object may throw {@code UnsupportedOperationException}s if the object is
 * immutable. Mutable instances of this interface may or may not be threadsafe.  In general, immutable instances of this
 * interface (and any objects referenced from this instance) are threadsafe.
 *
 * <p/>
 *
 * {@link SystemDescriptors} contains various utilities dealing with system descriptor instances.
 */
public interface ISystemDescriptor {

   /**
    * Gets all known packages.  The returned collection may not be modifiable if this object is immutable.
    *
    * @return all known packages
    */
   INamedChildCollection<ISystemDescriptor, IPackage> getPackages();

   /**
    * Traverses all objects contained by this descriptor.  The visitor may set a result on the context object to make
    * this operation behave similar to a "find" operation.
    *
    * @param visitor the visitor that will visit all objects contained by this descriptor
    * @return an optional that contains the value of {@link IVisitorContext#getResult()} if the visitor sets a result
    * via {@link IVisitorContext#setResult(Object)}; if the visitor does not set a result the optional is empty
    */
   default Optional<Object> traverse(IVisitor visitor) {
      return Traversals.traverse(this, visitor);
   }

   /**
    * Gets the model with the given fully qualified name
    *
    * @param fullyQualifiedName the fully qualified name of the model
    * @return an optional that contains the model or an empty optional of the model was not found
    * @throws IllegalArgumentException if the format of the fully qualified name does not match the format described in
    *                                  {@link IModel#getFullyQualifiedName()}
    * @see IModel#getFullyQualifiedName()
    */
   Optional<IModel> findModel(String fullyQualifiedName);

   /**
    * Gets the model with the given name which resides in the named packaged.
    *
    * @param packageName the name of the package that contains the model
    * @param name        the name of the model
    * @return an optional that contains the model or an empty optional of the model was not found
    */
   Optional<IModel> findModel(String packageName, String name);

   /**
    * Gets the data type with the given fully qualified name
    *
    * @param fullyQualifiedName the given fully qualified name of the data type
    * @return an optional that contains the data type or an empty optional of the data type was not found
    * @throws IllegalArgumentException if the format of the fully qualified name does not match the format described in
    *                                  {@link IData#getFullyQualifiedName()}
    * @see IData#getFullyQualifiedName()
    */
   Optional<IData> findData(String fullyQualifiedName);

   /**
    * Gets the data type with the given name which resides in the named packaged.
    *
    * @param packageName the name of the package that contains the data type
    * @param name        the name of the data type
    * @return an optional that contains the data type or an empty optional of the data type was not found
    */
   Optional<IData> findData(String packageName, String name);

   /**
    * Gets the enumeration type with the given fully qualified name
    *
    * @param fullyQualifiedName the given fully qualified name of the enumeration
    * @return an optional that contains the enumeration or an empty optional of the enumeration was not found
    * @throws IllegalArgumentException if the format of the fully qualified name does not match the format described in
    *                                  {@link IEnumeration#getFullyQualifiedName()}
    * @see IEnumeration#getFullyQualifiedName()
    */
   Optional<IEnumeration> findEnumeration(String fullyQualifiedName);

   /**
    * Gets the enumeration type with the given name which resides in the named packaged.
    *
    * @param packageName the name of the package that contains the enumeration
    * @param name        the name of the enumeration
    * @return an optional that contains the enumeration or an empty optional of the enumeration was not found
    */
   Optional<IEnumeration> findEnumeration(String packageName, String name);
}
