package com.ngc.seaside.systemdescriptor.service.impl.xtext.validation;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.systemdescriptor.extension.IValidatorExtension;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.link.IModelLink;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.WrappedPackage;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Data;
import com.ngc.seaside.systemdescriptor.systemDescriptor.DataFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.FieldReference;
import com.ngc.seaside.systemdescriptor.systemDescriptor.GivenStep;
import com.ngc.seaside.systemdescriptor.systemDescriptor.InputDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.LinkDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;
import com.ngc.seaside.systemdescriptor.systemDescriptor.OutputDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PartDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PrimitiveDataFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.ReferencedDataFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.RequireDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Scenario;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorFactory;
import com.ngc.seaside.systemdescriptor.validation.SystemDescriptorValidator;
import com.ngc.seaside.systemdescriptor.validation.api.ISystemDescriptorValidator;

import org.eclipse.emf.ecore.EObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ValidationDelegateTest {

   private final Collection<Package> morePackagesToWrapIfNeeded = new ArrayList<>();

   private ValidationDelegate delegate;

   private ISystemDescriptor descriptor;

   @Mock
   private SystemDescriptorValidator dslValidator;

   @Mock
   private ISystemDescriptorValidator validator;

   @Mock
   private ILogService logService;

   @Mock
   private IValidatorExtension.ValidationHelper helper;

   @Before
   public void setup() throws Throwable {
      delegate = new ValidationDelegate(dslValidator, logService, new ValidationDelegate.ValidatorsHolder()) {
         @Override
         protected void doValidate(EObject source,
                                   ValidationHelper helper,
                                   ISystemDescriptor descriptor) {
            interceptValidate(source, helper, descriptor);
            super.doValidate(source, helper, descriptor);
         }
      };
   }

   @Test
   public void testDoesRegisterAndUnregisterValidatorWithDsl() throws Throwable {
      delegate.addValidator(validator);
      delegate.removeValidator(validator);

      verify(dslValidator).addValidatorExtension(delegate);
      verify(dslValidator).removeValidatorExtension(delegate);
   }

   @Test
   public void testDoesValidatePackage() throws Throwable {
      Package source = factory().createPackage();
      source.setName("foo.package");

      delegate.addValidator(validator);
      delegate.validate(source, helper);

      IPackage toValidate = descriptor.getPackages().getByName(source.getName()).get();
      verify(validator).validate(argThat(ctx -> toValidate.equals(ctx.getObject())));
   }

   @Test
   public void testDoesValidateData() throws Throwable {
      Data source = factory().createData();
      source.setName("MyData");
      Package p = factory().createPackage();
      p.setName("foo.package");
      p.setElement(source);

      delegate.addValidator(validator);
      delegate.validate(source, helper);

      IData toValidate = descriptor.findData(p.getName(), source.getName()).get();
      verify(validator).validate(argThat(ctx -> toValidate.equals(ctx.getObject())));
   }

   @Test
   public void testDoesValidatePrimitiveDataField() throws Throwable {
      PrimitiveDataFieldDeclaration source = factory().createPrimitiveDataFieldDeclaration();
      source.setName("myPrimitiveDataField");
      Data data = factory().createData();
      data.setName("MyData");
      data.getFields().add(source);
      Package p = factory().createPackage();
      p.setName("foo.package");
      p.setElement(data);

      delegate.addValidator(validator);
      delegate.validate(source, helper);

      IDataField toValidate = descriptor.findData(p.getName(), data.getName()).get()
            .getFields()
            .getByName(source.getName())
            .get();
      verify(validator).validate(argThat(ctx -> toValidate.equals(ctx.getObject())));
   }

   @Test
   public void testDoesValidateReferencedDataField() throws Throwable {
      ReferencedDataFieldDeclaration source = factory().createReferencedDataFieldDeclaration();
      source.setName("myReferencedDataField");
      Data data = factory().createData();
      data.setName("MyData");
      data.getFields().add(source);
      Package p = factory().createPackage();
      p.setName("foo.package");
      p.setElement(data);

      delegate.addValidator(validator);
      delegate.validate(source, helper);

      IDataField toValidate = descriptor.findData(p.getName(), data.getName()).get()
            .getFields()
            .getByName(source.getName())
            .get();
      verify(validator).validate(argThat(ctx -> toValidate.equals(ctx.getObject())));
   }

   @Test
   public void testDoesValidateModel() throws Throwable {
      Model source = factory().createModel();
      source.setName("MyModel");
      Package p = factory().createPackage();
      p.setName("foo.package");
      p.setElement(source);

      delegate.addValidator(validator);
      delegate.validate(source, helper);

      IModel toValidate = descriptor.findModel(p.getName(), source.getName()).get();
      verify(validator).validate(argThat(ctx -> toValidate.equals(ctx.getObject())));
   }

   @Test
   public void testDoesValidateModelInput() throws Throwable {
      InputDeclaration source = factory().createInputDeclaration();
      source.setName("myInput");
      Model model = factory().createModel();
      model.setName("MyModel");
      model.setInput(factory().createInput());
      model.getInput().getDeclarations().add(source);
      Package p = factory().createPackage();
      p.setName("foo.package");
      p.setElement(model);

      delegate.addValidator(validator);
      delegate.validate(source, helper);

      IDataReferenceField toValidate = descriptor.findModel(p.getName(), model.getName()).get()
            .getInputs()
            .getByName(source.getName())
            .get();
      verify(validator).validate(argThat(ctx -> toValidate.equals(ctx.getObject())));
   }

   @Test
   public void testDoesValidateModelOutput() throws Throwable {
      OutputDeclaration source = factory().createOutputDeclaration();
      source.setName("myOutput");
      Model model = factory().createModel();
      model.setName("MyModel");
      model.setOutput(factory().createOutput());
      model.getOutput().getDeclarations().add(source);
      Package p = factory().createPackage();
      p.setName("foo.package");
      p.setElement(model);

      delegate.addValidator(validator);
      delegate.validate(source, helper);

      IDataReferenceField toValidate = descriptor.findModel(p.getName(), model.getName()).get()
            .getOutputs()
            .getByName(source.getName())
            .get();
      verify(validator).validate(argThat(ctx -> toValidate.equals(ctx.getObject())));
   }

   @Test
   public void testDoesValidateModelPart() throws Throwable {
      PartDeclaration source = factory().createPartDeclaration();
      source.setName("myPart");
      Model model = factory().createModel();
      model.setName("MyModel");
      model.setParts(factory().createParts());
      model.getParts().getDeclarations().add(source);
      Package p = factory().createPackage();
      p.setName("foo.package");
      p.setElement(model);

      delegate.addValidator(validator);
      delegate.validate(source, helper);

      IModelReferenceField toValidate = descriptor.findModel(p.getName(), model.getName()).get()
            .getParts()
            .getByName(source.getName())
            .get();
      verify(validator).validate(argThat(ctx -> toValidate.equals(ctx.getObject())));
   }

   @Test
   public void testDoesValidateModelRequirement() throws Throwable {
      RequireDeclaration source = factory().createRequireDeclaration();
      source.setName("myRequiredModel");
      Model model = factory().createModel();
      model.setName("MyModel");
      model.setRequires(factory().createRequires());
      model.getRequires().getDeclarations().add(source);
      Package p = factory().createPackage();
      p.setName("foo.package");
      p.setElement(model);

      delegate.addValidator(validator);
      delegate.validate(source, helper);

      IModelReferenceField toValidate = descriptor.findModel(p.getName(), model.getName()).get()
            .getRequiredModels()
            .getByName(source.getName())
            .get();
      verify(validator).validate(argThat(ctx -> toValidate.equals(ctx.getObject())));
   }

   @Test
   public void testDoesValidateLink() throws Throwable {
      Model sourceModel = factory().createModel();
      sourceModel.setName("sourceModel");
      Model targetModel = factory().createModel();
      targetModel.setName("targetModel");

      PartDeclaration sourceField = factory().createPartDeclaration();
      sourceField.setType(sourceModel);
      sourceField.setName("sourceField");
      Model yetMoreSourceModels = factory().createModel();
      yetMoreSourceModels.setName("yetMoreSourceModels");
      yetMoreSourceModels.setParts(factory().createParts());
      yetMoreSourceModels.getParts().getDeclarations().add(sourceField);

      PartDeclaration targetField = factory().createPartDeclaration();
      targetField.setType(targetModel);
      targetField.setName("targetField");
      Model yetMoreTargetModels = factory().createModel();
      yetMoreTargetModels.setName("yetMoreTargetModels");
      yetMoreTargetModels.setParts(factory().createParts());
      yetMoreTargetModels.getParts().getDeclarations().add(targetField);

      FieldReference s = factory().createFieldReference();
      s.setFieldDeclaration(sourceField);
      FieldReference t = factory().createFieldReference();
      t.setFieldDeclaration(targetField);

      LinkDeclaration source = factory().createLinkDeclaration();
      source.setSource(s);
      source.setTarget(t);

      Model model = factory().createModel();
      model.setName("MyModel");
      model.setLinks(factory().createLinks());
      model.getLinks().getDeclarations().add(source);

      Package p1 = factory().createPackage();
      p1.setName("foo.package");
      p1.setElement(model);
      Package p2 = factory().createPackage();
      p2.setName("foo.package");
      p2.setElement(yetMoreSourceModels);
      Package p3 = factory().createPackage();
      p3.setName("foo.package");
      p3.setElement(yetMoreTargetModels);
      morePackagesToWrapIfNeeded.addAll(Arrays.asList(p2, p3));

      delegate.addValidator(validator);
      delegate.validate(source, helper);

      IModelLink<?> toValidate = descriptor.findModel(p1.getName(), model.getName()).get()
            .getLinks()
            .iterator()
            .next();
      verify(validator).validate(argThat(ctx -> toValidate.equals(ctx.getObject())));
   }

   @Test
   public void testDoesValidateScenario() throws Throwable {
      Scenario source = factory().createScenario();
      source.setName("myScenario");
      Model model = factory().createModel();
      model.setName("MyModel");
      model.getScenarios().add(source);
      Package p = factory().createPackage();
      p.setName("foo.package");
      p.setElement(model);

      delegate.addValidator(validator);
      delegate.validate(source, helper);

      IScenario toValidate = descriptor.findModel(p.getName(), model.getName()).get()
            .getScenarios()
            .iterator()
            .next();
      verify(validator).validate(argThat(ctx -> toValidate.equals(ctx.getObject())));
   }

   @Test
   public void testDoesValidateScenarioStep() throws Throwable {
      GivenStep source = factory().createGivenStep();
      source.setKeyword("myStep");
      Scenario scenario = factory().createScenario();
      scenario.setName("myScenario");
      scenario.setGiven(factory().createGivenDeclaration());
      scenario.getGiven().getSteps().add(source);
      Model model = factory().createModel();
      model.setName("MyModel");
      model.getScenarios().add(scenario);
      Package p = factory().createPackage();
      p.setName("foo.package");
      p.setElement(model);

      delegate.addValidator(validator);
      delegate.validate(source, helper);

      IScenarioStep toValidate = descriptor.findModel(p.getName(), model.getName()).get()
            .getScenarios()
            .iterator()
            .next()
            .getGivens()
            .iterator()
            .next();
      verify(validator).validate(argThat(ctx -> toValidate.equals(ctx.getObject())));
   }

   @Test
   public void testDoesConsumeValidatorException() throws Throwable {
      Package source = factory().createPackage();
      source.setName("foo.package");

      doThrow(new RuntimeException("testing error handling")).when(validator).validate(any());

      delegate.addValidator(validator);
      delegate.validate(source, helper);
      // No exception should be thrown.
   }

   @Test
   public void testDoesAutomaticallyRegisterInjectedValidators() throws Throwable {
      ValidationDelegate.ValidatorsHolder holder = new ValidationDelegate.ValidatorsHolder();
      holder.validators = Collections.singleton(validator);
      delegate = new ValidationDelegate(dslValidator, logService, holder);
      verify(dslValidator).addValidatorExtension(delegate);
   }

   private void interceptValidate(EObject source,
                                  IValidatorExtension.ValidationHelper helper,
                                  ISystemDescriptor descriptor) {
      // Save the descriptor so we can use it in verification.
      this.descriptor = descriptor;
      for (Package p : morePackagesToWrapIfNeeded) {
         ((WrappedPackage) descriptor.getPackages().iterator().next()).wrap(p);
      }
   }

   private static SystemDescriptorFactory factory() {
      return SystemDescriptorFactory.eINSTANCE;
   }
}
