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
package com.ngc.seaside.jellyfish.cli.command.test.service;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.codegen.api.IJavaServiceGenerationService;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.ClassDto;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.EnumDto;
import com.ngc.seaside.jellyfish.service.name.api.IPackageNamingService;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import java.util.Set;
import java.util.TreeSet;

/**
 * A basic implementation of {@link IJavaServiceGenerationService} for tests
 */
public class MockedJavaServiceGenerationService implements IJavaServiceGenerationService {

   private final IPackageNamingService packageService;
   
   public MockedJavaServiceGenerationService() {
      this(new MockedPackageNamingService());
   }
   
   public MockedJavaServiceGenerationService(IPackageNamingService packageService) {
      this.packageService = packageService;
   }
   
   @Override
   public ClassDto getServiceInterfaceDescription(IJellyFishCommandOptions options, IModel model) {
      ClassDto dto = new ClassDto();
      dto.setPackageName(packageService.getServiceInterfacePackageName(options, model));
      dto.setName("I" + model.getName());
      return dto;
   }

   @Override
   public ClassDto getBaseServiceDescription(IJellyFishCommandOptions options, IModel model) {
      ClassDto dto = new ClassDto();
      dto.setPackageName(packageService.getServiceBaseImplementationPackageName(options, model));
      dto.setName("Abstract" + model.getName());
      return dto;
   }

   @Override
   public EnumDto getTransportTopicsDescription(IJellyFishCommandOptions options, IModel model) {
      EnumDto dto = new EnumDto();
      dto.setPackageName(packageService.getTransportTopicsPackageName(options, model));
      dto.setName(model.getName() + "TransportTopics");
      Set<String> topics = new TreeSet<>();
      model.getInputs().forEach(input -> topics.add(input.getType().getName().toUpperCase()));
      model.getOutputs().forEach(input -> topics.add(input.getType().getName().toUpperCase()));
      dto.setValues(topics);
      return dto;
   }

}
