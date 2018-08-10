package com.ngc.seaside.systemdescriptor.service.api;

import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;

import java.nio.file.Path;
import java.util.Collection;

/**
 * Stores the result of attempting to parse system descriptor files.
 *
 * @see ISystemDescriptorService#parseProject(Path)
 * @see ISystemDescriptorService#parseFiles(Collection)
 */
public interface IParsingResult {

   /**
    * Gets the system descriptor which contains all the packages that were successfully parsed. Use {@link
    * #isSuccessful()} to verify if the result of parsing was successful.
    */
   ISystemDescriptor getSystemDescriptor();

   /**
    * Returns true if the system descriptor files were successfully parsed without errors. If false is returned, the
    * {@code ISystemDescriptor} associated with this result may not be valid or may only be partially complete.
    *
    * @return true if the system descriptor files were successfully parsed without errors, false if there are errors
    */
   boolean isSuccessful();

   /**
    * Gets all issues associated with the parsed system descriptor. If there are no issues, an empty collection is
    * returned.
    *
    * @return an unmodifiable collection of parsing related issues
    */
   Collection<IParsingIssue> getIssues();

   /**
    * Returns a path to the root directory containing the main source files in the system descriptor project. This
    * includes system descriptor files and resource files. This method may return {@code null}.
    * 
    * @return the root directory containing the main source files
    */
   Path getMainSourcesRoot();

   /**
    * Returns a path to the root directory containing the test source files in the system descriptor project. This
    * includes feature files and test resource files. This method may return {@code null}.
    * 
    * @return the root directory containing the test source files
    */
   Path getTestSourcesRoot();

}
