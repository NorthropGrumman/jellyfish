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
package com.ngc.seaside.jellyfish.cli.command.analyzestyle;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.DefaultParameter;
import com.ngc.seaside.jellyfish.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.analysis.api.IAnalysisService;
import com.ngc.seaside.jellyfish.service.analysis.api.SystemDescriptorFinding;
import com.ngc.seaside.systemdescriptor.model.api.FieldCardinality;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.data.IEnumeration;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperty;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;
import com.ngc.seaside.systemdescriptor.model.impl.basic.Package;
import com.ngc.seaside.systemdescriptor.model.impl.basic.SystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.impl.basic.data.Data;
import com.ngc.seaside.systemdescriptor.model.impl.basic.data.DataField;
import com.ngc.seaside.systemdescriptor.model.impl.basic.data.Enumeration;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.BaseModelReferenceField;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.DataReferenceField;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.Model;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.link.ModelLink;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.properties.Property;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.properties.PropertyPrimitiveValue;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.scenario.Scenario;
import com.ngc.seaside.systemdescriptor.service.source.api.ISourceLocatorService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AnalyzeStyleCommandTest {

   private AnalyzeStyleCommand command;

   @Mock
   private ILogService logService;

   @Mock
   private IAnalysisService analysisService;

   @Mock
   private ISourceLocatorService sourceLocatorService;

   @Mock(answer = RETURNS_DEEP_STUBS)
   private IJellyFishCommandOptions options;

   private final SystemDescriptor sd = getNewSystemDescriptor();

   private final DefaultParameterCollection parameters = new DefaultParameterCollection();

   @Before
   public void setup() {
      command = new AnalyzeStyleCommand(logService, sourceLocatorService, analysisService);
      when(options.getParameters()).thenReturn(parameters);
      when(options.getSystemDescriptor()).thenReturn(sd);
   }

   @Test
   public void testAnalysis() throws Exception {
      Package[] goodPackages = toArray(Package::new, "com", "com.ngc.seaside.thr123eat1eval.a2");
      Package[] badPackages = toArray(Package::new, "com.under_score.test", "com.lowerCamelCase.asdf");
      Model[] goodModels = toArray(Model::new, "MyModel1WithLotsOfCamel12C23Ase", "M");
      Model[] badModels = toArray(Model::new, "modelLowerCamelCase", "ModelCamelCase_WithUnderscores");
      Data[] goodDatas = toArray(Data::new, "MyData1WithLotsOfCamel12C23Ase", "D");
      Data[] badDatas = toArray(Data::new, "dataLowerCamelCase", "DataCamelCase_WithUnderscores");
      Enumeration[] goodEnums = toArray(Enumeration::new, "MyEnum1WithLotsOfCamel12C23Ase", "E");
      Enumeration[] badEnums = toArray(Enumeration::new, "enumLowerCamelCase", "Enum_Under_Score");
      String[] goodEnumValues = { "U", "A1_B", "ASDF_ASDF_ASDF" };
      String[] badEnumValues = { "UpperCamelCase", "ASDF_ASDf" };
      DataField[] goodDataFields = toArray(DataField::new, "lowerCase", "a");
      DataField[] badDataFields = toArray(DataField::new, "UpperCase", "under_score");
      DataReferenceField[] goodInputs = toArray(DataReferenceField::new, "inputCamelCase", "i");
      DataReferenceField[] badInputs = toArray(DataReferenceField::new, "InputCamelCase", "input_under_score");
      DataReferenceField[] goodOutputs = toArray(DataReferenceField::new, "outputCamelCase", "o");
      DataReferenceField[] badOutputs = toArray(DataReferenceField::new, "OutputCamelCase", "output_under_score");
      Scenario[] goodScenarios = toArray(Scenario::new, "scenarioCamelCase", "s");
      Scenario[] badScenarios = toArray(Scenario::new, "ScenarioCamelCase", "scenario_under_score");
      BaseModelReferenceField[] goodParts = toArray(BaseModelReferenceField::new, "partCamelCase", "p");
      BaseModelReferenceField[] badParts =
               toArray(BaseModelReferenceField::new, "PartCamelCase", "part_under_score");
      BaseModelReferenceField[] goodRequires = toArray(BaseModelReferenceField::new, "requireCamelCase", "r");
      BaseModelReferenceField[] badRequires =
               toArray(BaseModelReferenceField::new, "RequireCamelCase", "require_under_score");
      Function<String, ModelLink<?>> linkFcn = name -> (ModelLink<?>) new ModelLink<>(goodModels[0]).setName(name);
      ModelLink<?>[] goodLinks = toArray(linkFcn, "linkCamelCase", "l");
      ModelLink<?>[] badLinks = toArray(linkFcn, "LinkCamelCase", "link_under_score");
      Function<String, Property> propertyFcn = name -> new Property(name, DataTypes.BOOLEAN, FieldCardinality.SINGLE,
               Collections.singleton(new PropertyPrimitiveValue(true)), null);
      Property[] goodProperties = toArray(propertyFcn, "propertyCamelCase", "q");
      Property[] badProperties = toArray(propertyFcn, "PropertyCamelCase", "property_under_score");

      sd.getPackages().addAll(asList(goodPackages));
      sd.getPackages().addAll(asList(badPackages));
      goodPackages[0].getModels().addAll(asList(goodModels));
      goodPackages[0].getModels().addAll(asList(badModels));
      goodPackages[0].getData().addAll(asList(goodDatas));
      goodPackages[0].getData().addAll(asList(badDatas));
      goodPackages[0].getEnumerations().addAll(asList(goodEnums));
      goodPackages[0].getEnumerations().addAll(asList(badEnums));
      goodModels[0].getInputs().addAll(asList(goodInputs));
      goodModels[0].getInputs().addAll(asList(badInputs));
      goodModels[0].getOutputs().addAll(asList(goodOutputs));
      goodModels[0].getOutputs().addAll(asList(badOutputs));
      goodModels[0].getParts().addAll(asList(goodParts));
      goodModels[0].getParts().addAll(asList(badParts));
      goodModels[0].getRequiredModels().addAll(asList(goodRequires));
      goodModels[0].getRequiredModels().addAll(asList(badRequires));
      goodModels[0].getScenarios().addAll(asList(goodScenarios));
      goodModels[0].getScenarios().addAll(asList(badScenarios));
      goodModels[0].getProperties().addAll(asList(goodProperties));
      goodModels[0].getProperties().addAll(asList(badProperties));
      goodModels[0].getLinks().addAll(asList(goodLinks));
      goodModels[0].getLinks().addAll(asList(badLinks));
      goodDatas[0].getFields().addAll(asList(goodDataFields));
      goodDatas[0].getFields().addAll(asList(badDataFields));
      goodEnums[0].getValues().addAll(asList(goodEnumValues));
      badEnums[0].getValues().addAll(asList(badEnumValues));

      command.run(options);

      ArgumentCaptor<SystemDescriptorFinding<?>> captor =
               ArgumentCaptor.forClass(SystemDescriptorFinding.class);
      verify(analysisService, atLeastOnce()).addFinding(captor.capture());
      List<SystemDescriptorFinding<?>> findings = captor.getAllValues();
      Map<String, List<SystemDescriptorFinding<?>>> keywordMap = new HashMap<>();
      for (SystemDescriptorFinding<?> finding : findings) {
         Matcher m = Pattern.compile("\\S+").matcher(finding.getMessage());
         while (m.find()) {
            keywordMap.computeIfAbsent(m.group(), __ -> new ArrayList<>()).add(finding);
         }
      }

      checkFindings(keywordMap, badPackages, IPackage::getName);
      checkFindings(keywordMap, badModels, IModel::getName);
      checkFindings(keywordMap, badDatas, IData::getName);
      checkFindings(keywordMap, badEnums, IEnumeration::getName);
      checkFindings(keywordMap, badEnumValues, Function.identity());
      checkFindings(keywordMap, badDataFields, IDataField::getName);
      checkFindings(keywordMap, badInputs, IDataReferenceField::getName);
      checkFindings(keywordMap, badOutputs, IDataReferenceField::getName);
      checkFindings(keywordMap, badParts, IModelReferenceField::getName);
      checkFindings(keywordMap, badRequires, IModelReferenceField::getName);
      checkFindings(keywordMap, badScenarios, IScenario::getName);
      checkFindings(keywordMap, badProperties, IProperty::getName);
      checkFindings(keywordMap, badLinks, l -> l.getName().get());

      assertEquals(badPackages.length + badModels.length + badDatas.length + badEnums.length + badDataFields.length
               + badEnumValues.length + badInputs.length + badOutputs.length + badScenarios.length + badParts.length
               + badRequires.length + badLinks.length + badProperties.length, findings.size());
   }

   @Test
   public void testAnalysisWithNonDefaults() throws Exception {
      parameters.addParameter(new DefaultParameter<>("packageSectionStyleRegex", "package\\d+"));
      parameters.addParameter(new DefaultParameter<>("typeStyleRegex", "type\\d+"));
      parameters.addParameter(new DefaultParameter<>("enumTypeStyleRegex", "enum\\d+"));
      parameters.addParameter(new DefaultParameter<>("nameStyleRegex", "name\\d+"));
      parameters.addParameter(new DefaultParameter<>("modelOutputStyleRegex", "output\\d+"));
      parameters.addParameter(new DefaultParameter<>("modelReferenceStyleRegex", "reference\\d+"));
      Package[] badPackages = toArray(Package::new, "com", "com.ngc.seaside.thr123eat1eval.a2");
      Package[] goodPackages = toArray(Package::new, "package1");
      Model[] badModels = toArray(Model::new, "MyModel1WithLotsOfCamel12C23Ase", "M");
      Model[] goodModels = toArray(Model::new, "type1");
      Data[] badDatas = toArray(Data::new, "MyData1WithLotsOfCamel12C23Ase", "D");
      Data[] goodDatas = toArray(Data::new, "type2");
      Enumeration[] badEnums = toArray(Enumeration::new, "MyEnum1WithLotsOfCamel12C23Ase", "E");
      Enumeration[] goodEnums = toArray(Enumeration::new, "enum1");
      String[] goodEnumValues = { "U", "A1_B", "ASDF_ASDF_ASDF" };
      String[] badEnumValues = { "UpperCamelCase", "ASDF_ASDf" };
      DataField[] badDataFields = toArray(DataField::new, "lowerCase", "a");
      DataField[] goodDataFields = toArray(DataField::new, "name1");
      DataReferenceField[] badInputs = toArray(DataReferenceField::new, "inputCamelCase", "i");
      DataReferenceField[] goodInputs = toArray(DataReferenceField::new, "name2");
      DataReferenceField[] badOutputs = toArray(DataReferenceField::new, "outputCamelCase", "o");
      DataReferenceField[] goodOutputs = toArray(DataReferenceField::new, "output1");
      Scenario[] badScenarios = toArray(Scenario::new, "scenarioCamelCase", "s");
      Scenario[] goodScenarios = toArray(Scenario::new, "name4");
      BaseModelReferenceField[] badParts = toArray(BaseModelReferenceField::new, "partCamelCase", "p");
      BaseModelReferenceField[] goodParts =
               toArray(BaseModelReferenceField::new, "reference1");
      BaseModelReferenceField[] badRequires = toArray(BaseModelReferenceField::new, "requireCamelCase", "r");
      BaseModelReferenceField[] goodRequires =
               toArray(BaseModelReferenceField::new, "reference2");
      Function<String, ModelLink<?>> linkFcn = name -> (ModelLink<?>) new ModelLink<>(badModels[0]).setName(name);
      ModelLink<?>[] badLinks = toArray(linkFcn, "linkCamelCase", "l");
      ModelLink<?>[] goodLinks = toArray(linkFcn, "name3");
      Function<String, Property> propertyFcn = name -> new Property(name, DataTypes.BOOLEAN, FieldCardinality.SINGLE,
               Collections.singleton(new PropertyPrimitiveValue(true)), null);
      Property[] badProperties = toArray(propertyFcn, "propertyCamelCase", "q");
      Property[] goodProperties = toArray(propertyFcn, "name4");

      sd.getPackages().addAll(asList(goodPackages));
      sd.getPackages().addAll(asList(badPackages));
      goodPackages[0].getModels().addAll(asList(goodModels));
      goodPackages[0].getModels().addAll(asList(badModels));
      goodPackages[0].getData().addAll(asList(goodDatas));
      goodPackages[0].getData().addAll(asList(badDatas));
      goodPackages[0].getEnumerations().addAll(asList(goodEnums));
      goodPackages[0].getEnumerations().addAll(asList(badEnums));
      goodModels[0].getInputs().addAll(asList(goodInputs));
      goodModels[0].getInputs().addAll(asList(badInputs));
      goodModels[0].getOutputs().addAll(asList(goodOutputs));
      goodModels[0].getOutputs().addAll(asList(badOutputs));
      goodModels[0].getParts().addAll(asList(goodParts));
      goodModels[0].getParts().addAll(asList(badParts));
      goodModels[0].getRequiredModels().addAll(asList(goodRequires));
      goodModels[0].getRequiredModels().addAll(asList(badRequires));
      goodModels[0].getScenarios().addAll(asList(goodScenarios));
      goodModels[0].getScenarios().addAll(asList(badScenarios));
      goodModels[0].getProperties().addAll(asList(goodProperties));
      goodModels[0].getProperties().addAll(asList(badProperties));
      goodModels[0].getLinks().addAll(asList(goodLinks));
      goodModels[0].getLinks().addAll(asList(badLinks));
      goodDatas[0].getFields().addAll(asList(goodDataFields));
      goodDatas[0].getFields().addAll(asList(badDataFields));
      goodEnums[0].getValues().addAll(asList(goodEnumValues));
      badEnums[0].getValues().addAll(asList(badEnumValues));

      command.run(options);

      ArgumentCaptor<SystemDescriptorFinding<?>> captor =
               ArgumentCaptor.forClass(SystemDescriptorFinding.class);
      verify(analysisService, atLeastOnce()).addFinding(captor.capture());
      List<SystemDescriptorFinding<?>> findings = captor.getAllValues();
      Map<String, List<SystemDescriptorFinding<?>>> keywordMap = new HashMap<>();
      for (SystemDescriptorFinding<?> finding : findings) {
         Matcher m = Pattern.compile("\\S+").matcher(finding.getMessage());
         while (m.find()) {
            keywordMap.computeIfAbsent(m.group(), __ -> new ArrayList<>()).add(finding);
         }
      }

      checkFindings(keywordMap, badPackages, IPackage::getName);
      checkFindings(keywordMap, badModels, IModel::getName);
      checkFindings(keywordMap, badDatas, IData::getName);
      checkFindings(keywordMap, badEnums, IEnumeration::getName);
      checkFindings(keywordMap, badEnumValues, Function.identity());
      checkFindings(keywordMap, badDataFields, IDataField::getName);
      checkFindings(keywordMap, badInputs, IDataReferenceField::getName);
      checkFindings(keywordMap, badOutputs, IDataReferenceField::getName);
      checkFindings(keywordMap, badParts, IModelReferenceField::getName);
      checkFindings(keywordMap, badRequires, IModelReferenceField::getName);
      checkFindings(keywordMap, badScenarios, IScenario::getName);
      checkFindings(keywordMap, badProperties, IProperty::getName);
      checkFindings(keywordMap, badLinks, l -> l.getName().get());

      assertEquals(badPackages.length + badModels.length + badDatas.length + badEnums.length + badDataFields.length
               + badEnumValues.length + badInputs.length + badOutputs.length + badScenarios.length + badParts.length
               + badRequires.length + badLinks.length + badProperties.length, findings.size());
   }

   private <T> void checkFindings(Map<String, List<SystemDescriptorFinding<?>>> keywordMap, T[] badElements,
            Function<T, String> keywordFcn) {
      for (T badElement : badElements) {
         String keyword = keywordFcn.apply(badElement);
         List<SystemDescriptorFinding<?>> findings = keywordMap.get(keyword);
         if (findings == null || findings.isEmpty()) {
            fail("No finding reported for " + badElement.getClass().getSimpleName() + " with name " + keyword);
         }
         if (findings.size() > 1) {
            fail("Expected only 1 finding for " + badElement.getClass().getSimpleName() + " with name " + keyword + ": "
                     + findings.stream().map(SystemDescriptorFinding::getMessage)
                              .collect(Collectors.joining(", ", "[", "]")));
         }
      }
   }

   @SafeVarargs
   private static <O, I> O[] toArray(Function<? super I, O> fcn, I... inputs) {
      @SuppressWarnings("unchecked")
      O[] array = (O[]) Array.newInstance(fcn.apply(inputs[0]).getClass(), inputs.length);
      for (int i = 0; i < inputs.length; i++) {
         array[i] = fcn.apply(inputs[i]);
      }
      return array;
   }

   private static SystemDescriptor getNewSystemDescriptor() {
      try {
         Class<SystemDescriptor> sdClass = SystemDescriptor.class;
         Constructor<SystemDescriptor> constructor = sdClass.getDeclaredConstructor();
         constructor.setAccessible(true);
         return constructor.newInstance();
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

}
