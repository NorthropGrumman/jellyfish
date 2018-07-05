package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.utils;

/**
 * Construct for managing variable names in a template.
 */
public interface IVariableManager {

   /**
    * Adds the given variable name to this manager. The manager will ensure that no other variables will have the same
    * name as this.
    * 
    * @param variableName variable name
    * @throws IllegalStateException if a variable has already been assigned this name
    */
   void add(String variableName);

   /**
    * Adds a variable to be managed, referenced by the given key with the given requested name.
    * 
    * @param key key to reference the variable name
    * @param requestedVariableName requested variable name
    * @throws IllegalStateException if the key has already been added
    */
   void add(Object key, String requestedVariableName);

   /**
    * Returns the variable name associated with the given key.
    * 
    * @param key key
    * @return the variable name associated with the given key
    */
   String get(Object key);

   /**
    * Enters a new scope. New variables created within the new scope are deleted upon calling {@link #exitScope()}.
    * It is up to implementations to determine if and how conflicts between variable names within different scopes
    * should be resolved.
    */
   void enterScope();

   /**
    * Exits the current. New variables created within the scope are deleted should be deleted.
    */
   void exitScope();

   /**
    * Clears all variables from this manager.
    */
   void clear();

}
