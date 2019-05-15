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
