package ${dto.packageName}.config;

import com.google.inject.Inject;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.service.resource.api.IResourceService;
import ${dto.basePackage}.transport.topic.${dto.className}TransportTopics;

import java.net.UnknownHostException;

public class ${dto.className}TestConfiguration{

   @SuppressWarnings("unused")
   private final ILogService logService;

   @SuppressWarnings("unused")
   private final IResourceService resourceService;

   @Inject
   public ${dto.className}TestConfiguration(ILogService logService, IResourceService resourceService) {
      this.logService = logService;
      this.resourceService = resourceService;
      activate();
   }

   private void activate() {
      // TODO Implement configurations
      throw new UnsupportedOperationException("Not Implemented")
   }
}
