package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.utils;

/**
 * Construct for managing java imports in a template.
 */
public interface IImportManager {

   /**
    * Sets the package for the java class containing the imports.
    * 
    * @param pkg package
    */
   void setPackage(String pkg);

   /**
    * Adds the given import to this manager. It is up to the implementation to determine how to handle conflicting
    * imports and wildcard imports.
    * 
    * @param imp the import to add
    */
   void add(String imp);

   /**
    * Adds the given static import to this manager. It is up to the implementation to determine how to handle
    * conflicting imports and wildcard imports.
    * 
    * @param imp the static import to add
    */
   void addStatic(String imp);

   /**
    * Clears the manager of all imports and static imports
    */
   void clear();

   /**
    * Returns a string for the imports section in a java class.
    * 
    * @return a string for the imports section in a java class
    */
   String generateJava();

   /**
    * Returns the type string given the fully qualified name. This method should return just the class name if there
    * is already an import for the given fully qualified name in this manager; otherwise it should return the fully
    * qualified name itself.
    * 
    * @return the type string given the fully qualified name
    */
   String getType(String fullyQualifiedName);

}
