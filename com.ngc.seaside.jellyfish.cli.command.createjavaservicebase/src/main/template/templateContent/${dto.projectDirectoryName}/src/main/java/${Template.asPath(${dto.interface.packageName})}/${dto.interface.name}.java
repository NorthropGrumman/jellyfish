package ${dto.interface.packageName};

import com.ngc.seaside.service.fault.api.ServiceFaultException;
#foreach ($i in $dto.interface.imports)
import ${i};
#end

public interface ${dto.interface.name}#if ($dto.interface.implementedInterface) extends ${dto.interface.implementedInterface.name}#end {

#foreach ($method in $dto.interface.methods)
   ${method.returnSnippet} ${method.name}(${method.argumentsListSnippet}) throws ServiceFaultException;

#end
}
