package com.ngc.seaside.jellyfish.cli.command.report.requirementsverification.utilities;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;

@RunWith(MockitoJUnitRunner.class)
public class ModelUtilsIT {
   @Mock
   private IJellyFishCommandOptions jellyFishCommandOptions;

   @Before
   public void setup() throws IOException {
      Mockito.when(jellyFishCommandOptions.getSystemDescriptorProjectPath()).thenReturn(Paths.get("src/test/resources"));
   }

   @Test
   public void givenFeatureFolderWithNonFeatureFiles_whenGetAllFeatures_thenNoExceptionThrown() {
      IPackage mockPackage = Mockito.mock(IPackage.class);         
      IModel mockModel = Mockito.mock(IModel.class);         

      Mockito.when(mockPackage.getName()).thenReturn("com.ngc.seaside.testeval3");
      Mockito.when(mockModel.getParent()).thenReturn(mockPackage);
      
      // Need at least one model
      Collection<IModel> models = new ArrayList<>();
      models.add(mockModel);
      
      ModelUtils.getAllFeatures(jellyFishCommandOptions, models, "src/test/gherkin");
   }
}
