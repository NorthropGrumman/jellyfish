/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
