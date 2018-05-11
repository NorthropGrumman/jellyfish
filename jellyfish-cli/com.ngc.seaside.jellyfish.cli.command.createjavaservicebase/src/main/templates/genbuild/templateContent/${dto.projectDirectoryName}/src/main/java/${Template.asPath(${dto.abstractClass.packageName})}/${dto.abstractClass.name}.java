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
#if (!$dto.correlationMethods.isEmpty())
#set ($ignore = $dto.abstractClass.imports.add("java.util.function.Function"))
#set ($ignore = $dto.abstractClass.imports.add("java.util.stream.Collectors"))
#end
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
#if (!$dto.correlationMethods.isEmpty())
   protected Map<ICorrelationTrigger<?>, Function<ICorrelationStatus<?>, ?>> triggers =
         new ConcurrentHashMap<>();
   
#end
################################## Pub sub Delegaters ###################################
#foreach ($method in $dto.basicPubSubMethods)
   @Override
   public ${method.output.type} ${method.serviceMethod}(${method.input.type} ${method.input.fieldName}) throws ServiceFaultException {
      Preconditions.checkNotNull(${method.input.fieldName}, "'${method.input.fieldName}' may not be null!");
#foreach($correlation in $method.inputOutputCorrelations)
      updateRequestWithCorrelation(${method.input.fieldName}.${correlation.getterSnippet});
#end
#if ($method.isCorrelating())
      try {
#end
         ${method.output.type} output = ${method.name}(${method.input.fieldName});
#foreach($correlation in $method.inputOutputCorrelations)
		 output.${correlation.setterSnippet}(${method.input.fieldName}.${correlation.getterSnippet});
#end
		 return output;
#if ($method.isCorrelating())
      } finally {
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

########## Multi-input 1-output methods with input-input correlation Delegaters##########
#foreach ($method in $dto.correlationMethods)
#foreach ($corrInput in $method.inputs)
   @Override
   public ${method.output.finalizedType} ${method.serviceTryMethodSnippet}(${corrInput.type} ${corrInput.fieldName}) throws ServiceFaultException {
      Preconditions.checkNotNull(${corrInput.fieldName}, "${corrInput.fieldName} may not be null!");
      return correlationService.correlate(${corrInput.fieldName})
            .stream()
            .filter(ICorrelationStatus::isCorrelationComplete)
            .map(status -> triggers.get(status.getTrigger()).apply(status))
            .map(${method.output.type}.class::cast)
            .collect(Collectors.toList());
   }

#end
#end
################################## Pub sub methods ###################################
#foreach ($method in $dto.basicPubSubMethods)
   protected abstract ${method.output.type} ${method.name}(${method.input.type} ${method.input.fieldName}) throws ServiceFaultException;

#end
################################## Request Response ##################################
#foreach($method in $dto.basicServerReqResMethods)
   protected abstract ${method.output.type} ${method.name}(${method.input.type} ${method.input.fieldName}) throws ServiceFaultException;

#end
################################## Multi-input 1-output methods with input-input correlation ##################################
#foreach($method in $dto.correlationMethods)
   protected abstract ${method.output.type} ${method.name}(
#foreach ($input in $method.inputs)
#if( $foreach.count < $method.inputs.size() )
      ${input.type} ${input.fieldName},
#else
      ${input.type} ${input.fieldName});
#end
#end

#end
############################### Sink methods ##################################
#foreach($method in $dto.basicSinkMethods)
   protected abstract ${method.output.type} ${method.name}(${method.input.type} ${method.input.fieldName}) throws ServiceFaultException;

#end
################################## Activate ###################################
   protected void activate() {
#foreach($method in $dto.correlationMethods)
      ${method.serviceRegisterSnippet};
#end
      setStatus(ServiceStatus.ACTIVATED);
      logService.info(getClass(), "activated");
   }

################################# Deactivate ##################################
   protected void deactivate() {
#if (!$dto.correlationMethods.isEmpty())
      triggers.keySet().forEach(ICorrelationTrigger::unregister);
      triggers.clear();   
#end
      setStatus(ServiceStatus.DEACTIVATED);
      logService.info(getClass(), "deactivated");
   }

################################# Correlation Triggers ########################
#foreach($method in $dto.correlationMethods)
   private void ${method.serviceRegisterSnippet} {
      ICorrelationTrigger<${method.correlationType}> trigger = correlationService.newTrigger(${method.correlationType}.class)
#foreach ($input in $method.inputs)
         .addEventIdProducer(${input.type}.class, a -> a.getHeader().getCorrelationEventId())
#end
         .addCompletenessCondition(${method.inputClassListSnippet} (a, b) ->
               Objects.equal(a.getHeader().getCorrelationEventId(), b.getHeader().getCorrelationEventId()))
         .register();
      triggers.put(trigger, this::${method.serviceFromStatusSnippet});
   }
#end

############################ Correlation Status Methods ########################
#foreach($method in $dto.correlationMethods)
   private ${method.output.type} ${method.serviceFromStatusSnippet}(ICorrelationStatus<?> status) {
      updateRequestWithCorrelation(status.getEvent());
      try {
         ${method.output.type} output = ${method.name}(
#foreach ($input in $method.inputs)
#if( $foreach.count < $method.inputs.size() )               
                  status.getData(${input.type}.class),
#else
                  status.getData(${input.type}.class));
#end
#end
         output.getHeader().setCorrelationEventId(status.getData(${method.inputs.get(0).type}.class)
                                                     .getHeader()
                                                     .getCorrelationEventId());
         return output;
      } finally {
         clearCorrelationFromRequest();
      }
   }
#end

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
