package ${dto.packageName};

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
public class ${dto.className}Test {

   private ${dto.className} service;

   @Mock
   private ILogService logService;

   @Mock
   private IEventService eventService;

   @Before
   public void setup() throws Throwable {
      service = new ${dto.className}();
      service.setLogService(logService);
      service.setEventService(eventService);
      service.activate();
   }

#foreach ($method in $dto.methods)
   @Test
   public void ${method.methodName}Test(){
       // TODO Auto-generated method stub
       fail("not implemented");
   }

#end

   @After
   public void cleanup() throws Throwable {
      service.deactivate();
   }
}
