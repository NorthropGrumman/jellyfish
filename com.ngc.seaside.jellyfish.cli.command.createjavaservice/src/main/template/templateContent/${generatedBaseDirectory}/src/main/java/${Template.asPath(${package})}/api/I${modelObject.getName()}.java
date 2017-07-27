package ${package}.api;

import com.ngc.seaside.service.fault.api.ServiceFaultException;

#foreach($field in $modelObject.getInputs())
import ${modelObject.getParent().getName()}.${modelObject.getName().toLowerCase()}.events.${field.getType().getName()};
#end
#foreach($field in $modelObject.getOutputs())
import ${modelObject.getParent().getName()}.${modelObject.getName().toLowerCase()}.events.${field.getType().getName()};
#end


public interface I${modelObject.getName()} {
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
    $method;

#end
}
