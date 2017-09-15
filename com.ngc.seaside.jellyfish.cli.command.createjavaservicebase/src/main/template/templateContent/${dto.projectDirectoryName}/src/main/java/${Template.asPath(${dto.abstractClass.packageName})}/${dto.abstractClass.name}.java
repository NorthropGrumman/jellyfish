package ${dto.abstractClass.packageName};

import com.google.common.base.Preconditions;
import java.util.Map;
import java.util.HashMap;
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
import com.ngc.blocs.service.thread.api.IThreadService;
import com.ngc.blocs.service.thread.api.ISubmittedLongLivingTask;

#foreach ($i in $dto.abstractClass.imports)
import ${i};
#end

public abstract class ${dto.abstractClass.name}
   implements IServiceModule, ${dto.interface.name} {

   public final static String NAME = "service:${dto.model.fullyQualifiedName}";

   protected IContext context;

   protected ServiceStatus status = ServiceStatus.DEACTIVATED;

   protected IEventService eventService;

   protected ILogService logService;

   protected IFaultManagementService faultManagementService;
   
   protected IThreadService threadService;
   
   protected Map<String, ISubmittedLongLivingTask> threads = new HashMap<>();

#foreach($method in $dto.abstractClass.methods)
#if (!$method.isPublisher())
#set ($argument = $method.arguments.get(0))
#set ($type = $argument.types.get(0).name)
   @Subscriber(${type}.TOPIC_NAME)
   public ${method.returnSnippet} ${method.name}(${method.argumentsListSnippet}) {
      Preconditions.checkNotNull(${argument.name}, "${argument.name} may not be null!");

#foreach ($entry in $method.publishMethods.entrySet())
#set ($scenarioName = $entry.key)
#set ($publishMethod = $entry.value)
      try {
#if ($method.flow.flowType == "PATH")
         ${publishMethod.name}(${scenarioName}(${argument.name}.getSource()));
#else
         ${scenarioName}(${argument.name}.getSource());
#end
      } catch (ServiceFaultException fault) {
         logService.error(getClass(),
            "Invocation of '%s.${scenarioName}(${type})' generated fault, dispatching to fault management service.",
            getClass().getName());
         faultManagementService.handleFault(fault);
         // Consume exception.
      }

#end
   }
#end
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
      
#foreach ($method in $dto.abstractClass.methods)
#if ($method.flow.flowType == "SOURCE")
#foreach ($entry in $method.publishMethods.entrySet())
#set ($scenarioName = $entry.key)
      ISubmittedLongLivingTask task = threadService.executeLongLivingTask("", () -> {
         try {
            ${scenarioName}(${dto.abstractClass.name}.this::${method.name});
         }  (ServiceFaultException fault) {
            logService.error(getClass(),
               "Invocation of '%s.${scenarioName}(Consumer<${method.arguments.get(0).name}>)' generated fault, dispatching to fault management service.",
               getClass().getName());
            faultManagementService.handleFault(fault);
            // Consume exception.
         }
      });
      threads.put("${scenarioName}::${method.name}", task);
#end
#end
#end
      setStatus(ServiceStatus.ACTIVATED);
      logService.info(getClass(), "activated");
   }

   protected void deactivate() {
      eventService.removeSubscriber(this);
      threads.values().forEach(ISubmittedLongLivingTask::cancel);
      threads.clear();
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

   public void setThreadService(IThreadService ref) {
      this.threadService = ref;
   }

   public void removeThreadService(IThreadService ref) {
      setThreadService(null);
   }

#foreach($method in $dto.abstractClass.methods)
#if ($method.isPublisher())
#set ($argument = $method.arguments.get(0))
   private ${method.returnSnippet} ${method.name}(${method.argumentsListSnippet}) {
      Preconditions.checkNotNull(${argument.name}, "${argument.name} may not be null!");
      eventService.publish(${argument.name}, ${method.publishingTopic});
   }
#end
#end
}
