package com.ngc.seaside.jellyfish.service.codegen.dataservice.impl;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.codegen.api.IDataFieldGenerationService;
import com.ngc.seaside.jellyfish.service.codegen.api.IGeneratedJavaField;
import com.ngc.seaside.jellyfish.service.codegen.api.IGeneratedProtoField;
import com.ngc.seaside.jellyfish.service.name.api.IPackageNamingService;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;

import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

public class DataFieldGenerationService implements IDataFieldGenerationService {

   private IPackageNamingService packageNamingService;

   @Override
   public IGeneratedJavaField getEventsField(IJellyFishCommandOptions options, IDataField field) {
      return GeneratedField.of(field, options, packageNamingService);
   }

   @Override
   public IGeneratedProtoField getMessagesField(IJellyFishCommandOptions options, IDataField field) {
      return GeneratedField.of(field, options, packageNamingService);
   }
   
   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC)
   public void setPackageNamingService(IPackageNamingService ref) {
      this.packageNamingService = ref;
   }

   public void removePackageNamingService(IPackageNamingService ref) {
      setPackageNamingService(null);
   }

}
