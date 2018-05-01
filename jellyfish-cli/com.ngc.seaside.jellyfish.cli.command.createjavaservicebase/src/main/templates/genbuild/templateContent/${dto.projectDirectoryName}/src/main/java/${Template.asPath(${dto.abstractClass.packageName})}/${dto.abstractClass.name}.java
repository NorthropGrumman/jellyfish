package ${dto.abstractClass.packageName};

#set ($ignore = $dto.abstractClass.imports.add("com.google.common.base.Preconditions"))
#set ($ignore = $dto.abstractClass.imports.add("com.ngc.blocs.api.IContext"))
#set ($ignore = $dto.abstractClass.imports.add("com.ngc.blocs.api.IStatus"))
#set ($ignore = $dto.abstractClass.imports.add("com.ngc.blocs.service.api.IServiceModule"))
#set ($ignore = $dto.abstractClass.imports.add("com.ngc.blocs.service.api.ServiceStatus"))
#set ($ignore = $dto.abstractClass.imports.add("com.ngc.blocs.service.event.api.Subscriber"))
#set ($ignore = $dto.abstractClass.imports.add("com.ngc.blocs.service.log.api.ILogService"))
#set ($ignore = $dto.abstractClass.imports.add("com.ngc.seaside.service.fault.api.IFaultManagementService"))
#set ($ignore = $dto.abstractClass.imports.add("com.ngc.seaside.service.fault.api.ServiceFaultException"))
#set ($ignore = $dto.abstractClass.imports.add("com.ngc.blocs.service.thread.api.IThreadService"))
#foreach ($i in $dto.abstractClass.imports)
import ${i};
#end

public abstract class ${dto.abstractClass.name}
   implements IServiceModule, ${dto.interface.name} {

   public final static String NAME = "service:${dto.model.fullyQualifiedName}";

   protected IContext<?> context;

   protected ServiceStatus status = ServiceStatus.DEACTIVATED;

   protected ILogService logService;

   protected IFaultManagementService faultManagementService;

   protected IThreadService threadService;

#if ($dto.correlationServiceRequired)
   protected ICorrelationService correlationService;

#end
################################## Pub sub Delegaters ###################################
#foreach ($method in $dto.basicPubSubMethods)
   @Override
   public ${method.output.type} ${method.serviceMethod}(${method.input.type} ${method.input.fieldName}) throws ServiceFaultException {
      Preconditions.checkNotNull(${method.input.fieldName}, "'${method.input.fieldName}' may not be null!");
#foreach($correlation in $method.inputOutputCorrelations)
      updateRequestWithCorrelation(input.${correlation.getterSnippet});
#end
      try {
         ${method.output.type} output = ${method.name}(${method.input.fieldName});
#foreach($correlation in $method.inputOutputCorrelations)
		 output.${correlation.setterSnippet}(input.${correlation.getterSnippet});
#end
		 return output;
	  } catch(ServiceFaultException fault) {
        logService.error(getClass(),
           "Invocation of '${dto.abstractClass.name}.${method.serviceMethod}' generated a fault, dispatching to fault management service.");
        faultManagementService.handleFault(fault);
      }
#if ($method.isCorrelating())
		  finally {
		     clearCorrelationFromRequest();
		  }
#end
   }

#end
################################## Request Response Delegaters ###################################
#foreach ($method in $dto.basicServerReqResMethods)
   @Override
   public ${method.output.type} ${method.serviceMethod}(${method.input.type} ${method.input.fieldName}) throws ServiceFaultException {
      Preconditions.checkNotNull(${method.input.fieldName}, "'${method.input.fieldName}' may not be null!");
      ${method.output.type} response = ${method.name}(${method.input.fieldName});
      return response;
   }

#end
################################## Pub sub methods ###################################
#foreach ($method in $dto.basicPubSubMethods)
   protected abstract ${method.output.type} ${method.name}(${method.input.type} ${method.input.fieldName}) throws ServiceFaultException;

#end
################################## Request Response ###################################
#foreach($method in $dto.basicServerReqResMethods)
   protected abstract ${method.output.type} ${method.name}(${method.input.type} ${method.input.fieldName}) throws ServiceFaultException;

#end
############################### Sink methods ###############################
#foreach($method in $dto.basicSinkMethods)
   protected abstract ${method.output.type} ${method.name}(${method.input.type} ${method.input.fieldName}) throws ServiceFaultException;

#end
########## Multi-input 1-output methods with input-input correlation ##########
#foreach($method in $dto.correlationMethods)
   @Override
   public void ${method.name}(ICorrelationStatus<?> status) {
      updateRequestWithCorrelation(status.getEvent());
      try {
         @SuppressWarnings("unchecked")
         ${method.output.type} output = ${method.serviceMethod}(
#foreach($input in $method.inputs)
               status.getData(${input.type}.class),
#end
               (ILocalCorrelationEvent<${method.correlationType}>) status.getEvent());
#foreach($correlation in $method.inputOutputCorrelations)
         output.${correlation.setterSnippet}(status.getData(${correlation.inputType}.class).${correlation.getterSnippet});
#end
         ${method.output.name}(output);
      } catch (ServiceFaultException fault) {
         logService.error(getClass(),
                  "Invocation of '${dto.abstractClass.name}.${method.serviceMethod}' generated a fault, dispatching to fault management service.");
         faultManagementService.handleFault(fault);
      } finally {
         clearCorrelationFromRequest();
      }
   }

#end
################################## Activate ###################################
   protected void activate() {
      setStatus(ServiceStatus.ACTIVATED);
      logService.info(getClass(), "activated");
   }

################################# Deactivate ##################################
   protected void deactivate() {
      setStatus(ServiceStatus.DEACTIVATED);
      logService.info(getClass(), "deactivated");
   }

   @Override
   public String getName() {
      return NAME;
   }

   @Override
   public IContext<?> getContext() {
      return context;
   }

   @Override
   public void setContext(@SuppressWarnings("rawtypes") IContext context) {
      this.context = context;
   }

   @Override
   public IStatus<ServiceStatus> getStatus() {
      return status;
   }

   @Override
   public boolean setStatus(IStatus<ServiceStatus> status) {
      Preconditions.checkNotNull(status, "status may not be null!");
      this.status = status.getStatus();
      return true;
   }

   public void setLogService(ILogService ref) {
      this.logService = ref;
   }

   public void removeLogService(ILogService ref) {
      setLogService(null);
   }

#if ($dto.correlationServiceRequired)
   public void setCorrelationService(ICorrelationService ref) {
      this.correlationService = ref;
   }

   public void removeCorrelationService(ICorrelationService ref) {
      setCorrelationService(null);
   }

#end
   public void setFaultManagementService(IFaultManagementService ref) {
      this.faultManagementService = ref;
   }

   public void removeFaultManagementService(IFaultManagementService ref) {
      setFaultManagementService(null);
   }

   public void setThreadService(IThreadService ref) {
      this.threadService = ref;
   }

   public void removeThreadService(IThreadService ref) {
      setThreadService(null);
   }
#if (!$dto.correlationMethods.isEmpty())

   @SuppressWarnings({ "unchecked", "rawtypes" })
   private void updateRequestWithCorrelation(ILocalCorrelationEvent<?> event) {
      IRequest request = Requests.getCurrentRequest();
      if (request instanceof IServiceRequest) {
         ((IServiceRequest) request).setLocalCorrelationEvent(event);
      }
   }
#end
#if ($dto.correlationRequestHandlingEnabled)

   @SuppressWarnings("unchecked")
   private void updateRequestWithCorrelation(Object correlationEventId) {
      IRequest request = Requests.getCurrentRequest();
      if (request instanceof IServiceRequest) {
         ((IServiceRequest) request).setLocalCorrelationEvent(
            correlationService.newLocalCorrelationEvent(correlationEventId));
      }
   }
#end
#if (!$dto.correlationMethods.isEmpty() || $dto.correlationRequestHandlingEnabled)

   @SuppressWarnings("rawtypes")
   private void clearCorrelationFromRequest() {
      IRequest request = Requests.getCurrentRequest();
      if (request instanceof IServiceRequest) {
         ((IServiceRequest) request).setLocalCorrelationEvent(null);
      }
   }
#end
}
