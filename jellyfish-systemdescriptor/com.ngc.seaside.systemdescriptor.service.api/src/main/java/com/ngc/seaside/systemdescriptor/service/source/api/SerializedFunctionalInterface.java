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

import java.io.Serializable;
import java.lang.invoke.MethodType;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Executable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Interface intending to be used as a functional interface that implements {@link Serializable} in order to use
 * reflection on a method reference. All serializable lambdas have a no-args method {@code writeReplace} that returns a
 * {@link SerializedLambda}; this information is used to determine the method or constructor specified when using a
 * method reference.
 */
interface SerializedFunctionalInterface extends Serializable {

   /**
    * Returns the method or constructor that the implementation of this functional interface calls.
    * 
    * @return the method that the implementation of this functional interface calls
    * @throws IllegalStateException if the method cannot be determined or if this implementation was not created from a
    *            method reference.
    */
   default Executable getMethodOrConstructor() {
      Method replaceMethod;
      SerializedLambda lambda;
      try {
         // Lambdas and method references have a writeReplace method that takes no arguments and returns an instance
         // of SerializedLambda
         replaceMethod = getClass().getDeclaredMethod("writeReplace");
         replaceMethod.setAccessible(true);
         lambda = (SerializedLambda) replaceMethod.invoke(this);
         if (lambda.getCapturingClass().equals(lambda.getImplClass())) {
            // lambda was used, not a method reference
            throw new IllegalArgumentException();
         }
      } catch (NoSuchMethodException | ClassCastException | IllegalArgumentException | InvocationTargetException e) {
         throw new IllegalStateException("A method reference of the form `ClassName::methodName` must be used");
      } catch (IllegalAccessException e) {
         throw new AssertionError(e);
      }
      ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
      String classname = lambda.getImplClass().replace('/', '.');
      Class<?> cls;
      try {
         cls = Class.forName(classname, false, classLoader);
      } catch (ClassNotFoundException e) {
         throw new IllegalStateException(e);
      }
      MethodType methodType = MethodType.fromMethodDescriptorString(lambda.getImplMethodSignature(),
               classLoader);
      Executable executable = null;
      if ("<init>".equals(lambda.getImplMethodName())) {
         try {
            executable = cls.getDeclaredConstructor(methodType.parameterArray());
         } catch (NoSuchMethodException e) {
            // ignore
         }
      } else {
         // Traverse the class's super classes to find the method
         while (cls != null) {
            try {
               executable = cls.getDeclaredMethod(lambda.getImplMethodName(), methodType.parameterArray());
               break;
            } catch (NoSuchMethodException e) {
               // ignore
            }
            cls = cls.getSuperclass();
         }
      }
      if (executable == null) {
         throw new IllegalStateException("Could not find method/constructor " + lambda.getImplMethodSignature());
      }
      if (executable.getDeclaringClass().isSynthetic() && !executable.getDeclaringClass().isAnonymousClass()
               && !executable.getDeclaringClass().isLocalClass()) {
         // method is a lambda, not a method reference
         throw new IllegalStateException("A method reference of the form `Classname::methodname` must be used");
      }
      return executable;
   }

}
