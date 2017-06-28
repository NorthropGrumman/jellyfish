package com.ngc.seaside.jellyfish.cli.command.help;

import java.util.TreeMap;
import java.util.stream.Collectors;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

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

public final class HelpCommand implements IJellyFishCommand
{
   public static final String COMMAND_NAME = "help";

   private static final IUsage COMMAND_USAGE = new DefaultUsage("Prints this help", new DefaultParameter("verbose", false), new DefaultParameter("command", false));

   private ILogService logService;

   private TreeMap<String, IJellyFishCommand> commands = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

   private StringTable<IJellyFishCommand> table = new StringTable<>(new IJellyFishCommandFormat());

   private final ITableFormat<IJellyFishCommand> normalFormat = new IJellyFishCommandFormat();
   private final ITableFormat<IJellyFishCommand> verboseFormat = new IJellyFishCommandVerboseFormat();

   public HelpCommand() {
      table.setShowHeader(false);
   }
   
   @Override
   public String getName()
   {
      return COMMAND_NAME;
   }

   @Override
   public IUsage getUsage()
   {
      return COMMAND_USAGE;
   }

   @Override
   public void run(IJellyFishCommandOptions commandOptions)
   {
      Preconditions.checkNotNull(commandOptions);
      IParameter verboseParameter = commandOptions.getParameters().getParameter("verbose");

      final boolean verbose;
      if (verboseParameter == null) {
         verbose = false;
      }
      else {
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
      
      if (commandParameter == null) {
         printHelp(verbose);
      }
      else {
         printCommandHelp(verbose, commandParameter.getValue());
      }
   }

   private void printHelp(boolean verbose)
   {
      table.setTableFormat(verbose ? verboseFormat : normalFormat);
      System.out.println("Usage: jellyfish command [-DinputDir=dir] [-Doption1=value1 ...]");
      System.out.println();
      System.out.println("Commands:");
      System.out.println();
      System.out.println(table);
   }

   private void printCommandHelp(boolean verbose, String name)
   {
      IJellyFishCommand command = commands.get(name);
      if (command == null) {
         System.out.println(name + " command not found");
      }
      else {
         String parameterUsage = " " + command.getUsage().getAllParameters().stream().map(p -> (p.isRequired() ? "" : "[") + "-D" + p.getName() + "=value" + (p.isRequired() ? "" : "]")).collect(Collectors.joining(" "));
         System.out.printf("Usage: jellyfish %s [-DinputDir=dir]%s%n", name, parameterUsage);
         System.out.println();
         System.out.println(command.getUsage().getDescription());
      }
   }

   public void addCommand(IJellyFishCommand command)
   {
      Preconditions.checkNotNull(command);
      IJellyFishCommand previous = commands.put(command.getName(), command);
      if (previous != null) {
         table.getModel().removeItem(previous);
      }
      table.getModel().addItem(command);
   }

   public void removeCommand(IJellyFishCommand command)
   {
      Preconditions.checkNotNull(command);
      commands.remove(command.getName());
      table.getModel().removeItem(command);
   }

   @Activate
   public void activate()
   {
      logService.trace(getClass(), "Activated");
   }

   @Deactivate
   public void deactivate()
   {
      logService.trace(getClass(), "Deactivated");
   }

   /**
    * Sets log service.
    *
    * @param ref the ref
    */
   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "removeLogService")
   @Inject
   public void setLogService(ILogService ref)
   {
      this.logService = ref;
   }

   /**
    * Remove log service.
    */
   public void removeLogService(ILogService ref)
   {
      setLogService(null);
   }

   private class IJellyFishCommandFormat implements ITableFormat<IJellyFishCommand>
   {

      @Override
      public int getColumnCount()
      {
         return 2;
      }

      @Override
      public String getColumnName(int column)
      {
         switch (column) {
         case 0:
            return "Name";
         case 1:
            return "Description";
         default:
            throw new IndexOutOfBoundsException("Invalid column: " + column);
         }
      }

      @Override
      public ITableFormat.ColumnSizePolicy getColumnSizePolicy(int column)
      {
         switch (column) {
         case 0:
            return ITableFormat.ColumnSizePolicy.MAX;
         case 1:
            return ITableFormat.ColumnSizePolicy.FIXED;
         default:
            throw new IndexOutOfBoundsException("Invalid column: " + column);
         }
      }

      @Override
      public int getColumnWidth(int column)
      {
         int nameSize = commands.keySet().stream().mapToInt(String::length).max().orElse(0);
         switch (column) {
         case 0:
            return 0;
         case 1:
            return 80 - nameSize;
         default:
            throw new IndexOutOfBoundsException("Invalid column: " + column);
         }
      }

      @Override
      public Object getColumnValue(IJellyFishCommand object, int column)
      {
         switch (column) {
         case 0:
            return object.getName();
         case 1:
            return object.getUsage().getDescription();
         default:
            throw new IndexOutOfBoundsException("Invalid column: " + column);
         }
      }

   }

   private class IJellyFishCommandVerboseFormat implements ITableFormat<IJellyFishCommand>
   {

      @Override
      public int getColumnCount()
      {
         return 3;
      }

      @Override
      public String getColumnName(int column)
      {
         switch (column) {
         case 0:
            return "Name";
         case 1:
            return "Description";
         case 2:
            return "Parameters";
         default:
            throw new IndexOutOfBoundsException("Invalid column: " + column);
         }
      }

      @Override
      public ITableFormat.ColumnSizePolicy getColumnSizePolicy(int column)
      {
         switch (column) {
         case 0:
            return ITableFormat.ColumnSizePolicy.MAX;
         case 1:
            return ITableFormat.ColumnSizePolicy.FIXED;
         case 2:
            return ITableFormat.ColumnSizePolicy.FIXED;
         default:
            throw new IndexOutOfBoundsException("Invalid column: " + column);
         }
      }

      @Override
      public int getColumnWidth(int column)
      {
         int nameSize = commands.keySet().stream().mapToInt(String::length).max().orElse(0);
         switch (column) {
         case 0:
            return 0;
         case 1:
            return 40 - nameSize;
         case 2:
            return 40 - nameSize;
         default:
            throw new IndexOutOfBoundsException("Invalid column: " + column);
         }
      }

      @Override
      public Object getColumnValue(IJellyFishCommand object, int column)
      {
         switch (column) {
         case 0:
            return object.getName();
         case 1:
            return object.getUsage().getDescription();
         case 2:
            String parameters = object.getUsage().getAllParameters().stream().map(p -> p.isRequired() ? p.getName() : ('[' + p.getName() + ']')).collect(Collectors.joining(", "));
            return parameters;
         default:
            throw new IndexOutOfBoundsException("Invalid column: " + column);
         }
      }

   }

}
