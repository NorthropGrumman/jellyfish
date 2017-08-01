package com.ngc.seaside.jellyfish.cli.command.requirementsallocationmatrix;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultUsage;
import com.ngc.seaside.command.api.IUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

@Component(service = IJellyFishCommand.class)
public class RequirementsAllocationMatrixCommand implements IJellyFishCommand {

   private static final String NAME = "requirements-allocation-matrix";
   private static final IUsage USAGE = createUsage();

   public static final String SCOPE_PROPERTY = "scope";
   public static final String VALUES_PROPERTY = "values";

   private ILogService logService;

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

   /**
    * Create the usage for this command.
    *
    * @return the usage.
    */
   @SuppressWarnings("rawtypes")
   private static IUsage createUsage() {
      // TODO Auto-generated method stub
      return new DefaultUsage("Description of requirements-allocation-matrix command", 
    	         new DefaultParameter(SCOPE_PROPERTY).setDescription("Scope property").setRequired(false),
    	         new DefaultParameter(VALUES_PROPERTY).setDescription("Values property").setRequired(false));
   }

}
