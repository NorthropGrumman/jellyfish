package com.ngc.seaside.jellyfish;

import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.IParameter;
import com.ngc.seaside.command.api.IParameterCollection;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.impl.provider.JellyFishCommandProvider;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.service.api.IParsingIssue;
import com.ngc.seaside.systemdescriptor.service.api.IParsingResult;
import com.ngc.seaside.systemdescriptor.service.api.ISystemDescriptorService;
import com.ngc.seaside.systemdescriptor.service.api.ParsingException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

@RunWith(MockitoJUnitRunner.class)
public class JellyFishTest {

   @Before
   public void before() throws Throwable {
      String ngFwHome = Paths.get(System.getProperty("user.dir"), "build", "resources", "test")
               .toAbsolutePath()
               .toString();
      System.setProperty("NG_FW_HOME", ngFwHome);


   }

   @Test
   public void validSDProjectParsedTest() {
      
      String root = Paths.get(System.getProperty("user.dir"), "build", "resources", "test", "valid-project")
            .toAbsolutePath()
            .toString();

      JellyFish.main(new String[]{"-Droot=" + root});
   }
   
   //TODO I don't think this proves anything
   @Test
   public void systemDescriptorLoadedIntoMemoryTest() {

      String root = Paths.get(System.getProperty("user.dir"), "build", "resources", "test", "valid-project")
            .toAbsolutePath()
            .toString();
      
      Path path = Paths.get("C:\\Dev\\repositories\\jellyfish-cli\\com.ngc.seaside.jellyfish\\build\\resources\\test\\valid-project");

      JellyFishCommandProvider cmdProvider = new JellyFishCommandProvider();
      
      IParameterCollection mockParameterCollection = mock(IParameterCollection.class);
      IParameter parameter;
      DefaultParameter defParam = new DefaultParameter("root", true);
      defParam.setValue("C:\\Dev\\repositories\\jellyfish-cli\\com.ngc.seaside.jellyfish\\build\\resources\\test\\valid-project");
      parameter = getIParam(defParam);
      
      when(mockParameterCollection.getParameter("root")).thenReturn(parameter);
      
      ISystemDescriptorService sdService = mock(ISystemDescriptorService.class);
      cmdProvider.setISystemDescriptorService(sdService);
      
      IParsingResult parsingResult = mock(IParsingResult.class);
      
      when(sdService.parseProject(path)).thenReturn(parsingResult);
      ISystemDescriptor sysDescriptor = mock(ISystemDescriptor.class);
      when(parsingResult.isSuccessful()).thenReturn(true);
      when(parsingResult.getSystemDescriptor()).thenReturn(sysDescriptor);
      
      
      IJellyFishCommandOptions jellyFishCmdOpts = cmdProvider.convert(mockParameterCollection);
      ISystemDescriptor sd = jellyFishCmdOpts.getSystemDescriptor();
      Assert.assertTrue(sd != null);
   }

     //TODO How do I prove that this failed?   
//   @Test
//   public void invalidSdProjectStructureParsed() {
//
//      String root = Paths.get(System.getProperty("user.dir"), "build", "resources", "test", "invalid-dir-struct-project")
//            .toAbsolutePath()
//            .toString();
//      
//      JellyFish.main(new String[]{"-Droot=" + root});
//   }

   private IParameter getIParam(DefaultParameter defParam) {
      return defParam;
   }

   @After
   public void after() throws Throwable {
      System.clearProperty("NG_FW_HOME");
   }
}
