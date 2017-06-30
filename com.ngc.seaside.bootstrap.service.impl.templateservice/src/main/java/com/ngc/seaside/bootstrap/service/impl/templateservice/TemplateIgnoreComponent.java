package com.ngc.seaside.bootstrap.service.impl.templateservice;

import com.ngc.blocs.service.log.api.ILogService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Ignore the files in the template that are listed in the template's ignore file.
 * These files are still copied, they just aren't run through the Velocity engine which will
 * usually corrupt any binary files such as zips.
 */
public class TemplateIgnoreComponent {

   private static final String TEMPLATE_IGNORE_FILENAME = "template.ignore";

   private final Path templateFile;
   private final Path templateFolder;
   private final List<Path> ignoreList = new ArrayList<>();
   private final ILogService logService;

   /**
    * Constructor
    * @param templateDir    the directory of the template.
    * @param templateFolder the template directory within the template.
    * @param logService     the log service.
    */
   public TemplateIgnoreComponent(Path templateDir, String templateFolder, ILogService logService) {
      this.templateFolder = templateDir.resolve(templateFolder);
      this.templateFile = templateDir.resolve(TEMPLATE_IGNORE_FILENAME);
      this.logService = logService;
   }

   /**
    * Parses the template's ignore file, collecting the list of files that
    * velocity should ignore.
    *
    * @return This instance.
    */
   public TemplateIgnoreComponent parse() throws IOException {
      ignoreList.clear();

      // Only parse the template file if it exists
      if (templateFile.toFile().exists()) {
         for (String eachPathStr : Files.readAllLines(templateFile)) {
            final Path eachPath = templateFolder.resolve(eachPathStr);
            ignoreList.add(eachPath);
         }
      } else {
         logService.trace(getClass(),
                          "Template ignore file (%s) does not exist, no files will be ignored.",
                          TEMPLATE_IGNORE_FILENAME);
      }

      return this;
   }

   /**
    * Checks if the specified path is to be ignored.
    *
    * @param path The path to check.
    * @return true if the path should be ignored, false otherwise.
    */
   public boolean contains(Path path) {
      return ignoreList.contains(path);
   }
}
