/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.ngc.seaside.jellyfish.service.config.impl.transportconfigurationservice;

import com.ngc.seaside.jellyfish.service.config.api.IAdministrationConfigurationService;
import com.ngc.seaside.jellyfish.service.config.api.dto.RestConfiguration;
import com.ngc.seaside.jellyfish.service.config.api.dto.admin.AdministrationConfiguration;
import com.ngc.seaside.jellyfish.service.config.api.dto.admin.RestAdministrationConfiguration;
import com.ngc.seaside.jellyfish.service.config.impl.transportconfigurationservice.utils.RestConfigurationUtils;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyDataValue;

public class AdministrationConfigurationService
         extends ModelPropertyConfigurationService<AdministrationConfiguration>
         implements IAdministrationConfigurationService {

   public static final String REST_ADIMINSTRATION_CONFIGURATION_QUALIFIED_NAME =
            "com.ngc.seaside.systemdescriptor.administration.RestAdministration";
   
   public static final String SHUTDOWN_FIELD_NAME = "shutdown";
   public static final String RESTART_FIELD_NAME = "restart";
   
   @Override
   protected boolean isConfigurationProperty(String qualifiedName) {
      return REST_ADIMINSTRATION_CONFIGURATION_QUALIFIED_NAME.equals(qualifiedName);
   }

   @Override
   protected AdministrationConfiguration convert(IPropertyDataValue value) {
      String type = value.getReferencedDataType().getFullyQualifiedName();
      switch (type) {
         case REST_ADIMINSTRATION_CONFIGURATION_QUALIFIED_NAME:
            return convertRest(value);
         default:
            throw new IllegalArgumentException("Unable to convert " + type + " property to telemetry");
      }
   }

   private RestAdministrationConfiguration convertRest(IPropertyDataValue value) {
      RestAdministrationConfiguration configuration = new RestAdministrationConfiguration();
      IPropertyDataValue shutdownProperty = value.getData(getField(value, SHUTDOWN_FIELD_NAME));
      IPropertyDataValue restartProperty = value.getData(getField(value, RESTART_FIELD_NAME));
      RestConfiguration shutdownConfig = RestConfigurationUtils.getRestConfiguration(shutdownProperty);
      RestConfiguration restartConfig = RestConfigurationUtils.getRestConfiguration(restartProperty);
      configuration.setShutdown(shutdownConfig);
      configuration.setRestart(restartConfig);
      return configuration;
   }
   
}
