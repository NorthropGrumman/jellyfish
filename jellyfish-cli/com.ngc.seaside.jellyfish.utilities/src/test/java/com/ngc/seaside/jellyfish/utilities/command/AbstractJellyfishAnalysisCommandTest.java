/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
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
package com.ngc.seaside.jellyfish.utilities.command;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.api.DefaultParameter;
import com.ngc.seaside.jellyfish.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.DefaultUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IEnumeration;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.impl.basic.NamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.impl.basic.metadata.Metadata;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import javax.json.Json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AbstractJellyfishAnalysisCommandTest {

   private TestableCommand command;

   private DefaultParameterCollection parameters;

   @Mock
   private ISystemDescriptor systemDescriptor;

   @Mock
   private IJellyFishCommandOptions options;

   @Mock
   private ILogService logService;

   @Before
   public void setup() {
      parameters = new DefaultParameterCollection();
      when(options.getParameters()).thenReturn(parameters);
      when(options.getSystemDescriptor()).thenReturn(systemDescriptor);

      command = new TestableCommand();
      command.setLogService(logService);
      command.activate();
   }

   @Test
   public void testDoesAnalyzeSingleModelWithModelArgument() {
      IModel model = registeredModel("com.foo.FooModel");
      parameters.addParameter(new DefaultParameter<>(CommonParameters.MODEL.getName(), model.getFullyQualifiedName()));

      command.run(options);
      assertTrue("model was not analyzed!",
                 command.wasAnalyzed(model));
      assertEquals("too many models analyzed!",
                   1,
                   command.models.size());
   }

   @Test
   public void testDoesAnalyzeModelsWithStereotypesArgument() {
      IModel model1 = registeredModel("com.foo.FooModel", "foo");
      IModel model2 = registeredModel("com.foo.BarModel", "foo");
      IModel model3 = registeredModel("com.foo.CooModel", "blah");
      parameters.addParameter(new DefaultParameter<>(CommonParameters.STEREOTYPES.getName(), "foo"));

      NamedChildCollection<IPackage, IModel> models = new NamedChildCollection<>();
      models.add(model1);
      models.add(model2);
      models.add(model3);

      IPackage packagez = mock(IPackage.class);
      when(packagez.getModels()).thenReturn(models);

      NamedChildCollection<ISystemDescriptor, IPackage> packages = new NamedChildCollection<>();
      packages.add(packagez);
      when(systemDescriptor.getPackages()).thenReturn(packages);

      command.run(options);
      assertTrue("model was not analyzed!",
                 command.wasAnalyzed(model1));
      assertTrue("model was not analyzed!",
                 command.wasAnalyzed(model2));
      assertEquals("too many models analyzed!",
                   2,
                   command.models.size());
   }

   @Test
   public void testDoesAnalyzeEntireProject() {
      IModel model = registeredModel("com.foo.FooModel");
      IData data = registeredData("com.foo.BarData");
      IEnumeration enumeration = registeredEnum("com.foo.CooEnum");

      NamedChildCollection<IPackage, IModel> models = new NamedChildCollection<>();
      NamedChildCollection<IPackage, IData> datum = new NamedChildCollection<>();
      NamedChildCollection<IPackage, IEnumeration> enums = new NamedChildCollection<>();
      models.add(model);
      datum.add(data);
      enums.add(enumeration);

      IPackage packagez = mock(IPackage.class);
      when(packagez.getModels()).thenReturn(models);
      when(packagez.getData()).thenReturn(datum);
      when(packagez.getEnumerations()).thenReturn(enums);

      NamedChildCollection<ISystemDescriptor, IPackage> packages = new NamedChildCollection<>();
      packages.add(packagez);
      when(systemDescriptor.getPackages()).thenReturn(packages);

      command.run(options);
      assertTrue("model was not analyzed!",
                 command.wasAnalyzed(model));
      assertTrue("data was not analyzed!",
                 command.wasAnalyzed(data));
      assertTrue("enum was not analyzed!",
                 command.wasAnalyzed(enumeration));
      assertEquals("too many models analyzed!",
                   1,
                   command.models.size());
      assertEquals("too many data analyzed!",
                   1,
                   command.datum.size());
      assertEquals("too many enums analyzed!",
                   1,
                   command.enums.size());
   }

   @Test
   public void testDoesLoadResource() {
      String resource = AbstractJellyfishAnalysisCommand.getResource("docs/example.md", getClass());
      assertTrue("content not correct!",
                 resource.contains("# This is an example"));
   }

   private IModel registeredModel(String name) {
      IModel model = mock(IModel.class);
      when(model.getName()).thenReturn(name.substring(name.lastIndexOf('.')));
      when(model.getFullyQualifiedName()).thenReturn(name);
      when(systemDescriptor.findModel(model.getFullyQualifiedName())).thenReturn(Optional.of(model));
      return model;
   }

   private IModel registeredModel(String name, String stereotype) {
      Metadata metadata = new Metadata();
      metadata.setJson(Json.createObjectBuilder().add(
            "stereotypes",
            Json.createArrayBuilder(Collections.singletonList(stereotype))).build());

      IModel model = mock(IModel.class);
      when(model.getName()).thenReturn(name.substring(name.lastIndexOf('.')));
      when(model.getMetadata()).thenReturn(metadata);

      return model;
   }

   private IData registeredData(String name) {
      IData data = mock(IData.class);
      when(data.getName()).thenReturn(name.substring(name.lastIndexOf('.')));
      return data;
   }

   private IEnumeration registeredEnum(String name) {
      IEnumeration enumeration = mock(IEnumeration.class);
      when(enumeration.getName()).thenReturn(name.substring(name.lastIndexOf('.')));
      return enumeration;
   }

   private static class TestableCommand extends AbstractJellyfishAnalysisCommand {

      private final Collection<IModel> models = new ArrayList<>();
      private final Collection<IData> datum = new ArrayList<>();
      private final Collection<IEnumeration> enums = new ArrayList<>();

      TestableCommand() {
         super("testable-command");
      }

      @Override
      protected IUsage createUsage() {
         return new DefaultUsage("");
      }

      @Override
      protected void analyzeModel(IModel model) {
         this.models.add(model);
      }

      @Override
      protected void analyzeData(IData data) {
         datum.add(data);
      }

      @Override
      protected void analyzeEnumeration(IEnumeration enumeration) {
         enums.add(enumeration);
      }

      boolean wasAnalyzed(IModel model) {
         return models.contains(model);
      }

      boolean wasAnalyzed(IData data) {
         return datum.contains(data);
      }

      boolean wasAnalyzed(IEnumeration enumeration) {
         return enums.contains(enumeration);
      }
   }
}
