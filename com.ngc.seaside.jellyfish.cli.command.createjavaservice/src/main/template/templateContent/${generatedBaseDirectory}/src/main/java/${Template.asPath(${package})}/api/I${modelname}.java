package ${package}.api;

import com.ngc.seaside.service.fault.api.ServiceFaultException;

#foreach($field in $modelObject.getInputs())
import ${modelObject.getParent().getName()}.${modelObject.getName().toLowerCase()}.events.${field.getType().getName()};
#end
#foreach($field in $modelObject.getOutputs())
import ${modelObject.getParent().getName()}.${modelObject.getName().toLowerCase()}.events.${field.getType().getName()};
#end


public interface I${modelname} {
##start processing each scenario
#foreach ( $iScenario in ${modelObject.getScenarios()} )
#set ( $inputList = [] )
##start processing each when
#foreach ( $iScenarioStep in $iScenario.getWhens() )
#if ( $iScenarioStep.getKeyword() == "receiving" )
#foreach ( $param in $iScenarioStep.getParameters() )
#set ($dontCare = $inputList.add($param))
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
#if (! $inputList.isEmpty() )
#set ($inputSize = $inputList.size() - 1)
#set ($outputType = $modelObject.getOutputs().getByName($output).get().getType().getName())
#set ($inputTypeList = [])
#foreach ($input in $inputList)
#set ($dontCare = $inputTypeList.add($modelObject.getInputs().getByName($input).get().getType().getName()))
#end
   $outputType $iScenario.getName() (#foreach ($input in $inputList)#set ($index = $inputList.indexOf($input))$inputTypeList.get($index)#if ($index < $inputSize) $input,#else $input#end#end) throws ServiceFaultException;
#end

#end
##end processing scenarios
}
