package com.ngc.seaside.jellyfish.cli.command.report.requirementsverification;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultUsage;
import com.ngc.seaside.command.api.IUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.traversal.ModelPredicates;
import com.ngc.seaside.systemdescriptor.model.api.traversal.Traversals;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Component(service = IJellyFishCommand.class)
public class RequirementsVerificationMatrixCommand implements IJellyFishCommand {

   public static final String OUTPUT_FORMAT_PROPERTY = "outputFormat";
   public static final String DEFAULT_OUTPUT_FORMAT_PROPERTY = "DEFAULT";
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

   /**
    * Retrieve the output format property value based on user input. Default is a table formatted string.
    *
    * @param commandOptions Jellyfish command options containing user params
    */
   private static String evaluateOutputFormat(IJellyFishCommandOptions commandOptions) {
      String outputFormat = DEFAULT_OUTPUT_FORMAT_PROPERTY;
      if (commandOptions.getParameters().containsParameter(OUTPUT_FORMAT_PROPERTY)) {
         String helper = commandOptions.getParameters().getParameter(OUTPUT_FORMAT_PROPERTY).getStringValue();
         outputFormat = (helper.equalsIgnoreCase("CSV")) ? helper.toUpperCase() : DEFAULT_OUTPUT_FORMAT_PROPERTY;
      }
      return outputFormat;
   }

   /**
    * Retrieve the output property value based on user input. Default is standard output
    *
    * @param commandOptions Jellyfish command options containing user params
    */
   private static String evaluateOutput(IJellyFishCommandOptions commandOptions) {
      String output = DEFAULT_OUTPUT_PROPERTY;
      if (commandOptions.getParameters().containsParameter(OUTPUT_PROPERTY)) {
         output = commandOptions.getParameters().getParameter(OUTPUT_PROPERTY).getStringValue();
      }
      return output;
   }

   /**
    * Retrieve the scope property value based on user input. Default is: "model.metadata.stereotypes"
    *
    * @param commandOptions Jellyfish command options containing user params
    */
   private static String evaluateScope(IJellyFishCommandOptions commandOptions) {
      String scope = DEFAULT_SCOPE_PROPERTY;
      if (commandOptions.getParameters().containsParameter(SCOPE_PROPERTY)) {
         scope = commandOptions.getParameters().getParameter(SCOPE_PROPERTY).getStringValue();
      }
      return scope;
   }

   /**
    * Retrieve the values property value based on user input. Default is: "service"
    *
    * @param commandOptions Jellyfish command options containing user params
    */
   private static String evaluateValues(IJellyFishCommandOptions commandOptions) {
      String values = DEFAULT_VALUES_PROPERTY;
      if (commandOptions.getParameters().containsParameter(VALUES_PROPERTY)) {
         values = commandOptions.getParameters().getParameter(VALUES_PROPERTY).getStringValue();
      }
      return values;
   }

   /**
    * Retrieve the operator property value based on user input. Default is: "OR"
    *
    * @param commandOptions Jellyfish command options containing user params
    */
   private static String evaluateOperator(IJellyFishCommandOptions commandOptions) {
      String operator = DEFAULT_OPERATOR_PROPERTY;
      if (commandOptions.getParameters().containsParameter(OPERATOR_PROPERTY)) {
         String helper = commandOptions.getParameters().getParameter(OPERATOR_PROPERTY).getStringValue().toUpperCase();
         operator = (helper.equalsIgnoreCase("AND") || helper.equalsIgnoreCase("NOT")) ? helper : operator;
      }
      return operator;
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
      String outputFormat = evaluateOutputFormat(commandOptions);
      String output = evaluateOutput(commandOptions);
      String scope = evaluateScope(commandOptions);
      String values = evaluateValues(commandOptions);
      String operator = evaluateOperator(commandOptions);

      Collection<IModel> models = searchModels(commandOptions, values, operator);

     // models.forEach(model -> {
         //model.getScenarios().getByName("b").orElse(null);

//         try {
         //System.out.println(PropertyUtils.getProperty(model, "inputs.byName(trackEngagementStatus).get.metadata.json.stereotypes").toString().contains("service"));
         //System.out.println(PropertyUtils.(model).toString());
//         } catch (IllegalAccessException e) {
//            e.printStackTrace();
//         } catch (InvocationTargetException e) {
//            e.printStackTrace();
//         } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//         }
     // });

      //StringTable
//      new DefaultTableModel<>();
//
      commandOptions.getSystemDescriptor().getPackages().forEach(iPackage -> {
         iPackage.getModels().forEach(iModel -> {
            System.out.println("MODEL:" + iModel.getMetadata().getJson());

            iModel.getScenarios().forEach(scenario -> {
               System.out.println("SCENARIO:" + scenario.getMetadata().getJson());

            });
         });
      });
   }

   /**
    * Returns a collection of models that matches the search criteria
    *
    * @param commandOptions Jellyfish command options containing system descriptor
    * @param values         the values in which to search
    * @param operator       the operator to apply to search
    */
   private Collection<IModel> searchModels(IJellyFishCommandOptions commandOptions, String values, String operator) {
      ISystemDescriptor sd = commandOptions.getSystemDescriptor();

      switch (operator) {
      case "AND":
         return Traversals.collectModels(sd, ModelPredicates.withAllStereotypes(valuesToCollection(values)));
      case "NOT":
         return Traversals.collectModels(sd, ModelPredicates.withAnyStereotype(valuesToCollection(values)).negate());
      default: // OR
         return Traversals.collectModels(sd, ModelPredicates.withAnyStereotype(valuesToCollection(values)));
      }
   }

   /**
    *
    * @param values
    * @return
    */
   private Collection<String> valuesToCollection(String values) {
      List<String> valueCollection = new ArrayList<>();
      if (values.contains(",")) {
         valueCollection.addAll(Arrays.asList(values.split(",")));
      } else {
         valueCollection.add(values);
      }
      return valueCollection;
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
