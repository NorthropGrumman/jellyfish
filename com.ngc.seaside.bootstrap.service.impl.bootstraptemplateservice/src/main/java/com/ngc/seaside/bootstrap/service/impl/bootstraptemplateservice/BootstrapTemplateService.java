package com.ngc.seaside.bootstrap.service.impl.bootstraptemplateservice;

import com.google.inject.Inject;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.service.resource.api.IResourceService;
import com.ngc.seaside.bootstrap.service.promptuser.api.IPromptUserService;
import com.ngc.seaside.bootstrap.service.template.api.BootstrapTemplateException;
import com.ngc.seaside.bootstrap.service.template.api.IBootstrapTemplateService;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import java.nio.file.Path;

/**
 * @author justan.provence@ngc.com
 */
@Component(service = IBootstrapTemplateService.class)
public class BootstrapTemplateService implements IBootstrapTemplateService {

   private final BootstrapTemplateServiceDelegate delegate =
            new BootstrapTemplateServiceDelegate();

   @Activate
   public void activate() {

   }

   @Deactivate
   public void deactivate() {

   }

   /**
    * Sets log service.
    *
    * @param ref the ref
    */
   @Reference(cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.STATIC,
            unbind = "removeLogService")
   @Inject
   public void setLogService(ILogService ref) {
      delegate.setLogService(ref);
   }

   /**
    * Remove log service.
    */
   public void removeLogService(ILogService ref) {
      setLogService(null);
   }

   /**
    * Sets the resource service.
    *
    * @param ref the ref
    */
   @Reference(cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.STATIC,
            unbind = "removeResourceService")
   @Inject
   public void setResourceService(IResourceService ref) {
      delegate.setResourceService(ref);
   }

   /**
    * Remove the resource service.
    */
   public void removeResourceService(IResourceService ref) {
      setResourceService(null);
   }

   /**
    * Sets the prompt user service.
    *
    * @param ref the ref
    */
   @Reference(cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.STATIC,
            unbind = "removePromptUserService")
   @Inject
   public void setPromptUserService(IPromptUserService ref) {
      delegate.setPromptUserService(ref);
   }

   /**
    * Remove the prompt user service.
    */
   public void removePromptUserService(IPromptUserService ref) {
      setPromptUserService(null);
   }


   @Override
   public boolean templateExists(String templateName) {
      return delegate.templateExists(templateName);
   }

   @Override
   public Path unpack(String templateName, Path outputDirectory, boolean clean)
            throws BootstrapTemplateException {
      return delegate.unpack(templateName, outputDirectory, clean);
   }
}
