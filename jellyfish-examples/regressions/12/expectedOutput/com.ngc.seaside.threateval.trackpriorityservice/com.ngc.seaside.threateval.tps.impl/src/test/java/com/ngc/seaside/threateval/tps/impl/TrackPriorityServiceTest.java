/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 * 
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
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
package com.ngc.seaside.threateval.tps.impl;

import com.ngc.blocs.service.log.api.ILogService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.fail;

@RunWith(MockitoJUnitRunner.class)
public class TrackPriorityServiceTest {

   private TrackPriorityService service;

   @Mock
   private ILogService logService;

   @Before
   public void setup() throws Throwable {
      service = new TrackPriorityService();
      service.setLogService(logService);
      service.activate();
   }

   @Test
   public void doCalculateConsolidatedTrackPriorityTest() throws Exception {
      // TODO: implement this
      fail("not implemented");
   }

   @Test
   public void doCalculateConsolidatedTrackPriorityWhenTrackDroppedTest() throws Exception {
      // TODO: implement this
      fail("not implemented");
   }

   @Test
   public void doGetTrackPrioritiesTest() throws Exception {
      // TODO: implement this
      fail("not implemented");
}

   @After
   public void cleanup() throws Throwable {
      service.deactivate();
   }
}
