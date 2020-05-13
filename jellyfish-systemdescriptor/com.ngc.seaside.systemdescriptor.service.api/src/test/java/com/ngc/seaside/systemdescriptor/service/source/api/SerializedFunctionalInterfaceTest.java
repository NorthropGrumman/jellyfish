/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
