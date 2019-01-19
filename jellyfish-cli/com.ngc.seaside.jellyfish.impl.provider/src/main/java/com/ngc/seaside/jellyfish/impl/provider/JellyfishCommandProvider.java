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
package com.ngc.seaside.jellyfish.impl.provider;

import com.google.common.base.Preconditions;

import com.ngc.blocs.component.impl.common.DeferredDynamicReference;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.CommandException;
import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.api.DefaultJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.DefaultParameter;
import com.ngc.seaside.jellyfish.api.DefaultUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandProvider;
import com.ngc.seaside.jellyfish.api.IParameter;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.service.parameter.api.IParameterService;
import com.ngc.seaside.jellyfish.utilities.parsing.ParsingResultLogging;
import com.ngc.seaside.systemdescriptor.service.api.IParsingResult;
import com.ngc.seaside.systemdescriptor.service.api.ISystemDescriptorService;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A command provider that runs {@code IJellyfishCommand}s.  These commands require a valid SD project in order to be
 * executed.  This provider will not execute any command if the SD project is not configured correctly or contains
 * errors.
 */
@Component(service = IJellyFishCommandProvider.class)
public class JellyfishCommandProvider extends AbstractCommandProvider<
      IJellyFishCommandOptions,
      IJellyFishCommand,
      IJellyFishCommandOptions>
      implements IJellyFishCommandProvider {

   private final Map<String, IJellyFishCommand> commands = new ConcurrentHashMap<>();

   private ISystemDescriptorService systemDescriptorService;

   /**
    * Ensure the dynamic references are added only after the activation of this Component.
    */
   private DeferredDynamicReference<IJellyFishCommand> commandsRef = new DeferredDynamicReference<IJellyFishCommand>() {
      @Override
      protected void addPostActivate(IJellyFishCommand command) {
         commands.put(command.getName(), command);
      }

      @Override
      protected void removePostActivate(IJellyFishCommand command) {
         commands.remove(command.getName());
      }
   };

   @Override
   public IUsage getUsage() {
      List<IParameter<?>> parameters = new ArrayList<>();
      parameters.add(CommonParameters.INPUT_DIRECTORY.optional());
      parameters.add(CommonParameters.GROUP_ARTIFACT_VERSION.required());
      return new DefaultUsage("JellyFish - Generate artifacts from System Descriptor models", parameters);
   }

   @Override
   public IJellyFishCommand getCommand(String commandName) {
      return commands.get(commandName);
   }

   @Override
   public void addCommand(IJellyFishCommand command) {
      commandsRef.add(command);
   }

   @Override
   public void removeCommand(IJellyFishCommand command) {
      commandsRef.remove(command);
   }

   @Override
   public IJellyFishCommandOptions run(String[] arguments) {
      Preconditions.checkNotNull(arguments, "arguments must not be null.");
      Preconditions.checkArgument(arguments.length > 0,
                                  "cannot invoke jellyfish with at least one argument!");

      String commandName = arguments[0];
      JellyfishCommandContext ctx = parseParameters(commandName, Arrays.asList(arguments).subList(1, arguments.length));
      return runCommand(ctx);
   }

   @Override
   public void run(String command, IJellyFishCommandOptions commandOptions) {
      Preconditions.checkNotNull(command, "command may not be null!");
      Preconditions.checkArgument(!command.trim().isEmpty(), "command may not be empty!");
      Preconditions.checkNotNull(commandOptions, "commandOptions may not be null!");
      IJellyFishCommand c = getCommand(command);
      Preconditions.checkArgument(c != null, "could not find command named %s!", command);
      c.run(commandOptions);
   }

   @Activate
   public void activate() {
      commandsRef.markActivated();
   }

   @Deactivate
   public void deactivate() {
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC,
         unbind = "removeLogService")
   @Override
   public void setLogService(ILogService ref) {
      super.setLogService(ref);
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC,
         unbind = "removeParameterService")
   @Override
   public void setParameterService(IParameterService ref) {
      super.setParameterService(ref);
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC,
         unbind = "removeSystemDescriptorService")
   public void setSystemDescriptorService(ISystemDescriptorService ref) {
      this.systemDescriptorService = ref;
   }

   public void removeSystemDescriptorService(ISystemDescriptorService ref) {
      setSystemDescriptorService(null);
   }

   private JellyfishCommandContext parseParameters(String command, List<String> params) {
      JellyfishCommandContext ctx = new JellyfishCommandContext(command, parameterService.parseParameters(params));

      // Resolve the input directory parameters if necessary.
      if (!ctx.getOriginalParameters().containsParameter(CommonParameters.INPUT_DIRECTORY.getName())) {
         ctx.addParameter(new DefaultParameter<>(CommonParameters.INPUT_DIRECTORY.getName(),
                                                 new File(".").getAbsolutePath()).required());
      }

      // Resolve the GAV parameter if necessary.
      if (!ctx.getOriginalParameters().containsParameter(CommonParameters.GROUP_ARTIFACT_VERSION.getName())) {
         String gav = guessGav(new File(ctx.getParameters()
                                              .getParameter(CommonParameters.INPUT_DIRECTORY.getName())
                                              .getStringValue()));
         if (gav != null) {
            ctx.addParameter(new DefaultParameter<>(CommonParameters.GROUP_ARTIFACT_VERSION.getName(), gav));
         }
      }

      return ctx;
   }

   private String guessGav(File inputDirectory) {
      String gav = null;
      Path projectInfo = Paths.get(inputDirectory.getAbsolutePath(),
                                   "build",
                                   "publications",
                                   "mavenSd",
                                   "pom-default.xml");
      if (Files.isRegularFile(projectInfo)) {
         MavenXpp3Reader reader = new MavenXpp3Reader();
         try {
            Model model = reader.read(Files.newBufferedReader(projectInfo));
            gav = String.format("%s:%s:%s", model.getGroupId(), model.getArtifactId(), model.getVersion());
         } catch (Exception e) {
            logService.warn(getClass(), e, "Failed to read project information at expected location %s.", projectInfo);
         }
      } else {
         logService.debug(
               getClass(),
               "Could not find project information at expected location %s.  Jellyfish will assume the input"
               + " directory has a valid SD project layout.  If this is not what you want, run `gradle install` on the"
               + " System Descriptor project first.",
               projectInfo);
      }
      return gav;
   }

   private IJellyFishCommandOptions runCommand(JellyfishCommandContext ctx) {
      IJellyFishCommand command = getCommand(ctx.getCommand());
      Preconditions.checkArgument(command != null, "no command named '%s' found!", ctx.getCommand());
      verifyRequiredParameters(command, ctx.getParameters());

      IJellyFishCommandOptions options = buildCommandOptions(ctx);
      // If the result is not successful, log errors and abort.
      if (!options.getParsingResult().isSuccessful()) {
         // Note the "%s" format string ensures there will not be logging errors if the line in the file contains
         // a format string.
         ParsingResultLogging.logErrors(options.getParsingResult())
               .forEach(l -> logService.error(JellyfishCommandProvider.class, "%s", l));
         // Abort execution.
         throw new CommandException("System Descriptor project contains errors.  See logs.");
      }

      // Run the command.
      command.run(options);
      return options;
   }

   private IJellyFishCommandOptions buildCommandOptions(JellyfishCommandContext ctx) {
      DefaultJellyFishCommandOptions options = new DefaultJellyFishCommandOptions();
      options.setParameters(ctx.getParameters());
      options.setParsingResult(parseProject(ctx));
      return options;
   }

   private IParsingResult parseProject(JellyfishCommandContext ctx) {
      IParsingResult result;
      if (ctx.isGavSpecified()) {
         String gav = ctx.getParameters()
               .getParameter(CommonParameters.GROUP_ARTIFACT_VERSION.getName())
               .getStringValue();
         logService.debug(getClass(), "Project has a GAV of %s, parsing with dependency management support.", gav);
         result = systemDescriptorService.parseProject(gav);
      } else {
         String inputDirectory = ctx.getParameters()
               .getParameter(CommonParameters.INPUT_DIRECTORY.getName())
               .getStringValue();
         logService.debug(getClass(), "Project has no GAV, assuming input directory has correct SD project layout.");
         result = systemDescriptorService.parseProject(Paths.get(inputDirectory));
      }
      return result;
   }
}
