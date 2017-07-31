package com.ngc.seaside.jellyfish.cli.command.report.requirementsverification;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultUsage;
import com.ngc.seaside.command.api.IUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

@Component(service = IJellyFishCommand.class)
public class RequirementsVerificationMatrixCommand implements IJellyFishCommand {

   public static final String OUTPUT_FORMAT_PROPERTY = "outputFormat";
   public static final String DEFAULT_OUTPUT_FORMAT_PROPERTY = "STDOUT_FORMAT";
   public static final String OUTPUT_PROPERTY = "output";
   public static final String DEFAULT_OUTPUT_PROPERTY = "STDOUT";
   public static final String SCOPE_PROPERTY = "scope";
   public static final String DEFAULT_SCOPE_PROPERTY = "model.metadata.stereotypes";
   public static final String VALUES_PROPERTY = "values";
   public static final String DEFAULT_VALUES_PROPERTY = "service";
   public static final String OPERATOR_PROPERTY = "operator";
   public static final String DEFAULT_OPERATOR_PROPERTY = "OR";
   private static final String NAME = "requirements-verification-matrix";
   private static final IUsage USAGE = createUsage();
   private ILogService logService;

   /**
    * Create the usage for this command.
    *
    * @return the usage.
    */
   @SuppressWarnings("rawtypes")
   private static IUsage createUsage() {
      return new DefaultUsage("A JellyFish command that can generate a requirements verification matrix.",
                              new DefaultParameter(OUTPUT_FORMAT_PROPERTY).setDescription(
                                       "Allows the user to define the output format. The possible values are default and csv")
                                       .setRequired(false),
                              new DefaultParameter(OUTPUT_PROPERTY).setDescription(
                                       "Allows the user to define the file where the output will be stored. Default: prints to stdout.")
                                       .setRequired(false),
                              new DefaultParameter(SCOPE_PROPERTY).setDescription(
                                       "Allows the user to enter a keyword scope (metadata, input, output, etc..) Default: model.metadata.stereotypes")
                                       .setRequired(false),
                              new DefaultParameter(VALUES_PROPERTY).setDescription(
                                       "The values in which to search as a comma separated string. Default: service")
                                       .setRequired(false),
                              new DefaultParameter(OPERATOR_PROPERTY).setDescription(
                                       "AND, OR, NOT: determines if the items be AND'd together or OR'd together. Default: OR")
                                       .setRequired(false));
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
      // TODO Auto-generated method stub
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
   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "removeLogService")
   public void setLogService(ILogService ref) {
      this.logService = ref;
   }

   /**
    * Remove log service.
    */
   public void removeLogService(ILogService ref) {
      setLogService(null);
   }

}
