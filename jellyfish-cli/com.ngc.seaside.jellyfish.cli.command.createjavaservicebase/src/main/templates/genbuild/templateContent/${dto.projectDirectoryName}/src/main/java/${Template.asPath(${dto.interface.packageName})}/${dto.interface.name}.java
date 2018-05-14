package ${dto.interface.packageName};

#set ($ignore = $dto.interface.imports.add("com.ngc.seaside.service.fault.api.ServiceFaultException"))
#if (!$dto.correlationMethods.isEmpty())
#set ($ignore = $dto.interface.imports.add("com.ngc.seaside.service.correlation.api.ICorrelationStatus"))
#set ($ignore = $dto.interface.imports.add("java.util.Collection;"))
#end
#foreach ($i in $dto.interface.imports)
import ${i};
#end

public interface ${dto.interface.name} {

################################## Pub sub methods ###################################
#foreach ($method in $dto.basicPubSubMethods)
   ${method.output.type} ${method.serviceMethod}(${method.input.type} ${method.input.fieldName}) throws ServiceFaultException;

#end
################################## Correlation methods ###################################
#foreach ($method in $dto.correlationMethods)
#foreach ($corrInput in $method.inputs)
   ${method.output.finalizedType} ${method.serviceTryMethodSnippet}(${corrInput.type} ${corrInput.fieldName}) throws ServiceFaultException;

#end
#end
################################## Req/res Methods ###################################
#foreach ($method in $dto.basicServerReqResMethods)
   ${method.output.type} ${method.serviceMethod}(${method.input.type} ${method.input.fieldName}) throws ServiceFaultException;

#end
}
