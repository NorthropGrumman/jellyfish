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
package com.ngc.seaside.jellyfish.service.template.api;

import com.ngc.seaside.jellyfish.api.IParameterCollection;

import java.nio.file.Path;

/**
 * This interface is intended to provide a way to unpack a zip file with the contents of a project. This class does
 * not and should not require knowledge of the type of project being created.
 * The zip file structure should contain a template.properties file at the top level.
 * It should contain the property and it's default for any items within the template that require user
 * input.
 */
public interface ITemplateService {

   /**
    * Determine if the template exist by the given name.
    *
    * @param templateName the name of the template.
    * @return true if the template exists.
    */
   boolean templateExists(String templateName);

   /**
    * Unpack the given template to the output directory.
    *
    * @param templateName    the name of the template.
    * @param outputDirectory the directory to unpack the template.
    * @param parameters      the parameters that should take the place of any parameters located
    *                        in the template.properties file.
    * @param clean           whether or not to recursively delete already existing folder before creating them again
    * @return The Path to the unpacked template directory.
    * @throws TemplateServiceException if any errors occur when parsing or unpacking the template.
    */
   ITemplateOutput unpack(String templateName, IParameterCollection parameters, Path outputDirectory, boolean clean)
         throws TemplateServiceException;
}
