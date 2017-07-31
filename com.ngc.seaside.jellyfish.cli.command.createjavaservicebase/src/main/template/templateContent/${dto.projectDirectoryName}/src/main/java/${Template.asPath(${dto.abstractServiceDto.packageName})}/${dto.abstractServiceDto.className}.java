package ${dto.abstractServiceDto.packageName};

import com.google.common.base.Preconditions;

import com.ngc.seaside.service.fault.api.ServiceFaultException;
import com.ngc.blocs.api.IContext;
import com.ngc.blocs.api.IStatus;
import com.ngc.blocs.service.api.IServiceModule;
import com.ngc.blocs.service.api.ServiceStatus;
import com.ngc.blocs.service.event.api.IEvent;
import com.ngc.blocs.service.event.api.IEventService;
import com.ngc.blocs.service.event.api.Subscriber;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.service.fault.api.IFaultManagementService;
import com.ngc.seaside.service.fault.api.ServiceFaultException;

#foreach ($i in $dto.abstractServiceDto.imports)
import ${i};
#end

public abstract class ${dto.abstractServiceDto.className} {

   public final static String NAME = "service:${dto.abstractServiceDto.modelName}";

   protected IContext context;

   protected ServiceStatus status = ServiceStatus.DEACTIVATED;

   protected IEventService eventService;

   protected ILogService logService;

   protected IFaultManagementService faultManagementService;

#foreach($method in $dto.receivingMethods)
   @Subscriber(${method.arguments.get(0).argumentClassName}.TOPIC_NAME)
   public void ${method.methodName}(${method.arguments.get(0).argumentClassName} ${method.arguments.get(0).argumentName}) {
      Preconditions.checkNotNull(${method.arguments.get(0).argumentName}, "${method.arguments.get(0).argumentName} may not be null!");
      try {
         //publishTrackPriority(calculateTrackPriority(event.getSource()));
      } catch (ServiceFaultException fault) {
         logService.error(
         getClass(),
         "Invocation of '%s.${method.methodName}(${method.arguments.get(0).argumentClassName})' generated fault, dispatching to fault management service.",
         getClass().getName());
         faultManagementService.handleFault(fault);
         // Consume exception.
      }
   }
#end

   @Override
   public String getName() {
      return NAME;
   }

   @Override
   public IContext getContext() {
      return context;
   }

   @Override
   public void setContext(IContext iContext) {
      this.context = iContext;
   }

   @Override
   public IStatus<ServiceStatus> getStatus() {
      return status;
   }

   @Override
   public boolean setStatus(IStatus<ServiceStatus> iStatus) {
      Preconditions.checkNotNull(iStatus, "iStatus may not be null!");
      this.status = iStatus.getStatus();
      return true;
   }

   protected void activate() {
      eventService.addSubscriber(this);
      setStatus(ServiceStatus.ACTIVATED);
      logService.info(getClass(), "activated");
   }

   protected void deactivate() {
      eventService.removeSubscriber(this);
      setStatus(ServiceStatus.DEACTIVATED);
      logService.info(getClass(), "deactivated");
   }

   public void setLogService(ILogService ref) {
      this.logService = ref;
   }

   public void removeLogService(ILogService ref) {
      setLogService(null);
   }

   public void setEventService(IEventService ref) {
      this.eventService = ref;
   }

   public void removeEventService(IEventService ref) {
      setEventService(null);
   }

   public void setFaultManagementService(IFaultManagementService ref) {
      this.faultManagementService = ref;
   }

   public void removeFaultManagementService(IFaultManagementService ref) {
      setFaultManagementService(null);
   }

#foreach($method in $dto.publishingMethods)
   private void ${method.methodName}(${method.arguments.get(0).argumentClassName} ${method.arguments.get(0).argumentName}) {
      Preconditions.checkNotNull(${method.arguments.get(0).argumentName}, "${method.arguments.get(0).argumentName} may not be null!");
      eventService.publish(${method.arguments.get(0).argumentName}, ${method.arguments.get(0).argumentClassName}.TOPIC);
   }
#end
}
