package ${serviceDto.service.packageName};

#set ($ignore = $serviceDto.service.imports.add("com.ngc.blocs.service.api.IServiceModule"))
#set ($ignore = $serviceDto.service.imports.add("com.ngc.blocs.service.event.api.IEventService"))
#set ($ignore = $serviceDto.service.imports.add("com.ngc.blocs.service.log.api.ILogService"))
#set ($ignore = $serviceDto.service.imports.add("com.ngc.seaside.service.fault.api.IFaultManagementService"))
#set ($ignore = $serviceDto.service.imports.add("com.ngc.blocs.service.thread.api.IThreadService"))
#set ($ignore = $serviceDto.service.imports.add("com.ngc.seaside.service.fault.api.ServiceFaultException"))
#if (!$baseServiceDto.correlationMethods.isEmpty())
#set ($ignore = $serviceDto.service.imports.add("com.ngc.seaside.service.correlation.api.ICorrelationService"))
#set ($ignore = $serviceDto.service.imports.add("com.ngc.seaside.service.correlation.api.ILocalCorrelationEvent"))
#end
#set ($ignore = $serviceDto.service.imports.add("org.osgi.service.component.annotations.Activate"))
#set ($ignore = $serviceDto.service.imports.add("org.osgi.service.component.annotations.Component"))
#set ($ignore = $serviceDto.service.imports.add("org.osgi.service.component.annotations.Deactivate"))
#set ($ignore = $serviceDto.service.imports.add("org.osgi.service.component.annotations.Reference"))
#set ($ignore = $serviceDto.service.imports.add("org.osgi.service.component.annotations.ReferenceCardinality"))
#set ($ignore = $serviceDto.service.imports.add("org.osgi.service.component.annotations.ReferencePolicy"))
#foreach ($i in $serviceDto.service.imports)
import ${i};
#end

@Component(service = {${serviceDto.interface}.class, IServiceModule.class}, immediate = true)
public class ${serviceDto.service.name} extends ${serviceDto.baseClass} {

#foreach ($method in $baseServiceDto.basicPubSubMethods)
   @Override
   public ${method.output.type} ${method.serviceMethod}(${method.input.type} ${method.input.fieldName}) throws ServiceFaultException {
      // TODO: implement this
      throw new UnsupportedOperationException("not implemented");
   }

#end
#foreach ($method in $baseServiceDto.basicSinkMethods)
   @Override
   public void ${method.serviceMethod}(${method.input.type} ${method.input.fieldName}) throws ServiceFaultException {
      // TODO: implement this
      throw new UnsupportedOperationException("not implemented");
   }

#end
#foreach ($method in $baseServiceDto.correlationMethods)
   @Override
   public ${method.output.type} ${method.serviceMethod}(
#foreach ($input in $method.inputs)
      ${input.type} ${input.fieldName},
#end
      ILocalCorrelationEvent<${method.correlationType}> correlationEvent) throws ServiceFaultException {
         // TODO: implement this
         throw new UnsupportedOperationException("not implemented");
   }

#end
#foreach ($scenario in $baseServiceDto.complexScenarios)
   @Override
   public void ${scenario.serviceMethod}(
#foreach ($input in $scenario.inputs)
#set ($lastParam = $foreach.last && $scenario.outputs.isEmpty())
      BlockingQueue<${input.type}> ${input.fieldName}Queue#if ($lastParam)) {#{else},
#end
#end
#foreach ($output in $scenario.outputs)
#set ($lastParam = $foreach.last)
      Consumer<${output.type}> ${output.fieldName}Consumer#if ($lastParam)) {#{else},
#end
#end

      // TODO: implement this
      throw new UnsupportedOperationException("not implemented");
   }

#end
#foreach ($method in $baseServiceDto.basicServerReqResMethods)
   @Override
   public ${method.output.type} ${method.serviceMethod}(${method.input.type} ${method.input.fieldName}) throws ServiceFaultException{
      // TODO: implement this
      throw new UnsupportedOperationException("not implemented");
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
   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "removeLogService")
   public void setLogService(ILogService ref) {
      super.setLogService(ref);
   }

   @Override
   public void removeLogService(ILogService ref) {
      super.removeLogService(ref);
   }

   @Override
   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "removeEventService")
   public void setEventService(IEventService ref) {
      super.setEventService(ref);
   }

   @Override
   public void removeEventService(IEventService ref) {
      super.removeEventService(ref);
   }

   @Override
   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "removeFaultManagementService")
   public void setFaultManagementService(IFaultManagementService ref) {
      super.setFaultManagementService(ref);
   }

   @Override
   public void removeFaultManagementService(IFaultManagementService ref) {
      super.removeFaultManagementService(ref);
   }

   @Override
   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "removeThreadService")
   public void setThreadService(IThreadService ref) {
      super.setThreadService(ref);
   }

   @Override
   public void removeThreadService(IThreadService ref) {
      super.removeThreadService(ref);
   }

#if (!$baseServiceDto.correlationMethods.isEmpty())
   @Override
   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "removeCorrelationService")
   public void setCorrelationService(ICorrelationService ref) {
      this.correlationService = ref;
   }

   @Override
   public void removeCorrelationService(ICorrelationService ref) {
      setCorrelationService(null);
   }

#end
}
