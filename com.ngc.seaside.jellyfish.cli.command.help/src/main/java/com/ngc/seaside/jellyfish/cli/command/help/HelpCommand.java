package com.ngc.seaside.jellyfish.cli.command.help;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.bootstrap.utilities.console.api.ITableFormat;
import com.ngc.seaside.bootstrap.utilities.console.impl.stringtable.StringTable;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultUsage;
import com.ngc.seaside.command.api.IParameter;
import com.ngc.seaside.command.api.IUsage;
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

   private static final IUsage COMMAND_USAGE = new DefaultUsage("Prints this help", new DefaultParameter("verbose", "Prints the help of all of the known commands", false),
      new DefaultParameter("command", "Command to print help", false));

   private ILogService logService;

   private TreeMap<String, IJellyFishCommand> commands = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

   private int lineWidth = 80;

   private String indent = "   ";

   public void addCommand(IJellyFishCommand command) {
      Preconditions.checkNotNull(command);
      commands.put(command.getName(), command);
   }

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
   @Inject
   public void setLogService(ILogService ref) {
      logService = ref;
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
    * Remove log service.
    */
   public void removeLogService(ILogService ref) {
      setLogService(null);
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
      IParameter verboseParameter = commandOptions.getParameters().getParameter("verbose");

      final boolean verbose;
      if (verboseParameter == null) {
         verbose = false;
      } else {
         switch (verboseParameter.getValue()) {
         case "true":
            verbose = true;
            break;
         case "false":
            verbose = false;
            break;
         default:
            throw new IllegalArgumentException("Invalid value for verbose: " + verboseParameter.getValue() + ". Expected true or false");
         }
      }

      IParameter commandParameter = commandOptions.getParameters().getParameter("command");
      StringBuilder builder = new StringBuilder();
      if (commandParameter == null) {
         printHelp(builder, verbose);
      } else {
         printCommandHelp(builder, true, verbose, commandParameter.getValue());
      }
      logService.info(getClass(), builder.toString());
   }

   private void printHelp(StringBuilder builder, boolean verbose) {
      builder.append("Usage: jellyfish command [-DinputDir=dir] [-Doption1=value1 ...]\n\nCommands:\n\n");
      if (verbose) {
         for (String cmd : commands.keySet()) {
            printCommandHelp(builder, false, true, cmd);
         }
      } else {
         StringTable<IJellyFishCommand> table = getCommandTable(indent, commands.values());
         builder.append(table);
      }
   }

   /**
    * @param standAloneHelp if the help was called for just this command
    * @param verbose
    * @param name name of command
    */
   private void printCommandHelp(StringBuilder builder, boolean standAloneHelp, boolean verbose, String name) {
      String baseIndent = standAloneHelp ? "" : this.indent;
      String parameterIndent = standAloneHelp ? this.indent : this.indent + this.indent;
      IJellyFishCommand command = commands.get(name);
      if (command == null) {
         builder.append(name + " command not found\n");
      } else {
         StringTable<IParameter> parameterTable = getParameterTable(parameterIndent, command.getUsage().getAllParameters().stream().filter(p -> !p.isRequired()).collect(Collectors.toList()));
         StringTable<IParameter> requiredParameterTable = getParameterTable(parameterIndent, command.getUsage().getRequiredParameters());
         if (standAloneHelp) {
            String parameterUsage = command.getUsage().getAllParameters().stream().map(p -> (p.isRequired() ? "" : "[") + "-D" + p.getName() + "=value" + (p.isRequired() ? "" : "]"))
                     .collect(Collectors.joining(" "));
            if (!parameterUsage.isEmpty()) {
               parameterUsage = " " + parameterUsage;
            }
            builder.append(String.format("Usage: jellyfish %s [-DinputDir=dir]%s%n%n", name, parameterUsage));
            builder.append(command.getUsage().getDescription()).append('\n');
         } else {
            StringTable<IJellyFishCommand> table = getCommandTable(baseIndent, Collections.singleton(command));
            builder.append(table).append('\n');
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

   private StringTable<IJellyFishCommand> getCommandTable(String columnSpace, Collection<IJellyFishCommand> elements)
   {
      int maxNameWidth = commands.keySet().stream().mapToInt(String::length).max().orElse(0);
      return getTable(columnSpace, elements, new JellyFishCommandFormat(lineWidth, columnSpace.length(), maxNameWidth));
   }

   private StringTable<IParameter> getParameterTable(String columnSpace, Collection<IParameter> elements) {
      int maxNameWidth = commands.values().stream().flatMap(i -> i.getUsage().getAllParameters().stream()).mapToInt(p -> p.getName().length()).max().orElse(0);
      return getTable(columnSpace, elements, new ParameterFormat(lineWidth, columnSpace.length(), maxNameWidth));
   }

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
