package ${package};

import com.ngc.blocs.service.api.IServiceModule;
import com.ngc.blocs.service.event.api.IEventService;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.service.fault.api.IFaultManagementService;
import com.ngc.seaside.service.fault.api.ServiceFaultException;
import com.ngc.seaside.service.fault.api.ServiceInputFaultException;
import ${package}.api.I${modelObject.getName()};
import ${package}.base.impl.Abstract${modelObject.getName()};
#foreach($field in $modelObject.getInputs())
import ${package}.events.${field.getType().getName()};
#end
#foreach($field in $modelObject.getOutputs())
import ${package}.events.${field.getType().getName()};
#end

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;


@Component(service = {I${modelObject.getName()}.class, IServiceModule.class}, immediate = true)
public class ${modelObject.getName()} extends Abstract${modelObject.getName()} {
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

#foreach ($method in $modelMethodList)
    @Override
    public $method {
         // TODO Auto-generated method stub
         throw new UnsupportedOperationException();
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
