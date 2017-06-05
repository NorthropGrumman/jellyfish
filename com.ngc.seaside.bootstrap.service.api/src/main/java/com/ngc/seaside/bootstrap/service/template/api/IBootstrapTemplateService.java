package com.ngc.seaside.bootstrap.service.template.api;

import java.nio.file.Path;

/**
 * This interface is intended to provide a way to unpack a zip file with the contents of a project. This class does
 * not and should not require knowledge of the type of project being created.
 *
 * The zip file structure should contain a template.properties file at the top level.
 * It should contain the property and it's default for any items within the template that require user
 * input.
 *
 */
public interface IBootstrapTemplateService {

   /**
    * Determine if the template exist by the given name.
    *
    * @param templateName
    * @return
    */
   boolean templateExists(String templateName);

   Path unpack(String templateName, Path outputDirectory, boolean clean)
            throws BootstrapTemplateException;
}
