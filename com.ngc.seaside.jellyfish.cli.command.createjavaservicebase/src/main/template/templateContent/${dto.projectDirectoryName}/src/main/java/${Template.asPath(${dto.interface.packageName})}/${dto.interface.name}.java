package ${dto.interface.packageName};

import com.ngc.seaside.service.fault.api.ServiceFaultException;
#foreach ($i in $dto.interface.imports)
import ${i};
#end

public interface ${dto.interface.name}#if ($dto.interface.implementedInterface) extends ${dto.interface.implementedInterface.name}#end {

#foreach ($method in $dto.basicPubSubMethods)
   ${method.outputType} ${method.serviceName}(${method.inputType} input) throws ServiceFaultException;
   
#end
#foreach ($method in $dto.basicSinkMethods)
   void ${method.serviceName}(${method.inputType} input) throws ServiceFaultException;

#end
#foreach ($method in $dto.correlationMethods)
   ${method.outputType} ${method.serviceName}(
#foreach ($input in $method.inputs)
      ${input.type} ${input.inputArgumentString},
#end
      ILocalCorrelationEvent<${method.correlationType}> correlationEvent) throws ServiceFaultException;

#end
#foreach ($scenario in $dto.complexScenarios)
   void ${scenario.serviceName}(
#foreach ($input in $scenario.inputs)
#set ($lastParam = $velocityCount == $scenario.inputs.size() && $scenario.outputs.isEmpty())
      BlockingQueue<${input.type}> input${velocityCount}Queue#if ($lastParam));#{else},#end
#end
#foreach ($output in $scenario.outputs)
#set ($lastParam = $velocityCount == $scenario.outputs.size())
      Consumer<${output.type}> output${velocityCount}Consumer#if ($lastParam));#{else},#end
#end

#end
}
