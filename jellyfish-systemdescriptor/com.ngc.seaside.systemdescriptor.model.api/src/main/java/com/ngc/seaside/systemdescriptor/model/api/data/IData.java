package com.ngc.seaside.systemdescriptor.model.api.data;

import com.ngc.seaside.systemdescriptor.model.api.INamedChild;
import com.ngc.seaside.systemdescriptor.model.api.INamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;

import java.util.Optional;

/**
 * Represents a data type.  Operations that change the state of this object may throw
 * {@code UnsupportedOperationException}s if the object is immutable.
 */
public interface IData extends INamedChild<IPackage> {

   /**
    * Gets the metadata associated with this data type.
    *
    * @return the metadata associated with this data type
    */
   IMetadata getMetadata();

   /**
    * Sets the metadata associated with this data type.
    *
    * @param metadata the metadata associated with this data type
    * @return this data type
    */
   IData setMetadata(IMetadata metadata);

   /**
    * Gets the data type that this data type extends.  If this data type does not extend another data type the
    * returned optional is empty.
    *
    * @return an optional that only contains a value if this data type extends another data type
    */
   Optional<IData> getExtendedDataType();

   /**
    * Sets the data type that this type extends.
    *
    * @param superDataType the data type that this type extends.
    * @return this data type
    */
   IData setExtendedDataType(IData superDataType);

   /**
    * Gets the data fields declared within this data type.  The fields are listed in the order they were declared.
    * The returned collection may not be modifiable if this object is immutable.
    *
    * @return the data fields declared within this data type
    */
   INamedChildCollection<IData, IDataField> getFields();

   /**
    * Gets the fully qualified name of this data type.  The fully qualified name is the name of the parent package,
    * appended with ".", appended with the name of this data type.  For example, the fully qualified name of a data type
    * named "HelloWorld" which resides in the package named "my.package" would be "my.package.HelloWorld".
    *
    * @return the fully qualified name of this data type
    */
   String getFullyQualifiedName();
}
