package ${dto.service.packageName};

import com.ngc.blocs.service.api.IServiceModule;
import com.ngc.blocs.service.event.api.IEventService;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.service.fault.api.IFaultManagementService;

#foreach ($i in $dto.service.imports)
import ${i};
#end

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

@Component(service = {#if($dto.service.implementedInterface)${dto.service.implementedInterface.name}.class, #{end}IServiceModule.class}, immediate = true)
public class ${dto.service.name}#if($dto.service.baseClass) extends ${dto.service.baseClass.name}#end {

#foreach ($method in $dto.service.methods)
#if( ${method.override} )
   @Override
#end
   public ${method.returnSnippet} ${method.name}(${method.argumentsListSnippet}) {
      // TODO: implement this
      throw new UnsupportedOperationException("not implemented");
   }
#end

   @Activate
   public void activate() {
      super.activate();
   }

   @Deactivate
   public void deactivate() {
      super.deactivate();
   }

   @Override
   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "removeLogService")
   public void setLogService(ILogService ref) {
      super.setLogService(ref);
   }

   @Override
   public void removeLogService(ILogService ref) {
      super.removeLogService(ref);
   }

   @Override
   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "removeEventService")
   public void setEventService(IEventService ref) {
      super.setEventService(ref);
   }

   @Override
   public void removeEventService(IEventService ref) {
      super.removeEventService(ref);
   }

   @Override
   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "removeFaultManagementService")
   public void setFaultManagementService(IFaultManagementService ref) {
      super.setFaultManagementService(ref);
   }

   @Override
   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC)
   public void removeFaultManagementService(IFaultManagementService ref) {
      super.removeFaultManagementService(ref);
   }
}
