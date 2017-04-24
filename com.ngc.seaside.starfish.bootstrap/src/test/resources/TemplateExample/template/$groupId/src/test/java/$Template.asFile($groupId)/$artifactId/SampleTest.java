package ${groupId}.${artifactId};

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SampleTest
{

   private Sample sample;

   @Before
   public void before()
   {
      sample = new Sample();
   }

   @Test
   public void test()
   {
      Assert.assertEquals("Hello World", sample.getHelloWorld());
   }

}
