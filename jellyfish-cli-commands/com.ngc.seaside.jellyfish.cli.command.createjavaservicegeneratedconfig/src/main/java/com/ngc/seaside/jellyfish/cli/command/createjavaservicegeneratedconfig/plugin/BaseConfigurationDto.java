package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin;

import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.utils.DefaultImportManager;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.utils.DefaultVariableManager;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.utils.IImportManager;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.utils.IVariableManager;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

public abstract class BaseConfigurationDto {

   private final ConfigurationContext context;
   private final IImportManager imports;
   private final IVariableManager variables;

   /**
    * Constructor.
    * 
    * @param context context
    */
   public BaseConfigurationDto(ConfigurationContext context) {
      this.context = context;
      this.imports = new DefaultImportManager();
      this.variables = new DefaultVariableManager();
   }

   public String getProjectDirectoryName() {
      return context.getProjectInformation().getDirectoryName();
   }

   public String getBasePackage() {
      return context.getBasePackage();
   }

   public IModel getModel() {
      return context.getModel();
   }

   public ConfigurationContext getContext() {
      return context;
   }

   public IImportManager getImports() {
      return imports;
   }

   public IVariableManager getVariables() {
      return variables;
   }

}
