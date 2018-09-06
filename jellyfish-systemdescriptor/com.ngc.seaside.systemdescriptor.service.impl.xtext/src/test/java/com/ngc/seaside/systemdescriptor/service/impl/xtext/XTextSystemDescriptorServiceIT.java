/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
 */
package com.ngc.seaside.systemdescriptor.service.impl.xtext;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.test.impl.common.log.PrintStreamLogService;
import com.ngc.seaside.systemdescriptor.model.api.INamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.link.IModelLink;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperties;
import com.ngc.seaside.systemdescriptor.model.api.traversal.ModelPredicates;
import com.ngc.seaside.systemdescriptor.model.api.traversal.Traversals;
import com.ngc.seaside.systemdescriptor.model.impl.basic.NamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.DataReferenceField;
import com.ngc.seaside.systemdescriptor.model.impl.view.AggregatedDataViewTest;
import com.ngc.seaside.systemdescriptor.model.impl.view.AggregatedModelViewTest;
import com.ngc.seaside.systemdescriptor.service.api.IParsingResult;
import com.ngc.seaside.systemdescriptor.service.api.ISystemDescriptorService;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.module.XTextSystemDescriptorServiceModule;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.testutil.InjectorTestFactory;
import com.ngc.seaside.systemdescriptor.validation.api.AbstractSystemDescriptorValidator;
import com.ngc.seaside.systemdescriptor.validation.api.ISystemDescriptorValidator;
import com.ngc.seaside.systemdescriptor.validation.api.IValidationContext;
import com.ngc.seaside.systemdescriptor.validation.api.Severity;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class XTextSystemDescriptorServiceIT {

   private ISystemDescriptorService service;

   private ISystemDescriptorValidator validator;

   @Before
   public void setup() {
      validator = new AbstractSystemDescriptorValidator() {
         @Override
         protected void validateDataField(IValidationContext<IDataField> context) {
            IDataField field = context.getObject();
            if ("hour".equals(field.getName())) {
               context.declare(Severity.ERROR, "testing custom validation", field).getName();
            }
         }
      };

      service = InjectorTestFactory.getSharedInstance().getInstance(ISystemDescriptorService.class);
   }

   @Test
   public void testDoesParseProject() {
      IParsingResult result = service.parseProject(Paths.get("build", "resources", "test", "valid-project"));
      assertTrue("did not parse project!",
                 result.isSuccessful());
   }

   @Test
   public void testDoesProgrammaticallyRegisterValidators() {
      service.addValidator(validator);
      try {
         IParsingResult result = service.parseProject(Paths.get("build", "resources", "test", "valid-project"));
         assertFalse("validator should have triggered issues!",
                     result.isSuccessful());
      } finally {
         service.removeValidator(validator);
      }
   }

   @Test
   public void testDoesCollectionModelsByStereotypes() {
      IParsingResult result = service.parseProject(Paths.get("build", "resources", "test", "valid-project"));
      assertTrue("did not parse project!",
                 result.isSuccessful());

      Collection<IModel> systems = Traversals.collectModels(result.getSystemDescriptor(),
                                                            ModelPredicates.withAnyStereotype("system"));
      assertTrue("did not find AlarmClock system!",
                 systems.contains(result.getSystemDescriptor().findModel("clocks.AlarmClock").get()));
      assertEquals("contains extra model objects that don't have the system annotation!",
                   1,
                   systems.size());

      Collection<IModel> everythingButSystems = Traversals.collectModels(
            result.getSystemDescriptor(),
            ModelPredicates.withAnyStereotype("system").negate());
      assertFalse("AlarmClock system should not be in the result",
                  everythingButSystems.contains(result.getSystemDescriptor().findModel("clocks.AlarmClock").get()));
      assertEquals("did not find all results!",
                   5,
                   everythingButSystems.size());
   }

   @Test
   public void testDoesReturnAggregatedViewsOfData() {
      IData data = mock(IData.class);
      IData parent = mock(IData.class);
      INamedChildCollection<IData, IDataField> children = AggregatedDataViewTest.fields("a");
      when(data.getMetadata()).thenReturn(IMetadata.EMPTY_METADATA);
      when(data.getFields()).thenReturn(children);
      when(data.getExtendedDataType()).thenReturn(Optional.of(parent));

      children = AggregatedDataViewTest.fields("b");
      when(parent.getMetadata()).thenReturn(IMetadata.EMPTY_METADATA);
      when(parent.getFields()).thenReturn(children);
      when(parent.getExtendedDataType()).thenReturn(Optional.empty());

      IData dataView = service.getAggregatedView(data);
      assertTrue("missing field on data object!",
                 dataView.getFields().getByName("a").isPresent());
      assertTrue("missing field on parent data object!",
                 dataView.getFields().getByName("b").isPresent());

      IData parentView = service.getAggregatedView(parent);
      assertNotSame("did not return unique views",
                    dataView,
                    parentView);

      assertSame("did not cache view!",
                 dataView,
                 service.getAggregatedView(data));
   }

   @Test
   public void testDoesReturnAggregatedViewsOfModels() {
      IModel model = mock(IModel.class);
      IModel parent = mock(IModel.class);
      INamedChildCollection<IModel, IDataReferenceField> dataRefFields;
      dataRefFields = AggregatedModelViewTest.fields(DataReferenceField.class, "inputA");
      when(model.getMetadata()).thenReturn(IMetadata.EMPTY_METADATA);
      when(model.getProperties()).thenReturn(IProperties.EMPTY_PROPERTIES);
      when(model.getInputs()).thenReturn(dataRefFields);
      when(model.getOutputs()).thenReturn(new NamedChildCollection<>());
      when(model.getParts()).thenReturn(new NamedChildCollection<>());
      when(model.getRequiredModels()).thenReturn(new NamedChildCollection<>());
      when(model.getScenarios()).thenReturn(new NamedChildCollection<>());
      when(model.getLinks()).thenReturn(Collections.emptyList());
      when(model.getRefinedModel()).thenReturn(Optional.of(parent));

      dataRefFields = AggregatedModelViewTest.fields(DataReferenceField.class, "inputB");
      when(parent.getMetadata()).thenReturn(IMetadata.EMPTY_METADATA);
      when(parent.getProperties()).thenReturn(IProperties.EMPTY_PROPERTIES);
      when(parent.getInputs()).thenReturn(dataRefFields);
      when(parent.getOutputs()).thenReturn(new NamedChildCollection<>());
      when(parent.getParts()).thenReturn(new NamedChildCollection<>());
      when(parent.getRequiredModels()).thenReturn(new NamedChildCollection<>());
      when(parent.getScenarios()).thenReturn(new NamedChildCollection<>());
      when(parent.getLinks()).thenReturn(Collections.emptyList());
      when(parent.getRefinedModel()).thenReturn(Optional.empty());

      IModel modelView = service.getAggregatedView(model);
      assertTrue("missing input field on model object!",
                 modelView.getInputs().getByName("inputA").isPresent());
      assertTrue("missing field on parent model object!",
                 modelView.getInputs().getByName("inputA").isPresent());

      IModel parentView = service.getAggregatedView(parent);
      assertNotSame("did not return unique views",
                    modelView,
                    parentView);

      assertSame("did not cache view!",
                 modelView,
                 service.getAggregatedView(model));
   }

   @Test
   public void testDoesManageProperties() {
      IParsingResult result = service.parseProject(Paths.get("build", "resources", "test", "properties-project"));
      assertTrue("did not parse project!",
                 result.isSuccessful());

      IModel model = service.getAggregatedView(
            result.getSystemDescriptor().findModel("clocks.RefinedAlarmClock").get());

      assertEquals("property config.port1 not correct!",
                   1,
                   model.getProperties().resolveAsInteger("config", "port1").get().intValue());
      assertEquals("property config.port2 not correct!",
                   2,
                   model.getProperties().resolveAsInteger("config", "port2").get().intValue());
      assertEquals("property config.name not correct!",
                   "hello",
                   model.getProperties().resolveAsString("config", "name").get());
      assertEquals("property config.host not correct!",
                   "world",
                   model.getProperties().resolveAsString("config", "host").get());

      IModelLink<?> link = model.getLinkByName("timer2Display").get();
      assertEquals("property linkConfig.port1 not correct!",
                   3,
                   link.getProperties().resolveAsInteger("linkConfig", "port1").get().intValue());
      assertEquals("property linkConfig.port2 not correct!",
                   4,
                   link.getProperties().resolveAsInteger("linkConfig", "port2").get().intValue());
      assertEquals("property linkConfig.name not correct!",
                   "foo",
                   link.getProperties().resolveAsString("linkConfig", "name").get());
      assertEquals("property linkConfig.host not correct!",
                   "bar",
                   link.getProperties().resolveAsString("linkConfig", "host").get());

      IModelReferenceField speaker = model.getParts().getByName("speaker").get();
      assertEquals("property zone not correct!",
                   "CST",
                   speaker.getProperties().resolveAsEnumeration("zone").get().getValue());

      // Test multiple levels of refinement.
      model = service.getAggregatedView(
            result.getSystemDescriptor().findModel("clocks.BetterAlarmClock").get());
      assertEquals("property config.port1 not correct!",
                   100,
                   model.getProperties().resolveAsInteger("config", "port1").get().intValue());
      assertEquals("property config.port2 not correct!",
                   2,
                   model.getProperties().resolveAsInteger("config", "port2").get().intValue());

      link = model.getLinkByName("timer2Display").get();
      assertEquals("property linkConfig.port1 not correct!",
                   300,
                   link.getProperties().resolveAsInteger("linkConfig", "port1").get().intValue());
      assertEquals("property linkConfig.port2 not correct!",
                   4,
                   link.getProperties().resolveAsInteger("linkConfig", "port2").get().intValue());

      speaker = model.getParts().getByName("speaker").get();
      assertEquals("property zone not correct!",
                   "MST",
                   speaker.getProperties().resolveAsEnumeration("zone").get().getValue());
   }

   @Ignore("This test cannot run with the build because XText holds state statically; however it is still useful to run"
         + " by itself to make sure the standalone configuration works.")
   @Test
   public void testDoesCreateStandaloneServiceWithGuice() {
      Collection<Module> modules = new ArrayList<>();
      modules.add(new AbstractModule() {
         @Override
         protected void configure() {
            bind(ILogService.class).toInstance(new PrintStreamLogService());
         }
      });
      modules.add(XTextSystemDescriptorServiceModule.forStandaloneUsage());
      Injector injector = Guice.createInjector(modules);
      service = injector.getInstance(ISystemDescriptorService.class);

      IParsingResult result = service.parseProject(Paths.get("build", "resources", "test", "valid-project"));
      assertTrue("did not parse project!",
                 result.isSuccessful());
   }
}
