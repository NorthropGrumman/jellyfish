package ${dto.serviceInterfaceDto.packageName};

import com.ngc.seaside.service.fault.api.ServiceFaultException;
#foreach ($i in $dto.serviceInterfaceDto.imports)
import ${i};
#end

public interface ${dto.serviceInterfaceDto.interfaceName} {

#foreach ($method in $dto.methods)
   ${method.returnSnippet} ${method.methodName}(${method.argumentsListSnippet}) throws ServiceFaultException;
#end
}
