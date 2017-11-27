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
   void ${method.outputType} ${method.serviceName}(${method.inputType} input) throws ServiceFaultException;

#end

#foreach ($method in $dto.correlationMethods)
   ${method.output.type} ${method.serviceName}(
#foreach ($input in $method.inputs)
      ${input.type} ${input.inputArgumentString},
#end
      ILocalCorrelationEvent<${method.correlationType}> correlationEvent) throws ServiceFaultException;
#end

}
