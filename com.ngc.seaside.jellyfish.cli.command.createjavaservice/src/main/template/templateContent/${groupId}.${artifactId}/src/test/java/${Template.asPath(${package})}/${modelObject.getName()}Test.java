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
public class $modelObject.getName()Test {

   private $modelObject.getName() service;

   @Mock
   private ILogService logService;

   @Mock
   private IEventService eventService;

   @Before
   public void setup() throws Throwable {
      service = new ${modelObject.getName()}();
      service.setLogService(logService);
      service.setEventService(eventService);
      service.activate();
   }

#foreach ($method in $modelMethodList)
   @Test
   public $modelScenarioList.get($modelMethodList.indexOf($method)).getName()Test(){
       // TODO Auto-generated method stub
       throw new UnsupportedOperationException();
   }

#end

   @After
   public void cleanup() throws Throwable {
      service.deactivate();
   }
}
