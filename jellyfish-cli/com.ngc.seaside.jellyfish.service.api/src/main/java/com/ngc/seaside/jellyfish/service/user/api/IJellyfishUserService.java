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
package com.ngc.seaside.jellyfish.service.user.api;

import java.nio.file.Path;
import java.util.Map;

/**
 * Service for handling different uses of the JELLYFISH_USER_HOME.
 */
public interface IJellyfishUserService {

   /**
    * Returns the jellyfish user properties.
    * 
    * @return the jellyfish user properties
    */
   Map<String, String> getJellyfishUserProperties();

   /**
    * Returns the path to jellyfish user home directory.
    * 
    * @return the path to jellyfish user home directory
    */
   Path getJellyfishUserHome();

   /**
    * Returns the path to the plugins directory for extending jellyfish.
    * 
    * @return the path to the plugins directory for extending jellyfish
    */
   Path getPluginsDirectory();

}
