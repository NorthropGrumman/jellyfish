/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE: All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
 */
package com.ngc.seaside.systemdescriptor.service.source.api;

import com.ngc.seaside.systemdescriptor.service.source.api.IChainedMethodCall.NoParameterMethodCall;
import com.ngc.seaside.systemdescriptor.service.source.api.IChainedMethodCall.OneParameterMethodCall;
import com.ngc.seaside.systemdescriptor.service.source.api.IChainedMethodCall.TwoParameterMethodCall;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class SerializedFunctionalInterfaceTest {

   @Test
   public void testCorrectMethodsReturnedForMethodReferences() throws Exception {
      NoParameterMethodCall<String, Integer> methodCall1 = String::length;
      Method method1 = String.class.getMethod("length");
      assertEquals(method1, methodCall1.getMethodOrConstructor());

      OneParameterMethodCall<String, Integer, String> methodCall2 = String::substring;
      Method method2 = String.class.getMethod("substring", int.class);
      assertEquals(method2, methodCall2.getMethodOrConstructor());

      Method method3 = String.class.getMethod("valueOf", Object.class);
      NoParameterMethodCall<Object, String> methodCall3 = String::valueOf;
      assertEquals(method3, methodCall3.getMethodOrConstructor());

      NoParameterMethodCall<Collection<? extends String>, ArrayList<String>> constructorCall1 = ArrayList::new;
      Constructor<?> constructor1 = ArrayList.class.getConstructor(Collection.class);
      assertEquals(constructor1, constructorCall1.getMethodOrConstructor());
   }

   @Test
   public void testExceptionThrownForNonMethodReferences() throws Exception {
      try {
         NoParameterMethodCall<String, Integer> methodCall = str -> str.length();
         fail(methodCall.getMethodOrConstructor().toString());
      } catch (IllegalStateException e) {
         // pass
      }

      try {
         NoParameterMethodCall<String, Integer> methodCall = new NoParameterMethodCall<String, Integer>() {
            @Override
            public Integer methodCall(String instance) throws Exception {
               return null;
            }
         };
         fail(methodCall.getMethodOrConstructor().toString());
      } catch (IllegalStateException e) {
         // pass
      }

      try {
         OneParameterMethodCall<String, Integer, String> methodCall = (str, index) -> str.substring(index);
         fail(methodCall.getMethodOrConstructor().toString());
      } catch (IllegalStateException e) {
         // pass
      }

      try {
         OneParameterMethodCall<String, Integer, String> methodCall =
               new OneParameterMethodCall<String, Integer, String>() {
                  @Override
                  public String methodCall(String instance, Integer input) throws Exception {
                     return null;
                  }
               };
         fail(methodCall.getMethodOrConstructor().toString());
      } catch (IllegalStateException e) {
         // pass
      }

      try {
         OneParameterMethodCall<String, Integer, String> methodCall = (str, index) -> str.substring(index);
         fail(methodCall.getMethodOrConstructor().toString());
      } catch (IllegalStateException e) {
         // pass
      }

      try {
         TwoParameterMethodCall<String, Integer, Integer, String> methodCall =
               new TwoParameterMethodCall<String, Integer, Integer, String>() {
                  @Override
                  public String methodCall(String instance, Integer input1, Integer input2) throws Exception {
                     return null;
                  }
               };
         fail(methodCall.getMethodOrConstructor().toString());
      } catch (IllegalStateException e) {
         // pass
      }
   }

}
