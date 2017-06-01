package com.ngc.seaside.systemdescriptor.model.impl.basic;

import static org.junit.Assert.*;

import com.ngc.seaside.systemdescriptor.model.impl.basic.data.Data;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.Model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SystemDescriptorTest {

   private Model timerModel;
   private Model alarmModel;
   private Data timeData;
   private Data dateData;
   private Data dateTimeData;
   private SystemDescriptor sd;
   
   @Before
   public void setUp() throws Exception {
      String p1NameStr = new String("clocks.datatypes");
      String p2NameStr = new String("clocks.models");
      Package p1 = new Package(p1NameStr);
      Package p2 = new Package(p2NameStr);
      
      // add some models (for p2) and data (for p1)
      
      String d1NameStr = new String("Time");
      String d2NameStr = new String("Date");
      String d3NameStr = new String("DateTime");
      timeData = new Data(d1NameStr);
      dateData = new Data(d2NameStr);
      dateTimeData = new Data(d3NameStr);
      p1.addData(timeData);
      p1.addData(dateData);
      p1.addData(dateTimeData);
      
      String m1NameStr = new String("Timer");
      String m2NameStr = new String("Alarm");
      timerModel = new Model(m1NameStr);
      alarmModel = new Model(m2NameStr);
      p2.addModel(timerModel);
      p2.addModel(alarmModel);
      
      sd = new SystemDescriptor();
      
      // fix this to auto populate the parent of the Package
      sd.addPackage(p1);
      sd.addPackage(p2);
   }

   @Test
   public void testFindModelString() {
      String fullyQName = timerModel.getFullyQualifiedName();
      assertTrue("Model not found!",
         sd.findModel(fullyQName).isPresent());
      //assertEquals("Model found but does not match!",
      //   timerModel,
      //   sd.findModel(fullyQName).get());
      
      fullyQName = alarmModel.getFullyQualifiedName();
      assertTrue("Model not found!",
         sd.findModel(fullyQName).isPresent());
      //assertEquals("Model found but does not match!",
      //   alarmModel,
      //   sd.findModel(fullyQName).get());
      
      fullyQName = "bad.name";
      assertFalse("Bad name did not return a null object in findModel",
         sd.findModel(fullyQName).isPresent());
   }

   @Test
   public void testFindModelStringString() {
      assertTrue("Model not found!",
         sd.findModel("clocks.models", "Timer").isPresent());
      //assertEquals("Model found but does not match!",
      //   timerModel,
      //   sd.findModel("clocks.models", "Timer").get());
      
      assertTrue("Model not found!",
         sd.findModel("clocks.models", "Alarm").isPresent());
      //assertEquals("Model found but does not match!",
      //   alarmModel,
      //   sd.findModel("clocks.models", "Alarm").get());
      
      assertFalse("Bad package name and model name pair did not return a null object",
         sd.findModel("clocks.datatypes", "boom").isPresent());
   }

   @Test
   public void testFindDataString() {
      String fullyQName = timeData.getFullyQualifiedName();
      assertTrue("Data not found!",
         sd.findData(fullyQName).isPresent());
      //assertEquals("Data found but does not match!",
      //   timeData,
      //   sd.findModel(fullyQName).get());
      
      fullyQName = dateData.getFullyQualifiedName();
      assertTrue("Data not found!",
         sd.findData(fullyQName).isPresent());
      //assertEquals("Data found but does not match!",
      //   dateData,
      //   sd.findModel(fullyQName).get());
      
      fullyQName = dateTimeData.getFullyQualifiedName();
      assertTrue("Data not found!",
         sd.findData(fullyQName).isPresent());
      //assertEquals("Data found but does not match!",
      //   dateTimeData,
      //   sd.findModel(fullyQName).get());
      
      fullyQName = "bad.name.data";
      assertFalse("Bad name did not return a null object in findData",
         sd.findData(fullyQName).isPresent());
   }

   @Test
   public void testFindDataStringString() {
      assertTrue("Data not found!",
         sd.findData("clocks.datatypes", "Time").isPresent());
      //assertEquals("Data found but does not match!",
      //   timeData,
      //   sd.findModel("clocks.datatypes", "Time").get());
      
      assertTrue("Data not found!",
         sd.findData("clocks.datatypes", "Date").isPresent());
      //assertEquals("Data found but does not match!",
      //   dateData,
      //   sd.findModel("clocks.datatypes", "Date").get());
      
      assertTrue("Data not found!",
         sd.findData("clocks.datatypes", "DateTime").isPresent());
      //assertEquals("Data found but does not match!",
      //   dateTimeData,
      //   sd.findModel("clocks.datatypes", "DateTime").get());
      
      assertFalse("Bad name did not return a null object in findData",
         sd.findData("clocks.datatypes", "DateTimeStamp").isPresent());
   }

}
