package com.ngc.seaside.jellyfish.cli.command.help;

import com.google.common.base.Preconditions;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.utilities.console.api.ITableFormat;
import com.ngc.seaside.jellyfish.utilities.console.impl.stringtable.StringTable;
import com.ngc.seaside.jellyfish.api.DefaultParameter;
import com.ngc.seaside.jellyfish.api.DefaultUsage;
import com.ngc.seaside.jellyfish.api.IParameter;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Component(service = IJellyFishCommand.class)
public final class HelpCommand implements IJellyFishCommand {
   public static final String COMMAND_NAME = "help";
   private static final int LINE_WIDTH = 80;
   private static final String INDENT = "   ";
   private static final IUsage COMMAND_USAGE = new DefaultUsage("Prints this help",
      new DefaultParameter<>("verbose").setDescription("Prints the help of all of the known commands").setRequired(false),
      new DefaultParameter<>("command").setDescription("Command to print help").setRequired(false));

   private ILogService logService;

   private final TreeMap<String, IJellyFishCommand> commands = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

   /**
    * Adds a command to the help
    *
    * @param command command to be added
    */
   @Reference(cardinality = ReferenceCardinality.AT_LEAST_ONE, policy = ReferencePolicy.STATIC, unbind = "removeCommand")
   public void addCommand(IJellyFishCommand command) {
      Preconditions.checkNotNull(command);
      commands.put(command.getName(), command);
   }

   /**
    * Removes a command from the help
    *
    * @param command command to be removed
    */
   public void removeCommand(IJellyFishCommand command) {
      Preconditions.checkNotNull(command);
      commands.remove(command.getName());
   }

   /**
    * Sets log service.
    *
    * @param ref the ref
    */
   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "removeLogService")
   public void setLogService(ILogService ref) {
      logService = ref;
   }

   /**
    * Remove log service.
    */
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
   public void run(IJellyFishCommandOptions commandOptions) {
      Preconditions.checkNotNull(commandOptions);
      IParameter<?> verboseParameter = commandOptions.getParameters().getParameter("verbose");

      final boolean verbose;
      if (verboseParameter == null) {
         verbose = false;
      } else {
         switch (verboseParameter.getStringValue()) {
         case "true":
            verbose = true;
            break;
         case "false":
            verbose = false;
            break;
         default:
            throw new IllegalArgumentException(
               "Invalid value for verbose: " + verboseParameter.getValue() + ". Expected true or false");
         }
      }

      IParameter<?> commandParameter = commandOptions.getParameters().getParameter("command");
      printHelp(commandParameter, verbose);
   }

   /**
    * Prints the help to the log service.
    *
    * @param commandParameter command to be printed or null if the generic help is to be printed
    * @param verbose whether or not to print each command's help along with the overall usage
    */
   private void printHelp(IParameter<?> commandParameter, boolean verbose) {
      StringBuilder builder = new StringBuilder();
      if (commandParameter == null) {
         writeUsage(builder, verbose);
      } else {
         writeCommandHelp(builder, false, commandParameter.getStringValue());
      }
      logService.info(getClass(), builder.toString());
   }

   /**
    * Writes the usage for the JellyFish cli.
    *
    * @param builder StringBuilder to write to
    * @param verbose whether or not to print each command's help along with the overall usage
    */
   private void writeUsage(StringBuilder builder, boolean verbose) {
      builder.append("Usage: jellyfish command [-Doption1=value1 ...]\n\nCommands:\n\n");
      if (verbose) {
         for (String cmd : commands.keySet()) {
            writeCommandHelp(builder, true, cmd);
         }
      } else {
         StringTable<IJellyFishCommand> table = getCommandTable(INDENT, commands.values());
         builder.append(table);
         builder.append("\nTo see more detail about a command, run `jellyfish help -Dcommand=<command-name>`\n");
      }
   }

   /**
    * Writes the help for the given command.
    *
    * @param builder StringBuilder to write to
    * @param inUsage if writing the help command within the usage
    * @param commandName name of command
    */
   private void writeCommandHelp(StringBuilder builder, boolean inUsage, String commandName) {
      String baseIndent = inUsage ? INDENT : "";
      String parameterIndent = inUsage ? INDENT + INDENT : INDENT;
      IJellyFishCommand command = commands.get(commandName);
      if (command == null) {
         builder.append(commandName + " command not found\n");
      } else {
         StringTable<IParameter<?>> parameterTable = getParameterTable(
               parameterIndent,
               command.getUsage().getAllParameters()
                     .stream()
                     .filter(p -> !p.isRequired())
                     .collect(Collectors.toList()));
         StringTable<IParameter<?>> requiredParameterTable = getParameterTable(parameterIndent,
            command.getUsage().getRequiredParameters());
         if (inUsage) {
            StringTable<IJellyFishCommand> table = getCommandTable(baseIndent, Collections.singleton(command));
            builder.append(table).append('\n');
         } else {
            String parameterUsage = command.getUsage().getAllParameters().stream()
                     .map(
                        p -> (p.isRequired() ? "" : "[") + "-D" + p.getName() + "=value" + (p.isRequired() ? "" : "]"))
                     .collect(Collectors.joining(" "));
            if (!parameterUsage.isEmpty()) {
               parameterUsage = " " + parameterUsage;
            }
            builder.append(String.format("Usage: jellyfish %s%s%n%n", commandName, parameterUsage));
            builder.append(command.getUsage().getDescription()).append("\n\n");
         }
         if (!requiredParameterTable.getModel().getItems().isEmpty()) {
            builder.append(baseIndent).append("required parameters:\n\n");
            builder.append(requiredParameterTable).append('\n');
         }
         if (!parameterTable.getModel().getItems().isEmpty()) {
            builder.append(baseIndent).append("optional parameters:\n\n");
            builder.append(parameterTable).append('\n');
         }
      }
   }

   /**
    * Returns the properly-formatted StringTable for printing IJellyFishCommands.
    *
    * @param columnSpace String to be printed between columns
    * @param elements commands to be added to the table
    * @return a properly-formatted StringTable for printing IJellyFishCommands
    */
   private StringTable<IJellyFishCommand> getCommandTable(String columnSpace, Collection<IJellyFishCommand> elements) {
      int maxNameWidth = commands.keySet().stream().mapToInt(String::length).max().orElse(0);
      return getTable(columnSpace, elements,
         new JellyFishCommandFormat(LINE_WIDTH, columnSpace.length(), maxNameWidth));
   }

   /**
    * Returns the properly-formatted StringTable for printing IParameters.
    *
    * @param columnSpace String to be printed between columns
    * @param elements parameters to be added to the table
    * @return a properly-formatted StringTable for printing IParameters
    */
   private StringTable<IParameter<?>> getParameterTable(String columnSpace, Collection<IParameter<?>> elements) {
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
    * @param elements elements to be added to the table
    * @param format instance of the ITableFormat
    * @return a properly-formatted StringTable for printing IParameters
    */
   private <T> StringTable<T> getTable(String columnSpace, Collection<T> elements, ITableFormat<T> format) {
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

}
