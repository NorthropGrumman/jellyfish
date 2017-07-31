package ${dto.nonSuffixPackageName}.api;

import com.ngc.seaside.service.fault.api.ServiceFaultException;

public interface I${dto.className} {

#foreach ($method in $dto.methods)
#if( ${method.override} )
   @Override
#end
   public ${method.returnSnippet} ${method.methodName}(${method.argumentsListSnippet}) throws ServiceFaultException;
#end
}
