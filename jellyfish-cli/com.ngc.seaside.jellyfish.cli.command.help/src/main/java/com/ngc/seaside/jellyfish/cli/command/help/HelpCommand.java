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
package com.ngc.seaside.jellyfish.cli.command.help;

import com.google.common.base.Preconditions;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.api.DefaultParameter;
import com.ngc.seaside.jellyfish.api.DefaultUsage;
import com.ngc.seaside.jellyfish.api.ICommand;
import com.ngc.seaside.jellyfish.api.ICommandOptions;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandProvider;
import com.ngc.seaside.jellyfish.api.IParameter;
import com.ngc.seaside.jellyfish.api.IParameterCollection;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.api.ParameterCategory;
import com.ngc.seaside.jellyfish.utilities.console.api.ITableFormat;
import com.ngc.seaside.jellyfish.utilities.console.impl.stringtable.StringTable;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.BiPredicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Component(service = ICommand.class)
public final class HelpCommand implements ICommand<ICommandOptions> {

   private static final Collector<ICommand<?>, ?, SortedSet<ICommand<?>>> COMMAND_COLLECTOR =
            Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(ICommand::getName)));
   private static final Collector<IParameter<?>, ?, SortedSet<IParameter<?>>> PARAMETER_COLLECTOR =
            Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(IParameter::getName)));
   private static final BiPredicate<IParameter<?>, Boolean> ADVANCED_USAGE_PREDICATE =
            (param, advanced) -> advanced ? true : param.getParameterCategory() != ParameterCategory.ADVANCED;
   private static final Comparator<IParameter<?>> USAGE_COMPARATOR =
            Comparator.comparing((IParameter<?> param) -> param.getParameterCategory() != ParameterCategory.REQUIRED)
                     .thenComparing(IParameter::getName);

   public static final String VERBOSE_PARAMETER_NAME = "verbose";
   public static final String ADVANCED_PARAMETER_NAME = "advanced";

   public static final String COMMAND_NAME = "help";
   private static final int LINE_WIDTH = 119;
   private static final String INDENT = "   ";
   private static final IUsage COMMAND_USAGE = new DefaultUsage(
         "Prints this help",
         new DefaultParameter<>(VERBOSE_PARAMETER_NAME)
               .setDescription("Prints the help of all of the known commands")
               .setParameterCategory(ParameterCategory.OPTIONAL),
         new DefaultParameter<>(ADVANCED_PARAMETER_NAME)
               .setDescription("Prints all of the command's parameters, even advanced usage parameters")
               .setParameterCategory(ParameterCategory.OPTIONAL),
         new DefaultParameter<>("command")
               .setDescription("Command to print help")
               .setParameterCategory(ParameterCategory.OPTIONAL));

   private ILogService logService;

   private final TreeMap<String, ICommand<?>> commands = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
   private Map<Boolean, SortedSet<IParameter<?>>> jellyfishProviderParameters;

   /**
    * Sets the jellyfish provider
    */
   @Reference
   public void setJellyfishProvider(IJellyFishCommandProvider jellyfishProvider) {
      this.jellyfishProviderParameters = jellyfishProvider.getUsage().getAllParameters().stream()
               .collect(Collectors.partitioningBy(
                        (IParameter<?> param) -> param.getParameterCategory() == ParameterCategory.REQUIRED,
                        PARAMETER_COLLECTOR));
   }

   public void removeJellyfishProvider(IJellyFishCommandProvider jellyfishProvider) {
      jellyfishProviderParameters = null;
   }
   
   @Reference(cardinality = ReferenceCardinality.AT_LEAST_ONE)
   public void addCommand(ICommand<?> command) {
      Preconditions.checkNotNull(command);
      commands.put(command.getName(), command);
   }

   public void removeCommand(ICommand<?> command) {
      Preconditions.checkNotNull(command);
      commands.remove(command.getName());
   }

   @Reference
   public void setLogService(ILogService ref) {
      logService = ref;
   }

   public void removeLogService(ILogService ref) {
      setLogService(null);
   }

   @Activate
   public void activate() {
      logService.trace(getClass(), "Activated");
   }

   @Deactivate
   public void deactivate() {
      logService.trace(getClass(), "Deactivated");
   }

   @Override
   public String getName() {
      return COMMAND_NAME;
   }

   @Override
   public IUsage getUsage() {
      return COMMAND_USAGE;
   }

   @Override
   public void run(ICommandOptions commandOptions) {
      Preconditions.checkNotNull(commandOptions);
      IParameterCollection parameters = commandOptions.getParameters();
      boolean verbose = CommonParameters.evaluateBooleanParameter(parameters, "verbose");
      boolean advanced = CommonParameters.evaluateBooleanParameter(parameters, "advanced");

      IParameter<?> commandParameter = commandOptions.getParameters().getParameter("command");
      printHelp(commandParameter, advanced, verbose);
   }

   /**
    * Prints the help to the log service.
    *
    * @param commandParameter command to be printed or null if the generic help is to be printed
    * @param advanced         whether or not to print the command's optional advanced parameters
    * @param verbose          whether or not to print each command's help along with the overall usage
    */
   private void printHelp(IParameter<?> commandParameter, boolean advanced, boolean verbose) {
      if (commandParameter == null) {
         writeUsage(advanced, verbose);
      } else {
         writeCommandHelp(false, advanced, commandParameter.getStringValue());
      }
   }

   /**
    * Writes the usage for the JellyFish cli.
    *
    * @param advanced whether or not to print the command's optional advanced parameters
    * @param verbose  whether or not to print each command's help along with the overall usage
    */
   private void writeUsage(boolean advanced, boolean verbose) {
      logService.info(getClass(), "\nUsage: jellyfish <command-name> [parameter1=value1 ...]\n");
      logService.info(getClass(), 
               "Jellyfish is a command-line tool for inspecting System Descriptor "
               + "projects and generating various custom artifacts");
      logService.info(getClass(), 
               "from a project. Most commands require you to identify the System "
               + "Descriptor project, either using the parameter");
      logService.info(getClass(), 
               "`gav=<groupId>:<artifactId>:<version>` or `inputDirectory=<path-to-project>`.");
      logService.info(getClass(), "\nCommands:\n");
      if (verbose) {
         for (String cmd : commands.keySet()) {
            writeCommandHelp(true, advanced, cmd);
         }
      } else {
         StringTable<ICommand<?>> table =
                  getCommandTable(INDENT, commands.values().stream().collect(COMMAND_COLLECTOR));
         logService.info(getClass(), table);
         logService.info(getClass(), "\nTo see more detail about a command, "
               + "run `jellyfish help command=<command-name>`\n");
      }
   }


   /**
    * Writes the help for the given command.
    *
    * @param inUsage     if writing the help command within the usage
    * @param advanced    whether or not to print the command's optional advanced parameters
    * @param commandName name of command
    */
   private void writeCommandHelp(boolean inUsage, boolean advanced, String commandName) {
      String baseIndent = inUsage ? INDENT : "";
      String parameterIndent = inUsage ? INDENT + INDENT : INDENT;
      ICommand<?> command = commands.get(commandName);
      if (command == null) {
         logService.info(getClass(), commandName + " command not found\n");
      } else {
         Map<Boolean, SortedSet<IParameter<?>>> commands = command.getUsage()
                  .getAllParameters()
                  .stream()
                  .filter(param -> ADVANCED_USAGE_PREDICATE.test(param, advanced))
                  .collect(Collectors.partitioningBy(
                           (IParameter<?> param) -> param.getParameterCategory() == ParameterCategory.REQUIRED,
                           PARAMETER_COLLECTOR));
         if (command instanceof IJellyFishCommand) {
            commands.get(false).addAll(jellyfishProviderParameters.get(false));
            commands.get(true).addAll(jellyfishProviderParameters.get(true));
         }
         StringTable<IParameter<?>> parameterTable = getParameterTable(
               parameterIndent, commands.get(false));
         StringTable<IParameter<?>> requiredParameterTable = getParameterTable(parameterIndent,
                                                                               commands.get(true));
         if (inUsage) {
            TreeSet<ICommand<?>> set = new TreeSet<>(Comparator.comparing(ICommand::getName));
            set.add(command);
            StringTable<ICommand<?>> table = getCommandTable(baseIndent, set);
            logService.info(getClass(), table);
            logService.info(getClass(), '\n');
         } else {
            String parameterUsage = command.getUsage()
                     .getAllParameters()
                     .stream()
                     .filter(param -> ADVANCED_USAGE_PREDICATE.test(param, advanced))
                     .sorted(USAGE_COMPARATOR)
                     .map(HelpCommand::getUsageString)
                     .collect(Collectors.joining(" "));
            if (!parameterUsage.isEmpty()) {
               parameterUsage = " " + parameterUsage;
            }
            logService.info(getClass(), String.format("Usage: jellyfish %s%s%n", commandName, parameterUsage));
            logService.info(getClass(), command.getUsage().getDescription() + "\n");
         }
         if (!requiredParameterTable.getModel().getItems().isEmpty()) {
            logService.info(getClass(), baseIndent + "required parameters:\n");
            logService.info(getClass(), requiredParameterTable);
         }
         if (!parameterTable.getModel().getItems().isEmpty()) {
            logService.info(getClass(), baseIndent + "optional parameters:\n");
            logService.info(getClass(), parameterTable);
         }
      }
   }
   
   /**
    * Returns the properly-formatted StringTable for printing IJellyFishCommands.
    *
    * @param columnSpace String to be printed between columns
    * @param elements    commands to be added to the table
    * @return a properly-formatted StringTable for printing IJellyFishCommands
    */
   private StringTable<ICommand<?>> getCommandTable(String columnSpace, SortedSet<ICommand<?>> elements) {
      int maxNameWidth = commands.keySet().stream().mapToInt(String::length).max().orElse(0);
      return getTable(columnSpace, elements,
                      new JellyFishCommandFormat(LINE_WIDTH, columnSpace.length(), maxNameWidth));
   }

   /**
    * Returns the properly-formatted StringTable for printing IParameters.
    *
    * @param columnSpace String to be printed between columns
    * @param elements    parameters to be added to the table
    * @return a properly-formatted StringTable for printing IParameters
    */
   private StringTable<IParameter<?>> getParameterTable(String columnSpace, SortedSet<IParameter<?>> elements) {
      int maxNameWidth = commands.values()
            .stream()
            .flatMap(i -> i.getUsage().getAllParameters().stream())
            .mapToInt(p -> p.getName().length())
            .max()
            .orElse(1);
      return getTable(columnSpace, elements, new ParameterFormat(LINE_WIDTH, columnSpace.length(), maxNameWidth));
   }

   /**
    * Returns the properly-formatted StringTable
    *
    * @param columnSpace String to be printed between columns
    * @param elements    elements to be added to the table
    * @param format      instance of the ITableFormat
    * @return a properly-formatted StringTable for printing IParameters
    */
   private <T> StringTable<T> getTable(String columnSpace, SortedSet<T> elements, ITableFormat<T> format) {
      StringTable<T> table = new StringTable<>(format);
      if (columnSpace != null) {
         table.setColumnSpacer(columnSpace);
      }
      table.setRowSpacer("");
      table.setShowHeader(false);
      table.setShowRowNumber(false);
      List<T> list = new ArrayList<>();
      list.addAll(elements);
      table.getModel().addItems(list);
      return table;
   }

   private static final String getUsageString(IParameter<?> parameter) {
      StringBuilder builder = new StringBuilder();
      if (parameter.getParameterCategory() != ParameterCategory.REQUIRED) {
         builder.append('[');
      }
      builder.append(parameter.getName()).append("=value");
      if (parameter.getParameterCategory() != ParameterCategory.REQUIRED) {
         builder.append(']');
      }
      return builder.toString();
   }
   
}
