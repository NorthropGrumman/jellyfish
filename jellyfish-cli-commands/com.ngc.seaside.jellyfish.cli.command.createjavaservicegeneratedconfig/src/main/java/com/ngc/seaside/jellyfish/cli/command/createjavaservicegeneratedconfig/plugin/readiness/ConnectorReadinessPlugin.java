package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.readiness;

import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.ConfigurationContext;
import com.ngc.seaside.jellyfish.service.name.api.IPackageNamingService;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import javax.inject.Inject;

public class ConnectorReadinessPlugin implements IReadinessPlugin {

   private IPackageNamingService packageNamingService;

   @Inject
   public ConnectorReadinessPlugin(IPackageNamingService packageNamingService) {
      this.packageNamingService = packageNamingService;
   }

   @Override
   public void configure(ReadinessTemplateDto dto) {
      ConfigurationContext context = dto.getContext();
      IModel model = context.getModel();
      String pkg = packageNamingService.getConnectorPackageName(context.getOptions(), model);
      String name = model.getName() + "Connector";
      dto.addComponent(pkg + '.' + name);
   }

}
