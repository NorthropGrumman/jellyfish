/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
 */
package com.ngc.seaside.systemdescriptor.service.impl.xtext.source.location;

import com.ngc.seaside.systemdescriptor.service.source.api.ISourceLocation;

public interface IDetailedSourceLocation extends ISourceLocation {

   /**
    * Returns the character offset from the start of the file that the location starts.
    * 
    * @return the character offset from the start of the file that the location starts
    */
   int getOffset();

   /**
    * Returns the location within this location
    * 
    * @param offset character offset from {@link #getOffset()}
    * @param length length
    * @return the location within this location
    */
   IDetailedSourceLocation getSubLocation(int offset, int length);

}
