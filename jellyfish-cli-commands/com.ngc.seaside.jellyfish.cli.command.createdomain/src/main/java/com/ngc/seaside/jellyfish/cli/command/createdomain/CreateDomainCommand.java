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
package com.ngc.seaside.jellyfish.cli.command.createdomain;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.api.DefaultParameter;
import com.ngc.seaside.jellyfish.api.DefaultUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildManagementService;
import com.ngc.seaside.jellyfish.service.name.api.IPackageNamingService;
import com.ngc.seaside.jellyfish.service.name.api.IProjectNamingService;
import com.ngc.seaside.jellyfish.service.template.api.ITemplateService;
import com.ngc.seaside.jellyfish.utilities.command.AbstractJellyfishCommand;
import com.ngc.seaside.systemdescriptor.service.log.api.ILogService;

@Deprecated
@Component(service = IJellyFishCommand.class)
public class CreateDomainCommand extends AbstractJellyfishCommand {

   private static final String NAME = "create-domain";
   private static final String DEFAULT_DOMAIN_TEMPLATE_FILE = "service-domain.java.vm";
   static final String BLOCS_PLUGINS_DEPENDENCY = "com.ngc.blocs:gradle.plugin";

   public static final String OUTPUT_DIRECTORY_PROPERTY = CommonParameters.OUTPUT_DIRECTORY.getName();
   public static final String CLEAN_PROPERTY = CommonParameters.CLEAN.getName();

   public static final String EXTENSION_PROPERTY = "extension";
   public static final String DOMAIN_TEMPLATE_FILE_PROPERTY = "domainTemplateFile";
   public static final String USE_VERBOSE_IMPORTS_PROPERTY = "useVerboseImports";

   // Note these parameters can only be used programmatically. Thus, we don't include them the usage object that
   // the user sees.
   public static final String PACKAGE_GENERATOR_PROPERTY = "packageGenerator";
   public static final String PROJECT_NAMER_PROPERTY = "projectNamer";
   public static final String GENERATED_OBJECT_PREDICATE_PROPERTY = "generatedObjectPredicate";

   static final Path DOMAIN_PATH = Paths.get("src", "main", "resources", "domain");


   public CreateDomainCommand() {
      super(NAME);
   }

   @Activate
   public void activate() {
      logService.trace(getClass(), "Activated");
   }

   @Deactivate
   public void deactivate() {
      logService.trace(getClass(), "Deactivated");
   }

   /**
    * Sets log service.
    *
    * @param ref the ref
    */
   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC,
         unbind = "removeLogService")
   public void setLogService(ILogService ref) {
      this.logService = ref;
   }

   /**
    * Remove log service.
    */
   public void removeLogService(ILogService ref) {
      setLogService(null);
   }

   /**
    * Sets template service.
    *
    * @param ref the ref
    */
   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC,
         unbind = "removeTemplateService")
   public void setTemplateService(ITemplateService ref) {
      this.templateService = ref;
   }

   /**
    * Remove template service.
    */
   public void removeTemplateService(ITemplateService ref) {
      setTemplateService(null);
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC,
         unbind = "removeProjectNamingService")
   public void setProjectNamingService(IProjectNamingService ref) {
      this.projectNamingService = ref;
   }

   public void removeProjectNamingService(IProjectNamingService ref) {
      setProjectNamingService(null);
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC,
         unbind = "removePackageNamingService")
   public void setPackageNamingService(IPackageNamingService ref) {
      this.packageNamingService = ref;
   }

   public void removePackageNamingService(IPackageNamingService ref) {
      setPackageNamingService(null);
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC)
   public void setBuildManagementService(IBuildManagementService ref) {
      this.buildManagementService = ref;
   }

   public void removeBuildManagementService(IBuildManagementService ref) {
      setBuildManagementService(null);
   }

   @Override
   protected IUsage createUsage() {
      return new DefaultUsage("Generate a BLoCS domain model gradle project.",
                              CommonParameters.GROUP_ID.advanced(),
                              CommonParameters.ARTIFACT_ID.advanced(),
                              CommonParameters.OUTPUT_DIRECTORY.required(),
                              CommonParameters.MODEL.required(),
                              CommonParameters.CLEAN.optional(),
                              CommonParameters.UPDATE_GRADLE_SETTING.advanced(),
                              CommonParameters.HEADER_FILE.advanced(),
                              new DefaultParameter<String>(DOMAIN_TEMPLATE_FILE_PROPERTY)
                                    .setDescription("The velocity template file")
                                    .advanced(),
                              new DefaultParameter<String>(USE_VERBOSE_IMPORTS_PROPERTY)
                                    .setDescription(
                                          "If true, imports from the same package will be included for "
                                          + "generated domains")
                                    .advanced(),
                              new DefaultParameter<String>(EXTENSION_PROPERTY)
                                    .setDescription("The extension of the generated domain files")
                                    .advanced());
   }

   @Override
   protected void doRun() {
       throw new UnsupportedOperationException("command is deprecated, do not use it");
   }
}
