package ${package};

      import com.ngc.blocs.service.api.IServiceModule;
      import com.ngc.blocs.service.event.api.IEventService;
      import com.ngc.blocs.service.log.api.ILogService;
      import com.ngc.seaside.service.fault.api.IFaultManagementService;
      import com.ngc.seaside.service.fault.api.ServiceFaultException;
      import com.ngc.seaside.service.fault.api.ServiceInputFaultException;
      import com.ngc.seaside.threateval.engagementplanning.events.TrackEngagementStatus;
      import ${groupId}.${artifactId}.api.I${modelname};

      import ${groupId}.${artifactId}.base.impl.Abstract${modelname};
      import com.ngc.seaside.threateval.events.TrackPriority;

      import org.osgi.service.component.annotations.Activate;
      import org.osgi.service.component.annotations.Component;
      import org.osgi.service.component.annotations.Deactivate;
      import org.osgi.service.component.annotations.Reference;
      import org.osgi.service.component.annotations.ReferenceCardinality;
      import org.osgi.service.component.annotations.ReferencePolicy;

//@Component(service = {I${modelname}.class, IServiceModule.class}, immediate = true)
public class ${generatedDelegateClassname} extends Abstract${modelname}{

   private final ${modelname} delegate = new ${modelname}();

   @Inject
   public ${generatedDelegateClassname}(ILogService logService, IEventService eventService, IFaultManagementService faultManagementService){
         delegate.setLogService(logService);
         delegate.setEventService(eventService);
         delegate.setFaultManagementService(faultManagementService);
         delegate.activate();
   }
}
