package com.ngc.seaside.systemdescriptor.model.api.data;

/**
 * Defines the different data types that are available.  All data types other than {@link #DATA} are consider primitive
 * types.  The {@code DATA} indicates a field is referencing another {@link IData} object.
 */
public enum DataTypes {
   INT,
   FLOAT,
   STRING,
   BOOLEAN,
   DATA,
   ENUM
}
