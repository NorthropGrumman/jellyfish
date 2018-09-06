/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
 */
package com.ngc.seaside.systemdescriptor.model.impl.basic;

import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.impl.basic.data.Data;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.Model;

import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SystemDescriptorTest {

   private Model timerModel;
   private Model alarmModel;
   private Model clockHandModel;
   private Data timeData;
   private Data dateData;
   private Data dateTimeData;
   private SystemDescriptor sd;

   @Before
   public void setUp() throws Exception {
      String p1NameStr = new String("clocks.datatypes");
      String p2NameStr = new String("clocks.models");
      String p3NameStr = new String("clocks.models.submodels");
      Package p1 = new Package(p1NameStr);
      Package p2 = new Package(p2NameStr);
      Package p3 = new Package(p3NameStr);

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

      String m3NameStr = new String("ClockHand");
      clockHandModel = new Model(m3NameStr);
      p3.addModel(clockHandModel);

      sd = new SystemDescriptor();

      // fix this to auto populate the parent of the Package
      sd.addPackage(p1);
      sd.addPackage(p2);
      sd.addPackage(p3);
   }

   @Test
   public void testFindModelString() {
      String fullyQName = timerModel.getFullyQualifiedName();
      Optional<IModel> model = sd.findModel(fullyQName);
      assertTrue("Model not found!", model.isPresent());
      assertEquals("Model found but does not match!", timerModel, model.get());

      fullyQName = alarmModel.getFullyQualifiedName();
      model = sd.findModel(fullyQName);
      assertTrue("Model not found!", model.isPresent());
      assertEquals("Model found but does not match!", alarmModel, model.get());

      fullyQName = clockHandModel.getFullyQualifiedName();
      model = sd.findModel(fullyQName);
      assertTrue("Model not found!", model.isPresent());
      assertEquals("Model found but does not match!", clockHandModel, model.get());

      fullyQName = "bad.name";
      assertFalse("Bad name did not return a null object in findModel", sd.findModel(fullyQName).isPresent());

   }

   @Test
   public void testFindModelStringString() {
      Optional<IModel> model = sd.findModel("clocks.models", "Timer");
      assertTrue("Model not found!", model.isPresent());
      assertEquals("Model found but does not match!", timerModel, model.get());

      model = sd.findModel("clocks.models", "Alarm");
      assertTrue("Model not found!", model.isPresent());
      assertEquals("Model found but does not match!", alarmModel, model.get());

      model = sd.findModel("clocks.models.submodels", "ClockHand");
      assertTrue("Model not found!", model.isPresent());
      assertEquals("Model found but does not match!", clockHandModel, model.get());

      assertFalse("Bad package name and model name pair did not return a null object",
                  sd.findModel("clocks.datatypes", "boom").isPresent());
   }

   @Test
   public void testFindDataString() {
      String fullyQName = timeData.getFullyQualifiedName();
      Optional<IData> data = sd.findData(fullyQName);
      assertTrue("Data not found!", data.isPresent());
      assertEquals("Data found but does not match!", timeData, data.get());

      fullyQName = dateData.getFullyQualifiedName();
      data = sd.findData(fullyQName);
      assertTrue("Data not found!", data.isPresent());
      assertEquals("Data found but does not match!", dateData, data.get());

      fullyQName = dateTimeData.getFullyQualifiedName();
      data = sd.findData(fullyQName);
      assertTrue("Data not found!", data.isPresent());
      assertEquals("Data found but does not match!", dateTimeData, data.get());

      fullyQName = "bad.name.data";
      assertFalse("Bad name did not return a null object in findData", sd.findData(fullyQName).isPresent());
   }

   @Test
   public void testFindDataStringString() {
      Optional<IData> data = sd.findData("clocks.datatypes", "Time");
      assertTrue("Data not found!", data.isPresent());
      assertEquals("Data found but does not match!", timeData, data.get());

      data = sd.findData("clocks.datatypes", "Date");
      assertTrue("Data not found!", data.isPresent());
      assertEquals("Data found but does not match!", dateData, data.get());

      data = sd.findData("clocks.datatypes", "DateTime");
      assertTrue("Data not found!", data.isPresent());
      assertEquals("Data found but does not match!", dateTimeData, data.get());

      assertFalse("Bad name did not return a null object in findData",
                  sd.findData("clocks.datatypes", "DateTimeStamp").isPresent());
   }

}
