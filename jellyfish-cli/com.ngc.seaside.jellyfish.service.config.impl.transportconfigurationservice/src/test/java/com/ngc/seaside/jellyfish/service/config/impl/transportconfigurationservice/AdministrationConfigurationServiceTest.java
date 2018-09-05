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
package com.ngc.seaside.jellyfish.service.config.impl.transportconfigurationservice;

import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.config.api.dto.HttpMethod;
import com.ngc.seaside.jellyfish.service.config.api.dto.RestConfiguration;
import com.ngc.seaside.jellyfish.service.config.api.dto.admin.AdministrationConfiguration;
import com.ngc.seaside.jellyfish.service.config.api.dto.admin.RestAdministrationConfiguration;
import com.ngc.seaside.systemdescriptor.model.api.FieldCardinality;
import com.ngc.seaside.systemdescriptor.model.api.INamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperties;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperty;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyDataValue;
import com.ngc.seaside.systemdescriptor.service.api.ISystemDescriptorService;
import com.ngc.seaside.systemdescriptor.test.systemdescriptor.ModelUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import static com.ngc.seaside.jellyfish.service.config.impl.transportconfigurationservice.ConfigurationTestUtils.getMockedReference;
import static com.ngc.seaside.jellyfish.service.config.impl.transportconfigurationservice.ConfigurationTestUtils.getMockedRestConfiguration;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class AdministrationConfigurationServiceTest {

   private AdministrationConfigurationService service;

   @Mock
   private ISystemDescriptorService sdService;

   @Before
   public void setup() {
      service = new AdministrationConfigurationService();
      service.setSystemDescriptorService(sdService);
   }

   @Test
   public void testAdministrationConfiguration() {
      String deploymentModelName = "com.ngc.DeploymentModel";
      IModel deploymentModel = mock(IModel.class, RETURNS_DEEP_STUBS);
      String modelName = "com.ngc.Model";
      IModel model = mock(IModel.class, RETURNS_DEEP_STUBS);
      String address = "localhost";
      String interfaceName = "0.0.0.0";
      int port = 8081;
      String shutdownPath = "/shutdown";
      String restartPath = "/shutdown";
      String contentType = "application/x-protobuf";
      HttpMethod method = HttpMethod.POST;

      IProperty configProperty = getMockedRestAdministrationConfiguration(address, interfaceName, port, shutdownPath,
               restartPath, contentType, method);

      IJellyFishCommandOptions options = mock(IJellyFishCommandOptions.class, RETURNS_DEEP_STUBS);
      when(options.getParameters()
               .getParameter(CommonParameters.DEPLOYMENT_MODEL.getName())
               .getStringValue()).thenReturn(deploymentModelName);
      when(options.getSystemDescriptor().findModel(deploymentModelName)).thenReturn(Optional.of(deploymentModel));
      when(options.getSystemDescriptor().findModel(modelName)).thenReturn(Optional.of(model));
      when(sdService.getAggregatedView(deploymentModel)).thenReturn(deploymentModel);
      when(sdService.getAggregatedView(model)).thenReturn(model);
      IModelReferenceField part = getMockedReference("model", model, configProperty);
      INamedChildCollection<IModel, IModelReferenceField> parts = ModelUtils.mockedNamedCollectionOf(part);
      when(deploymentModel.getParts()).thenReturn(parts);
      when(model.getProperties()).thenReturn(IProperties.EMPTY_PROPERTIES);

      Collection<AdministrationConfiguration> configurations = service.getConfigurations(options, model);
      assertEquals(1, configurations.size());
      assertTrue(configurations.iterator().next() instanceof RestAdministrationConfiguration);
      RestAdministrationConfiguration config = (RestAdministrationConfiguration) configurations.iterator().next();
      RestConfiguration shutdownConfig = config.getShutdown();
      assertEquals(address, shutdownConfig.getNetworkAddress().getAddress());
      assertEquals(interfaceName, shutdownConfig.getNetworkInterface().getName());
      assertEquals(port, shutdownConfig.getPort());
      assertEquals(shutdownPath, shutdownConfig.getPath());
      assertEquals(contentType, shutdownConfig.getContentType());
      assertEquals(method, shutdownConfig.getHttpMethod());
      RestConfiguration restartConfig = config.getShutdown();
      assertEquals(address, restartConfig.getNetworkAddress().getAddress());
      assertEquals(interfaceName, restartConfig.getNetworkInterface().getName());
      assertEquals(port, restartConfig.getPort());
      assertEquals(restartPath, restartConfig.getPath());
      assertEquals(contentType, restartConfig.getContentType());
      assertEquals(method, restartConfig.getHttpMethod());

   }

   private static IProperty getMockedRestAdministrationConfiguration(String address, String interfaceName,
            int port, String shutdownPath, String restartPath, String contentType, HttpMethod method) {
      IProperty property = mock(IProperty.class, RETURNS_DEEP_STUBS);
      when(property.getName()).thenReturn(UUID.randomUUID().toString());
      when(property.getCardinality()).thenReturn(FieldCardinality.SINGLE);
      when(property.getType()).thenReturn(DataTypes.DATA);
      when(property.getReferencedDataType().getFullyQualifiedName()).thenReturn(
               AdministrationConfigurationService.REST_ADIMINSTRATION_CONFIGURATION_QUALIFIED_NAME);
      when(property.getData().isSet()).thenReturn(true);
      when(property.getData().getReferencedDataType().getFullyQualifiedName())
               .thenReturn(AdministrationConfigurationService.REST_ADIMINSTRATION_CONFIGURATION_QUALIFIED_NAME);
      IDataField shutdownField = mock(IDataField.class);
      IDataField restartField = mock(IDataField.class);
      when(property.getData().getFieldByName(AdministrationConfigurationService.SHUTDOWN_FIELD_NAME)).thenReturn(
               Optional.of(shutdownField));
      when(property.getData().getFieldByName(AdministrationConfigurationService.RESTART_FIELD_NAME)).thenReturn(
               Optional.of(restartField));
      IPropertyDataValue shutdownConfig =
               getMockedRestConfiguration(address, interfaceName, port, shutdownPath, contentType, method).getData();
      IPropertyDataValue restartConfig =
               getMockedRestConfiguration(address, interfaceName, port, restartPath, contentType, method).getData();
      when(property.getData().getData(shutdownField)).thenReturn(shutdownConfig);
      when(property.getData().getData(restartField)).thenReturn(restartConfig);
      return property;
   }

}
