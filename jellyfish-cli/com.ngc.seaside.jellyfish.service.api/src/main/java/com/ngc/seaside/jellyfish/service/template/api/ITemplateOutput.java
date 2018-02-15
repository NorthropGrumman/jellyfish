package com.ngc.seaside.jellyfish.service.template.api;

import java.nio.file.Path;
import java.util.Map;

/**
 *
 */
public interface ITemplateOutput {

   Map<String, ?> getProperties();

   Path getOutputPath();

}
