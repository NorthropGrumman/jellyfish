package ${dto.abstractClass.packageName};

#set ($ignore = $dto.abstractClass.imports.add("com.google.common.base.Preconditions"))
#set ($ignore = $dto.abstractClass.imports.add("com.ngc.blocs.api.IContext"))
#set ($ignore = $dto.abstractClass.imports.add("com.ngc.blocs.api.IStatus"))
#set ($ignore = $dto.abstractClass.imports.add("com.ngc.blocs.service.api.IServiceModule"))
#set ($ignore = $dto.abstractClass.imports.add("com.ngc.blocs.service.api.ServiceStatus"))
#set ($ignore = $dto.abstractClass.imports.add("com.ngc.blocs.service.event.api.IEventService"))
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

   protected IEventService eventService;

   protected ILogService logService;

   protected IFaultManagementService faultManagementService;

   protected IThreadService threadService;

#if ($dto.correlationServiceRequired)
   protected ICorrelationService correlationService;

#end
#if (!$dto.correlationMethods.isEmpty())
   protected Map<ICorrelationTrigger<?>, Collection<Consumer<ICorrelationStatus<?>>>> triggers = new ConcurrentHashMap<>();

#end
#if (!$dto.complexScenarios.isEmpty())
   private Map<Class<?>, Collection<Queue<?>>> queues = new ConcurrentHashMap<>();

   private Map<String, ISubmittedLongLivingTask> threads = new ConcurrentHashMap<>();

#end
############################### Receive methods ###############################
#foreach($method in $dto.receiveMethods)
   @Subscriber(${method.topic})
   public void ${method.name}(IEvent<${method.eventType}> event) {
      Preconditions.checkNotNull(event, "event may not be null!");
      ${method.eventType} source = Preconditions.checkNotNull(event.getSource(), "event source may not be null!");

#foreach($scenario in $method.basicScenarios)
      ${scenario}(source);
#end
#if ($method.hasCorrelations())
      correlationService.correlate(source)
         .stream()
         .filter(ICorrelationStatus::isCorrelationComplete)
         .forEach(status -> {
            triggers.get(status.getTrigger()).forEach(consumer -> consumer.accept(status));
         });
#end
#if (!$dto.complexScenarios.isEmpty())
      queues.getOrDefault(${method.eventType}.class, Collections.emptyList()).forEach(queue -> {
         @SuppressWarnings("unchecked")
         Collection<${method.eventType}> q = (Collection<${method.eventType}>) queue;
         q.add(source);
      });
#end
   }

#end
############################### Publish methods ###############################
#foreach($method in $dto.publishMethods)
   private void ${method.name}(${method.type} value) {
      Preconditions.checkNotNull(value, "${method.type} value may not be null!");
      eventService.publish(value, ${method.topic});
   }

#end
#################### Basic 1-input 1-output pubsub methods ####################
#foreach($method in $dto.basicPubSubMethods)
   private void ${method.name}(${method.input.type} input) {
#foreach($correlation in $method.inputOutputCorrelations)
      updateRequestWithCorrelation(input.${correlation.getterSnippet});
#end
      try {
         ${method.output.type} output = ${method.serviceMethod}(input);
#foreach($correlation in $method.inputOutputCorrelations)
         output.${correlation.setterSnippet}(input.${correlation.getterSnippet});
#end
         logService.info(getClass(), "ELK - Scenario: ${method.scenarioName}; Input: %s; Output: %s;", input, output);
         ${method.output.name}(output);
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
##################### Basic 1-input 0-output sink methods #####################
#foreach($method in $dto.basicSinkMethods)
   private void ${method.name}(${method.input.type} input) {
      try {
         ${method.serviceMethod}(input);
         logService.info(getClass(), "ELK - Scenario: ${method.scenarioName}; Input: %s; Output: ;", input);
      } catch(ServiceFaultException fault) {
         logService.error(getClass(),
            "Invocation of '${dto.abstractClass.name}.${method.serviceMethod}' generated a fault, dispatching to fault management service.");
         faultManagementService.handleFault(fault);
      }
   }

#end
########## Multi-input 1-output methods with input-input correlation ##########
#foreach($method in $dto.correlationMethods)
   private void ${method.name}(ICorrelationStatus<?> status) {
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
         logService.info(getClass(), "ELK - Scenario: ${method.scenarioName}; Input: ${method.inputLogFormat}; Output: %s;",
#foreach($input in $method.inputs)
            status.getData(${input.type}.class).toString(),
#end
            output.toString());
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
############################ Trigger Registrations ############################
#foreach($method in $dto.triggerRegistrationMethods)
   private void ${method.name}() {
      ICorrelationTrigger<${method.triggerType}> trigger = correlationService.newTrigger(${method.triggerType}.class)
#foreach($eventDto in $method.eventProducers)
            .addEventIdProducer(${eventDto.type}.class, a -> a.${eventDto.getterSnippet})
#end
#foreach($completenessDto in $method.completionStatements)
            .addCompletenessCondition(${completenessDto.input1Type}.class, ${completenessDto.input2Type}.class, (a, b) ->
               Objects.equal(a.${completenessDto.input1GetterSnippet}, b.${completenessDto.input2GetterSnippet}))
#end
            .register();
      triggers.computeIfAbsent(trigger, __ -> new ArrayList<>()).add(this::${method.correlationMethod});
   }

#end
############ Multi-input multi-output methods without correlation #############
#foreach($scenario in $dto.complexScenarios)
#set ($serviceArguments = "")
#foreach($input in $scenario.inputs)
#set ($serviceArguments = "${serviceArguments}, input${foreach.count}Queue")
#end
#foreach($output in $scenario.outputs)
#set ($serviceArguments = "${serviceArguments}, ${dto.abstractClass.name}.this::${output.name}")
#end
#set ($serviceArguments = $serviceArguments.substring(2))
   private void ${scenario.startMethod}() {
      threads.put("${scenario.name}", threadService.executeLongLivingTask("${scenario.name}", () -> {
#foreach($input in $scenario.inputs)
         final BlockingQueue<${input.type}> input${foreach.count}Queue = new LinkedBlockingQueue<>();
#end
#foreach($input in $scenario.inputs)
         queues.computeIfAbsent(${input.type}.class, __ -> Collections.newSetFromMap(new IdentityHashMap<>())).add(input${foreach.count}Queue);
#end
         try {
            ${scenario.serviceMethod}(${serviceArguments});
         } finally {
            threads.remove("${scenario.name}");
#foreach($input in $scenario.inputs)
            queues.getOrDefault(${input.type}.class, Collections.emptySet()).remove(input${foreach.count}Queue);
#end
         }
      }));
   }
#end
################################## Activate ###################################
   protected void activate() {
#foreach($method in $dto.triggerRegistrationMethods)
      ${method.name}();
#end
#foreach($scenario in $dto.complexScenarios)
      ${scenario.startMethod}();
#end
#if (!$dto.receiveMethods.isEmpty())
      eventService.addSubscriber(this);
#end
      setStatus(ServiceStatus.ACTIVATED);
      logService.info(getClass(), "activated");
   }

################################# Deactivate ##################################
   protected void deactivate() {
#if (!$dto.receiveMethods.isEmpty())
      eventService.removeSubscriber(this);
#end
#if (!$dto.correlationMethods.isEmpty())
      triggers.keySet().forEach(ICorrelationTrigger::unregister);
      triggers.clear();
#end
#if (!$dto.complexScenarios.isEmpty())
      queues.clear();
      threads.values().forEach(ISubmittedLongLivingTask::cancel);
      threads.clear();
#end
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

   public void setEventService(IEventService ref) {
      this.eventService = ref;
   }

   public void removeEventService(IEventService ref) {
      setEventService(null);
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
      if (request instanceof ServiceRequest) {
         ((ServiceRequest) request).setLocalCorrelationEvent(event);
      }
   }
#end
#if ($dto.correlationRequestHandlingEnabled)

   @SuppressWarnings("unchecked")
   private void updateRequestWithCorrelation(Object correlationEventId) {
      IRequest request = Requests.getCurrentRequest();
      if (request instanceof ServiceRequest) {
         ((ServiceRequest) request).setLocalCorrelationEvent(
            correlationService.newLocalCorrelationEvent(correlationEventId));
      }
   }
#end
#if (!$dto.correlationMethods.isEmpty() || $dto.correlationRequestHandlingEnabled)

   @SuppressWarnings("rawtypes")
   private void clearCorrelationFromRequest() {
      IRequest request = Requests.getCurrentRequest();
      if (request instanceof ServiceRequest) {
         ((ServiceRequest) request).clearLocalCorrelationEvent();
      }
   }
#end
}
