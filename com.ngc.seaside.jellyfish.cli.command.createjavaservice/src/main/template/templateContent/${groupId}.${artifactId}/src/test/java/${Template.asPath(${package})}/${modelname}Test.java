package ${package};

import com.ngc.blocs.service.event.api.IEventService;
import com.ngc.blocs.service.log.api.ILogService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ${modelname}Test {

   private $modelname} service;

   @Mock
   private ILogService logService;

   @Mock
   private IEventService eventService;

   @Before
   public void setup() throws Throwable {
      service = new ${modelname}();
      service.setLogService(logService);
      service.setEventService(eventService);
      service.activate();
   }

   @Test
   public void testService() {
      // TODO Auto-generated method stub
   }

   @After
   public void cleanup() throws Throwable {
      service.deactivate();
   }
}
