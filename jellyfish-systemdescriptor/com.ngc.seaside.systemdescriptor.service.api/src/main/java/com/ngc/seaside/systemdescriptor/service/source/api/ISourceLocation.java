package com.ngc.seaside.systemdescriptor.service.source.api;

import java.nio.file.Path;

/**
 * Represents the source code location of a System Descriptor element.
 */
public interface ISourceLocation {

   /**
    * Returns the path to the System Descriptor file.
    * 
    * @return the path to the System Descriptor file
    */
   Path getPath();

   /**
    * Returns the line number of the location within the {@link #getPath() file}. Line numbers start at {@code 1}.
    * 
    * @return the line number of the location
    */
   int getLineNumber();

   /**
    * Returns the character offset from the start of the {@link #getLineNumber() line} where the location starts.
    * Column numbers start at {@code 1}.
    * 
    * @return the character offset from the start of the {@link #getLineNumber() line}
    */
   int getColumn();

   /**
    * Returns the character length of the location within the {@link #getPath() file} from the {@link #getColumn()
    * offset}.
    * 
    * @return the character length of the location
    */
   int getLength();
}
