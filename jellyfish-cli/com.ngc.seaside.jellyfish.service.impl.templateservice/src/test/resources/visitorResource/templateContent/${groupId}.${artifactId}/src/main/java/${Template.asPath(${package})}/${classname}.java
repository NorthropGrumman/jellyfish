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
package ${package};

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.utilites.file.FileUtilitiesException;
import com.ngc.seaside.jellyfish.utilites.file.GradleSettingsUtilities;
import com.ngc.seaside.jellyfish.api.CommandException;
import com.ngc.seaside.jellyfish.api.DefaultParameter;
import com.ngc.seaside.jellyfish.api.DefaultUsage;
import com.ngc.seaside.jellyfish.api.IParameterCollection;
import com.ngc.seaside.jellyfish.api.IUsage;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

/**
 *
 */
@Component(service = IJellyFishCommand.class)
public class ${classname} implements IJellyFishCommand {

   private static final String FIRSTNAME = "${pojo.firstName}";
   private static final String LASTNAME = "${pojo.lastName}";

   private static final String NAME = "${commandName}";
   private static final IUsage USAGE = createUsage();

   private static final String OUTPUT_DIR_PROPERTY = "outputDirectory";
   private static final String GROUP_ID_PROPERTY = "groupId";
   private static final String ARTIFACT_ID_PROPERTY = "artifactId";
   private static final String PACKAGE_PROPERTY = "package";
   private static final String CLASSNAME_PROPERTY = "classname";

   private ILogService logService;

   @Activate
   public void activate() {
      logService.info(getClass(), "activated");
   }

   @Deactivate
   public void deactivate() {
      logService.info(getClass(), "deactivated");
   }

   @Reference(
       unbind="removeLogService",
       cardinality = ReferenceCardinality.MANDATORY,
       policy = ReferencePolicy.STATIC)
   public void setLogService(ILogService ref) {
      this.logService = ref;
   }

   public void removeLogService(ILogService ref) {
      setLogService(null);
   }

   @Override
   public String getName() {
      return NAME;
   }

   @Override
   public IUsage getUsage() {
      return USAGE;
   }

   @Override
   public void run(IJellyFishCommandOptions commandOptions) {
      IParameterCollection parameters = commandOptions.getParameters();

      try {
         //adds the new project to the settings.gradle file located in the outputDirectory
         GradleSettingsUtilities.addProject(parameters);
      } catch (FileUtilitiesException e) {
        logService.warn(getClass(), e, "Unable to add the new project to settings.gradle.");
        throw new CommandException(e);
      }
   }

   /**
    * Create the usage for this command. Some of these parameters are provided in the template.
    *
    * @return the usage.
    */
   private static IUsage createUsage() {
         //TODO Fix the description
         return new DefaultUsage(
         "The ${commandName} ... fill in the blank.",
            new DefaultParameter(CLASSNAME_PROPERTY)
               .setDescription("The name of the class that will be generated. i.e. MyClass")
               .required(),
            new DefaultParameter(GROUP_ID_PROPERTY)
               .setDescription("The groupId. This is usually similar to com.ngc.myprojectname.")
               .required(),
            new DefaultParameter(ARTIFACT_ID_PROPERTY)
               .setDescription("The artifactId. This is usually the lowercase version of the classname.")
               .required(),
            new DefaultParameter(PACKAGE_PROPERTY)
               .setDescription("The default package for the classname to reside. This is usually a combination of the groupId.artifactId")
               .required(),
            new DefaultParameter(OUTPUT_DIR_PROPERTY)
               .setDescription("The directory in which the bundle has been created.")
               .required());
   }
}
