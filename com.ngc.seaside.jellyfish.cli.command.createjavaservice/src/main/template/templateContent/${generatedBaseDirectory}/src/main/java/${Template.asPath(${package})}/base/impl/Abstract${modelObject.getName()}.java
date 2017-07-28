package ${package}.base.impl;

##start processing each scenario
#set ( $modelMethodList = [])
#set ( $modelScenarioList = [])
#foreach ( $iScenario in ${modelObject.getScenarios()} )
#set ($dontCare = $modelScenarioList.add($iScenario))
#set ( $modelReceiveList = [] )
##start processing each when
#foreach ( $iScenarioStep in $iScenario.getWhens() )
#if ( $iScenarioStep.getKeyword() == "receiving" )
#foreach ( $param in $iScenarioStep.getParameters() )
#set ($dontCare = $modelReceiveList.add($param))
#end
#end
#end
##end processing each when
##start processing each then
#foreach ( $iScenarioStep in $iScenario.getThens() )
#if ( $iScenarioStep.getKeyword() == "willPublish" )
#set ( $output = $iScenarioStep.getParameters().get(0))
#end
#end
##end processing then
#if (! $modelReceiveList.isEmpty() )
#set ($outputType = $modelObject.getOutputs().getByName($output).get().getType().getName())
#set ($modelReceiveTypeList = [])
#foreach ($input in $modelReceiveList)
#set ($dontCare = $modelReceiveTypeList.add($modelObject.getInputs().getByName($input).get().getType().getName()))
#end
#set ($dontCare = $modelMethodList.add("$outputType $iScenario.getName() (#foreach ($input in $modelReceiveList)#set ($index = $modelReceiveList.indexOf($input))$modelReceiveTypeList.get($index) $input#if( $velocityHasNext ),#end#end) throws ServiceFaultException"))
#end
#end
##end processing scenarios
import com.google.common.base.Preconditions;

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
import ${package}.api.I$modelObject.getName();
#foreach($field in $modelObject.getInputs())
import ${package}.events.${field.getType().getName()};
#end
#foreach($field in $modelObject.getOutputs())
import ${package}.events.${field.getType().getName()};
#end
#set ($DOT = ".")

/**
 * Base class for $modelObject.getName().replaceAll("((^[a-z]+)|([A-Z]{1}[a-z]+)|([A-Z]+(?=([A-Z][a-z])|($))))", "$1 ").toLowerCase() implementations.
 */
public abstract class Abstract${modelObject.getName()} implements IServiceModule, I$modelObject.getName() {

   public final static String NAME = "service:${model}";

   /**
    * BLoCS boilerplate.
    */
   protected IContext context;
   /**
    * BLoCS boilerplate.
    */
   protected ServiceStatus status = ServiceStatus.DEACTIVATED;

   protected IEventService eventService;

   protected ILogService logService;

   protected IFaultManagementService faultManagementService;

#foreach ($modelReceiver in $modelReceiveList)
#set ( $index = $modelReceiveList.indexOf($modelReceiver))
   @Subscriber(${modelReceiveTypeList.get($index)}${DOT}TOPIC_NAME)
   public void receive$modelReceiveTypeList.get($index)(IEvent<$modelReceiveTypeList.get($index)> event) {
      Preconditions.checkNotNull(event, "event may not be null!");
      try {
         publish$outputType($modelScenarioList.get($modelReceiveList.indexOf($modelReceiver)).getName()(event.getSource()));
      } catch (ServiceFaultException fault) {
         logService.error(
               getClass(),
               "Invocation of '%s.calculate$outputType($modelReceiveTypeList.get($index))' generated fault, dispatching to fault"
               + " management service.",
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
#set ($outputTypeLowercase = "$outputType.substring(0, 1).toLowerCase()$outputType.substring(1)")

   /**
    * Publishes the given {@code $outputType} object.
    *
    * @param $outputTypeLowercase the {@code $outputType} to publish
    */
   private void publish$outputType($outputType $outputTypeLowercase) {
      Preconditions.checkNotNull($outputTypeLowercase, "$outputTypeLowercase may not be null!");
      eventService.publish($outputTypeLowercase, ${outputType}${DOT}TOPIC);
   }
}
