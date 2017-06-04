package com.ngc.seaside.bootstrap.service.impl.bootstraptemplateservice;

import com.ngc.seaside.bootstrap.service.template.api.BootstrapTemplateException;
import com.ngc.seaside.bootstrap.service.template.api.IBootstrapTemplateService;

import java.nio.file.Path;

/**
 * @author justan.provence@ngc.com
 */
public class BootstrapTemplateService implements IBootstrapTemplateService {

   @Override
   public boolean templateExists(String templateName) {
      return false;
   }

   @Override
   public Path unpack(String templateName, Path outputDirectory, boolean clean)
            throws BootstrapTemplateException {
      return null;
   }
}
