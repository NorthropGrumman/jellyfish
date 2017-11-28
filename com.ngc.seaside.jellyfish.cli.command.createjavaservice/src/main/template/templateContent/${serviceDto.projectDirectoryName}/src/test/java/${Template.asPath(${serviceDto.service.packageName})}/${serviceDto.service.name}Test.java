package ${serviceDto.service.packageName};

import com.ngc.blocs.service.event.api.IEventService;
import com.ngc.blocs.service.log.api.ILogService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.fail;

@RunWith(MockitoJUnitRunner.class)
public class ${serviceDto.service.name}Test {

   private ${serviceDto.service.name} service;

   @Mock
   private ILogService logService;

   @Mock
   private IEventService eventService;

   @Before
   public void setup() throws Throwable {
      service = new ${serviceDto.service.name}();
      service.setLogService(logService);
      service.setEventService(eventService);
      service.activate();
   }

#foreach ($method in $serviceDto.service.methods)
   @Test
   public void ${method.name}Test(){
       // TODO Auto-generated method stub
       fail("not implemented");
   }

#end
   @After
   public void cleanup() throws Throwable {
      service.deactivate();
   }
}
