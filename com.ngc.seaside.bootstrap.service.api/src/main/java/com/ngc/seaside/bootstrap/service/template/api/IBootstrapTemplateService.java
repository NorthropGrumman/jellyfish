package com.ngc.seaside.bootstrap.service.template.api;

import java.nio.file.Path;

/**
 * @author justan.provence@ngc.com
 */
public interface IBootstrapTemplateService {

   boolean templateExists(String templateName);

   Path unpack(String templateName, Path outputDirectory, boolean clean)
            throws BootstrapTemplateException;
}
