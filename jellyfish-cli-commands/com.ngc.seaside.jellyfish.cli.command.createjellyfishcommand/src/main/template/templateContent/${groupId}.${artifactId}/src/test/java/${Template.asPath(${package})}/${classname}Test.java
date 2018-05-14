package ${package};

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedBuildManagementService;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedPackageNamingService;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedProjectNamingService;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedTemplateService;

import org.junit.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ${classname}Test {

   private ${classname} cmd = new ${classname}();

   @Mock
   private ILogService logService;

   private MockedBuildManagementService buildManagementService = new MockedBuildManagementService();

   private MockedTemplateService templateService = new MockedTemplateService();

   @Before
   public void setup() {
      cmd.setLogService(logService);
      cmd.setBuildManagementService(buildManagementService);
      cmd.setTemplateService(templateService);
      cmd.setProjectNamingService(new MockedProjectNamingService());
      cmd.setPackageNamingService(new MockedPackageNamingService());
      cmd.activate();
   }

   @Test
   public void testCommand() {
      // TODO Auto-generated method stub
   }

   @After
   public void cleanup() {
      cmd.deactivate();
   }
}
