package com.ngc.seaside.jellyfish.service.name.packagez.impl;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.name.packagez.impl.PackageNamingService;
import com.ngc.seaside.systemdescriptor.model.impl.basic.Package;
import com.ngc.seaside.systemdescriptor.model.impl.basic.data.Data;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.Model;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PackageNamingServiceTest {

   private PackageNamingService service;

   private DefaultParameterCollection parameters;

   @Mock
   private ILogService logService;

   @Mock
   private IJellyFishCommandOptions options;

   @Before
   public void setup() throws Throwable {
      parameters = new DefaultParameterCollection();
      when(options.getParameters()).thenReturn(parameters);

      service = new PackageNamingService();
      service.setLogService(logService);
      service.activate();
   }

   @Test
   public void testDoesGenerateFullyQualifiedNameForMessage() throws Throwable {
      String modelName = "com.ngc.seaside.threateval.ThreatEvaluation";
      parameters.addParameter(new DefaultParameter<>(PackageNamingService.MODEL_PARAMETER_NAME, modelName));

      Data data = newData("TrackPriority", "com.ngc.seaside.threateval.datatype");
      assertEquals("message name not correct!",
                   "com.ngc.seaside.threateval.threatevaluation.datatype",
                   service.getMessagePackageName(options, data));

      data = newData("StateVector", "com.ngc.seaside.common.datatype");
      assertEquals("message name not correct!",
                   "com.ngc.seaside.threateval.threatevaluation.common.datatype",
                   service.getMessagePackageName(options, data));

      data = newData("StateVector", "com.ngc.common.datatype");
      assertEquals("message name not correct!",
                   "com.ngc.seaside.threateval.threatevaluation.common.datatype",
                   service.getMessagePackageName(options, data));

      data = newData("StateVector", "external.datatype");
      assertEquals("message name not correct!",
                   "com.ngc.seaside.threateval.threatevaluation.external.datatype",
                   service.getMessagePackageName(options, data));

      data = newData("StateVector", "com.ngc.seaside.threateval");
      assertEquals("message name not correct!",
                   "com.ngc.seaside.threateval.threatevaluation",
                   service.getMessagePackageName(options, data));
   }
   
   @Test
   public void testDoesGenerateFullyQualifiedNameForDistribution() throws Throwable {
      Model model = newModel("TrackPriorityService", "com.ngc.seaside.threateval");
      parameters.addParameter(new DefaultParameter<>(PackageNamingService.MODEL_PARAMETER_NAME, model.getFullyQualifiedName()));
      assertEquals("name not correct!",
                   "com.ngc.seaside.threateval.trackpriorityservice",
                   service.getDistributionPackageName(options, model));

      model = newModel("EngagementTrackPriorityService", "com.ngc.seaside.common.datatype");
      parameters.addParameter(new DefaultParameter<>(PackageNamingService.MODEL_PARAMETER_NAME, model.getFullyQualifiedName()));
      assertEquals("name not correct!",
                   "com.ngc.seaside.common.datatype.engagementtrackpriorityservice",
                   service.getDistributionPackageName(options, model));

      model = newModel("DefendedAreaTrackPriorityService", "com.ngc.seaside.common.defendedareatrackpriorityservice");
      parameters.addParameter(new DefaultParameter<>(PackageNamingService.MODEL_PARAMETER_NAME, model.getFullyQualifiedName()));
      assertEquals("name not correct!",
                   "com.ngc.seaside.common.defendedareatrackpriorityservice.defendedareatrackpriorityservice",
                   service.getDistributionPackageName(options, model));

      model = newModel("ClassificationTrackPriorityService", "external.datatype");
      parameters.addParameter(new DefaultParameter<>(PackageNamingService.MODEL_PARAMETER_NAME, model.getFullyQualifiedName()));
      assertEquals("name not correct!",
                   "external.datatype.classificationtrackpriorityservice",
                   service.getDistributionPackageName(options, model));

      model = newModel("TrackPriorityService", "com.ngc.seaside.threateval");
      parameters.addParameter(new DefaultParameter<>(PackageNamingService.MODEL_PARAMETER_NAME, model.getFullyQualifiedName()));
      assertEquals("name not correct!",
                   "com.ngc.seaside.threateval.trackpriorityservice",
                   service.getDistributionPackageName(options, model));
   }

   @Test
   public void testDoesGetFullyQualifiedNameForDomain() throws Throwable {
      String modelName = "com.ngc.seaside.threateval.EngagementTrackPriorityService";
      parameters.addParameter(new DefaultParameter<>(PackageNamingService.MODEL_PARAMETER_NAME, modelName));

      Data data = newData("TrackPriority", "com.ngc.seaside.threateval.datatype");
      assertEquals("domain name not correct!",
                   "com.ngc.seaside.threateval.engagementtrackpriorityservice.domain.datatype",
                   service.getDomainPackageName(options, data));

      data = newData("StateVector", "com.ngc.seaside.common.datatype");
      assertEquals("domain name not correct!",
                   "com.ngc.seaside.threateval.engagementtrackpriorityservice.domain.common.datatype",
                   service.getDomainPackageName(options, data));

      data = newData("StateVector", "com.ngc.common.datatype");
      assertEquals("domain name not correct!",
                   "com.ngc.seaside.threateval.engagementtrackpriorityservice.domain.common.datatype",
                   service.getDomainPackageName(options, data));

      data = newData("StateVector", "external.datatype");
      assertEquals("domain name not correct!",
                   "com.ngc.seaside.threateval.engagementtrackpriorityservice.domain.external.datatype",
                   service.getDomainPackageName(options, data));

      data = newData("StateVector", "com.ngc.seaside.threateval");
      assertEquals("domain name not correct!",
                   "com.ngc.seaside.threateval.engagementtrackpriorityservice.domain",
                   service.getDomainPackageName(options, data));
   }

   @Test
   public void testDoesGetFullyQualifiedNameForEvent() throws Throwable {
      String modelName = "com.ngc.seaside.threateval.EngagementTrackPriorityService";
      parameters.addParameter(new DefaultParameter<>(PackageNamingService.MODEL_PARAMETER_NAME, modelName));

      Data data = newData("TrackPriority", "com.ngc.seaside.threateval.datatype");
      assertEquals("event name not correct!",
                   "com.ngc.seaside.threateval.engagementtrackpriorityservice.event.datatype",
                   service.getEventPackageName(options, data));

      data = newData("StateVector", "com.ngc.seaside.common.datatype");
      assertEquals("event name not correct!",
                   "com.ngc.seaside.threateval.engagementtrackpriorityservice.event.common.datatype",
                   service.getEventPackageName(options, data));

      data = newData("StateVector", "com.ngc.common.datatype");
      assertEquals("event name not correct!",
                   "com.ngc.seaside.threateval.engagementtrackpriorityservice.event.common.datatype",
                   service.getEventPackageName(options, data));

      data = newData("StateVector", "external.datatype");
      assertEquals("event name not correct!",
                   "com.ngc.seaside.threateval.engagementtrackpriorityservice.event.external.datatype",
                   service.getEventPackageName(options, data));

      data = newData("StateVector", "com.ngc.seaside.threateval");
      assertEquals("event name not correct!",
                   "com.ngc.seaside.threateval.engagementtrackpriorityservice.event",
                   service.getEventPackageName(options, data));
   }
   
   @Test
   public void testDoesGetFullyQualifiedNameForConnector() throws Throwable {
      Model model = newModel("TrackPriorityService", "com.ngc.seaside.threateval");
      parameters.addParameter(new DefaultParameter<>(PackageNamingService.MODEL_PARAMETER_NAME, model.getFullyQualifiedName()));
      assertEquals("event name not correct!",
                   "com.ngc.seaside.threateval.trackpriorityservice.connector",
                   service.getConnectorPackageName(options, model));

      model = newModel("EngagementTrackPriorityService", "com.ngc.seaside.common.datatype");
      parameters.addParameter(new DefaultParameter<>(PackageNamingService.MODEL_PARAMETER_NAME, model.getFullyQualifiedName()));
      assertEquals("event name not correct!",
                   "com.ngc.seaside.common.datatype.engagementtrackpriorityservice.connector",
                   service.getConnectorPackageName(options, model));

      model = newModel("DefendedAreaTrackPriorityService", "com.ngc.seaside.common.defendedareatrackpriorityservice");
      parameters.addParameter(new DefaultParameter<>(PackageNamingService.MODEL_PARAMETER_NAME, model.getFullyQualifiedName()));
      assertEquals("event name not correct!",
                   "com.ngc.seaside.common.defendedareatrackpriorityservice.defendedareatrackpriorityservice.connector",
                   service.getConnectorPackageName(options, model));

      model = newModel("ClassificationTrackPriorityService", "external.datatype");
      parameters.addParameter(new DefaultParameter<>(PackageNamingService.MODEL_PARAMETER_NAME, model.getFullyQualifiedName()));
      assertEquals("event name not correct!",
                   "external.datatype.classificationtrackpriorityservice.connector",
                   service.getConnectorPackageName(options, model));

      model = newModel("TrackPriorityService", "com.ngc.seaside.threateval");
      parameters.addParameter(new DefaultParameter<>(PackageNamingService.MODEL_PARAMETER_NAME, model.getFullyQualifiedName()));
      assertEquals("event name not correct!",
                   "com.ngc.seaside.threateval.trackpriorityservice.connector",
                   service.getConnectorPackageName(options, model));
   }

   @Test
   public void testDoesGetFullyQualifiedNameForService() throws Throwable {
      Model model = newModel("TrackPriorityService", "com.ngc.seaside.threateval");
      parameters.addParameter(new DefaultParameter<>(PackageNamingService.MODEL_PARAMETER_NAME, model.getFullyQualifiedName()));
      assertEquals("event name not correct!",
                   "com.ngc.seaside.threateval.trackpriorityservice.impl",
                   service.getServiceImplementationPackageName(options, model));

      model = newModel("EngagementTrackPriorityService", "com.ngc.seaside.common.datatype");
      parameters.addParameter(new DefaultParameter<>(PackageNamingService.MODEL_PARAMETER_NAME, model.getFullyQualifiedName()));
      assertEquals("event name not correct!",
                   "com.ngc.seaside.common.datatype.engagementtrackpriorityservice.impl",
                   service.getServiceImplementationPackageName(options, model));

      model = newModel("DefendedAreaTrackPriorityService", "com.ngc.seaside.common.defendedareatrackpriorityservice");
      parameters.addParameter(new DefaultParameter<>(PackageNamingService.MODEL_PARAMETER_NAME, model.getFullyQualifiedName()));
      assertEquals("event name not correct!",
                   "com.ngc.seaside.common.defendedareatrackpriorityservice.defendedareatrackpriorityservice.impl",
                   service.getServiceImplementationPackageName(options, model));

      model = newModel("ClassificationTrackPriorityService", "external.datatype");
      parameters.addParameter(new DefaultParameter<>(PackageNamingService.MODEL_PARAMETER_NAME, model.getFullyQualifiedName()));
      assertEquals("event name not correct!",
                   "external.datatype.classificationtrackpriorityservice.impl",
                   service.getServiceImplementationPackageName(options, model));

      model = newModel("TrackPriorityService", "com.ngc.seaside.threateval");
      parameters.addParameter(new DefaultParameter<>(PackageNamingService.MODEL_PARAMETER_NAME, model.getFullyQualifiedName()));
      assertEquals("event name not correct!",
                   "com.ngc.seaside.threateval.trackpriorityservice.impl",
                   service.getServiceImplementationPackageName(options, model));
   }

   @Test
   public void testDoesGetFullyQualifiedNameForServiceBase() throws Throwable {
      Model model = newModel("TrackPriorityService", "com.ngc.seaside.threateval");
      parameters.addParameter(new DefaultParameter<>(PackageNamingService.MODEL_PARAMETER_NAME, model.getFullyQualifiedName()));
      assertEquals("event name not correct!",
                   "com.ngc.seaside.threateval.trackpriorityservice.base.impl",
                   service.getServiceBaseImplementationPackageName(options, model));

      model = newModel("EngagementTrackPriorityService", "com.ngc.seaside.common.datatype");
      parameters.addParameter(new DefaultParameter<>(PackageNamingService.MODEL_PARAMETER_NAME, model.getFullyQualifiedName()));
      assertEquals("event name not correct!",
                   "com.ngc.seaside.common.datatype.engagementtrackpriorityservice.base.impl",
                   service.getServiceBaseImplementationPackageName(options, model));

      model = newModel("DefendedAreaTrackPriorityService", "com.ngc.seaside.common.defendedareatrackpriorityservice");
      parameters.addParameter(new DefaultParameter<>(PackageNamingService.MODEL_PARAMETER_NAME, model.getFullyQualifiedName()));
      assertEquals("event name not correct!",
                   "com.ngc.seaside.common.defendedareatrackpriorityservice.defendedareatrackpriorityservice.base.impl",
                   service.getServiceBaseImplementationPackageName(options, model));

      model = newModel("ClassificationTrackPriorityService", "external.datatype");
      parameters.addParameter(new DefaultParameter<>(PackageNamingService.MODEL_PARAMETER_NAME, model.getFullyQualifiedName()));
      assertEquals("event name not correct!",
                   "external.datatype.classificationtrackpriorityservice.base.impl",
                   service.getServiceBaseImplementationPackageName(options, model));

      model = newModel("TrackPriorityService", "com.ngc.seaside.threateval");
      parameters.addParameter(new DefaultParameter<>(PackageNamingService.MODEL_PARAMETER_NAME, model.getFullyQualifiedName()));
      assertEquals("event name not correct!",
                   "com.ngc.seaside.threateval.trackpriorityservice.base.impl",
                   service.getServiceBaseImplementationPackageName(options, model));
   }

   @Test
   public void testDoesGetFullyQualifiedNameForServiceInterface() throws Throwable {
      Model model = newModel("TrackPriorityService", "com.ngc.seaside.threateval");
      parameters.addParameter(new DefaultParameter<>(PackageNamingService.MODEL_PARAMETER_NAME, model.getFullyQualifiedName()));
      assertEquals("event name not correct!",
                   "com.ngc.seaside.threateval.trackpriorityservice.api",
                   service.getServiceInterfacePackageName(options, model));

      model = newModel("EngagementTrackPriorityService", "com.ngc.seaside.common.datatype");
      parameters.addParameter(new DefaultParameter<>(PackageNamingService.MODEL_PARAMETER_NAME, model.getFullyQualifiedName()));
      assertEquals("event name not correct!",
                   "com.ngc.seaside.common.datatype.engagementtrackpriorityservice.api",
                   service.getServiceInterfacePackageName(options, model));

      model = newModel("DefendedAreaTrackPriorityService", "com.ngc.seaside.common.defendedareatrackpriorityservice");
      parameters.addParameter(new DefaultParameter<>(PackageNamingService.MODEL_PARAMETER_NAME, model.getFullyQualifiedName()));
      assertEquals("event name not correct!",
                   "com.ngc.seaside.common.defendedareatrackpriorityservice.defendedareatrackpriorityservice.api",
                   service.getServiceInterfacePackageName(options, model));

      model = newModel("ClassificationTrackPriorityService", "external.datatype");
      parameters.addParameter(new DefaultParameter<>(PackageNamingService.MODEL_PARAMETER_NAME, model.getFullyQualifiedName()));
      assertEquals("event name not correct!",
                   "external.datatype.classificationtrackpriorityservice.api",
                   service.getServiceInterfacePackageName(options, model));

      model = newModel("TrackPriorityService", "com.ngc.seaside.threateval");
      parameters.addParameter(new DefaultParameter<>(PackageNamingService.MODEL_PARAMETER_NAME, model.getFullyQualifiedName()));
      assertEquals("event name not correct!",
                   "com.ngc.seaside.threateval.trackpriorityservice.api",
                   service.getServiceInterfacePackageName(options, model));
   }

   @Test
   public void testDoesGetFullyQualifiedNameForServiceTopics() throws Throwable {
      Model model = newModel("TrackPriorityService", "com.ngc.seaside.threateval");
      parameters.addParameter(new DefaultParameter<>(PackageNamingService.MODEL_PARAMETER_NAME, model.getFullyQualifiedName()));
      assertEquals("event name not correct!",
                   "com.ngc.seaside.threateval.trackpriorityservice.transport.topic",
                   service.getTransportTopicsPackageName(options, model));

      model = newModel("EngagementTrackPriorityService", "com.ngc.seaside.common.datatype");
      parameters.addParameter(new DefaultParameter<>(PackageNamingService.MODEL_PARAMETER_NAME, model.getFullyQualifiedName()));
      assertEquals("event name not correct!",
                   "com.ngc.seaside.common.datatype.engagementtrackpriorityservice.transport.topic",
                   service.getTransportTopicsPackageName(options, model));

      model = newModel("DefendedAreaTrackPriorityService", "com.ngc.seaside.common.defendedareatrackpriorityservice");
      parameters.addParameter(new DefaultParameter<>(PackageNamingService.MODEL_PARAMETER_NAME, model.getFullyQualifiedName()));
      assertEquals("event name not correct!",
                   "com.ngc.seaside.common.defendedareatrackpriorityservice.defendedareatrackpriorityservice.transport.topic",
                   service.getTransportTopicsPackageName(options, model));

      model = newModel("ClassificationTrackPriorityService", "external.datatype");
      parameters.addParameter(new DefaultParameter<>(PackageNamingService.MODEL_PARAMETER_NAME, model.getFullyQualifiedName()));
      assertEquals("event name not correct!",
                   "external.datatype.classificationtrackpriorityservice.transport.topic",
                   service.getTransportTopicsPackageName(options, model));

      model = newModel("TrackPriorityService", "com.ngc.seaside.threateval");
      parameters.addParameter(new DefaultParameter<>(PackageNamingService.MODEL_PARAMETER_NAME, model.getFullyQualifiedName()));
      assertEquals("event name not correct!",
                   "com.ngc.seaside.threateval.trackpriorityservice.transport.topic",
                   service.getTransportTopicsPackageName(options, model));
   }

   private static Model newModel(String name, String packageName) {
      Model model = new Model(name);
      Package p = new Package(packageName);
      model.setParent(p);
      p.addModel(model);
      return model;
   }

   private static Data newData(String name, String packageName) {
      Data data = new Data(name);
      Package p = new Package(packageName);
      data.setParent(p);
      p.addData(data);
      return data;
   }
}
