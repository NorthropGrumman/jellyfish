package com.ngc.seaside.jellyfish.service.codegen.module;

import com.google.inject.AbstractModule;
import com.ngc.seaside.jellyfish.service.codegen.api.IDataFieldGenerationService;
import com.ngc.seaside.jellyfish.service.codegen.api.IJavaServiceGenerationService;
import com.ngc.seaside.jellyfish.service.codegen.dataservice.impl.DataFieldGenerationServiceGuiceWrapper;
import com.ngc.seaside.jellyfish.service.codegen.javaservice.impl.JavaServiceGenerationServiceGuiceWrapper;

public class CodeGenServicesGuiceModule extends AbstractModule {

   @Override
   protected void configure() {
      bind(IJavaServiceGenerationService.class).to(JavaServiceGenerationServiceGuiceWrapper.class);
      bind(IDataFieldGenerationService.class).to(DataFieldGenerationServiceGuiceWrapper.class);
   }
}
