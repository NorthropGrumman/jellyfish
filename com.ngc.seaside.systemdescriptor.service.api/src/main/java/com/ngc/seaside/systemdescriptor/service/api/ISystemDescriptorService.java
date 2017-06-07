package com.ngc.seaside.systemdescriptor.service.api;

import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.validation.api.ISystemDescriptorValidator;

import java.nio.file.Path;
import java.util.Collection;

/**
 * The top level service used to interface with a system descriptor.  This is the entry point for most system descriptor
 * related operations.
 */
public interface ISystemDescriptorService {

   /**
    * Parses a system descriptor project where all the {@code .sd} files are organized under {@code src/main/sd}.
    * Always check the {@link IParsingResult#isSuccessful() result} of parsing to
    * before inspecting the {@code ISystemDescriptor}.  If the parsing was not successful, the descriptor may be in a
    * inconsistent state.
    *
    * @param projectDirectory a path to the root directory of the system descriptor project
    * @return the result of parsing
    * @throws ParsingException if some exception occurs during parsing
    */
   IParsingResult parseProject(Path projectDirectory);

   /**
    * Parses the given {@code .sd} files.  Always check the {@link IParsingResult#isSuccessful() result} of parsing to
    * before inspecting the {@code ISystemDescriptor}.  If the parsing was not successful, the descriptor may be in a
    * inconsistent state.
    *
    * @param paths the paths that point to {@code .sd} files
    * @return the result of parsing
    * @throws ParsingException if some exception occurs during parsing
    */
   IParsingResult parseFiles(Collection<Path> paths);

   /**
    * Returns an immutable copy of the given {@code ISystemDescriptor}
    *
    * @param descriptor the descriptor to create a copy of
    * @return an immutable copy
    */
   ISystemDescriptor immutableCopy(ISystemDescriptor descriptor);

   /**
    * Registers a validator that is used when parsing and validating system descriptor.
    *
    * @param validator the validator to register
    * @see ISystemDescriptorValidator
    */
   void addValidator(ISystemDescriptorValidator validator);

   /**
    * Unregisters a validator that has been registered via {@link #addValidator(ISystemDescriptorValidator)}.
    *
    * @param validator the validator to remove
    * @return true if the validator was removed, false if the validator was never added
    * @see ISystemDescriptorValidator
    */
   boolean removeValidator(ISystemDescriptorValidator validator);
}
