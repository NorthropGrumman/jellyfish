package com.ngc.seaside.jellyfish.cli.command.report.requirementsverification;

import com.ngc.blocs.test.impl.common.log.PrintStreamLogService;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RequirementsVerificationMatrixCommandTest {

   private RequirementsVerificationMatrixCommand cmd = new RequirementsVerificationMatrixCommand();
   private DefaultParameterCollection parameters;
   private PrintStreamLogService logger = new PrintStreamLogService();

   @Mock
   private IJellyFishCommandOptions jellyFishCommandOptions;

   @Mock
   private ISystemDescriptor systemDescriptor;

   @Mock
   private IModel model;

   @Mock
   private IPackage packagez;

   @Before
   public void setup() {
      // Setup mock system descriptor
      when(jellyFishCommandOptions.getSystemDescriptor()).thenReturn(systemDescriptor);
      when(systemDescriptor.findModel("com.ngc.seaside.test.Service")).thenReturn(Optional.of(model));

      // Setup mock model
      when(model.getParent()).thenReturn(packagez);
      when(model.getParent().getName()).thenReturn("com.ngc.seaside.test");
      when(model.getName()).thenReturn("Model");

      parameters = new DefaultParameterCollection();

      // Setup class under test
      cmd.setLogService(logger);
   }

   @Test
   public void testCommandWithoutOptionalParams() {
      // Verify mocked behaviors
      verify(jellyFishCommandOptions, times(1)).getParameters();
      verify(jellyFishCommandOptions, times(1)).getSystemDescriptor();
      verify(model, times(4)).getName();
      verify(model, times(2)).getParent();

   }

   @Test
   public void testCommandWithOptionalParams() {
      parameters.addParameter(new DefaultParameter<>(RequirementsVerificationMatrixCommand.OUTPUT_FORMAT_PROPERTY,
                                                     RequirementsVerificationMatrixCommand.DEFAULT_OUTPUT_FORMAT_PROPERTY));
      parameters.addParameter(new DefaultParameter<>(RequirementsVerificationMatrixCommand.OUTPUT_PROPERTY,
                                                     RequirementsVerificationMatrixCommand.DEFAULT_OUTPUT_PROPERTY));
      parameters.addParameter(new DefaultParameter<>(RequirementsVerificationMatrixCommand.SCOPE_PROPERTY,
                                                     RequirementsVerificationMatrixCommand.DEFAULT_SCOPE_PROPERTY));
      parameters.addParameter(new DefaultParameter<>(RequirementsVerificationMatrixCommand.VALUES_PROPERTY,
                                                     RequirementsVerificationMatrixCommand.DEFAULT_VALUES_PROPERTY));
      parameters.addParameter(new DefaultParameter<>(RequirementsVerificationMatrixCommand.OPERATOR_PROPERTY,
                                                     RequirementsVerificationMatrixCommand.DEFAULT_OPERATOR_PROPERTY));

      // Verify mocked behaviors
      verify(jellyFishCommandOptions, times(1)).getParameters();
      verify(jellyFishCommandOptions, times(1)).getSystemDescriptor();
      verify(model, times(3)).getName();
      verify(model, times(1)).getParent();
   }

}
