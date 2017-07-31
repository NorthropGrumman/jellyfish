package ${dto.packageName};

import com.ngc.blocs.service.api.IServiceModule;
import com.ngc.blocs.service.event.api.IEventService;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.service.fault.api.IFaultManagementService;
import com.ngc.seaside.service.fault.api.ServiceFaultException;
import com.ngc.seaside.service.fault.api.ServiceInputFaultException;

#foreach ($i in $dto.imports)
import ${i};
#end

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

@Component(service = ${dto.serviceInterfaceDto.interfaceName}.class, IServiceModule.class}, immediate = true)
public class ${dto.className} extends ${dto.baseClassName} {

#foreach ($method in $dto.methods)
#if( ${method.override} )
   @Override
#end
   public ${method.returnSnippet} ${method.methodName}(${method.argumentsListSnippet}) {
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
   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC)
   public void setLogService(ILogService ref) {
      super.setLogService(ref);
   }

   @Override
   public void removeLogService(ILogService ref) {
      super.removeLogService(ref);
   }

   @Override
   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC)
   public void setEventService(IEventService ref) {
      super.setEventService(ref);
   }

   @Override
   public void removeEventService(IEventService ref) {
      super.removeEventService(ref);
   }

   @Override
   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC)
   public void setFaultManagementService(IFaultManagementService ref) {
      super.setFaultManagementService(ref);
   }

   @Override
   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC)
   public void removeFaultManagementService(IFaultManagementService ref) {
      super.removeFaultManagementService(ref);
   }
}
