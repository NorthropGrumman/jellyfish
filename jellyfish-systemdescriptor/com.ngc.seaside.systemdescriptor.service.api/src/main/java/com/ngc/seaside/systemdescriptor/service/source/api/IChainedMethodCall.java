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

import java.lang.reflect.Executable;
import java.util.Optional;
import java.util.function.Function;

/**
 * Intermediate interface for selecting an element for finding its source location. Calling {@link #calling(Function)}
 * or any of the {@link #then(NoParameterMethodCall) then} methods will add a method call to this chain. Calling
 * {@link #getLocation()} will return the source location of the last element in the method call chain.
 * 
 * @param <T> the current type
 * @see ISourceLocatorService#with(Object)
 */
public interface IChainedMethodCall<T> {

   /**
    * Returns the source code location of the last element in this chain.
    * 
    * @return source code location
    * @throws UnknownSourceLocationException if unable to find the source location
    */
   ISourceLocation getLocation();

   /**
    * Adds steps in the method call chain using the given function.
    * 
    * <p>
    * Example:
    * 
    * <pre>
    * service.with(model)
    *        .then(m -> m.getMetadata().getJson().getValue("requirements"))
    *        .getLocation()
    * </pre>
    * </p>
    * 
    * <p/>
    * The {@code chainedCalls} lambda should only call methods declared in interfaces. Calling non-interface methods
    * within the lambda (such as {@link String#substring(int)} or {@link Optional#get()}) may throw an exception or
    * return unexpected results. If non-interface methods need to be called, use one of the
    * {@link #then(NoParameterMethodCall) then} methods.
    * 
    * @param chainedCalls function that calls chained methods from the input
    * @return a {@link IChainedMethodCall} of the function's return type
    * @throws UnknownSourceLocationException if any of the lambda's method calls cannot be used in a source code
    *            location chain
    * @throws IllegalStateException if there were any other exceptions
    */
   <O> IChainedMethodCall<O> calling(Function<? super T, ? extends O> chainedCalls);

   /**
    * Adds a step in the method call chain.
    * 
    * <p/>
    * Users of this interface will likely not need to call this method directly,
    * but instead use {@link #calling(Function)} or one of the {@link #then(NoParameterMethodCall) then} methods.
    * 
    * @param methodOrConstructor method or constructor
    * @param arguments arguments supplied to the method/constructor
    * @return a {@link IChainedMethodCall} of the method/constructor's return type
    * @throws UnknownSourceLocationException if the method/constructor cannot be used in a source code location chain
    * @throws IllegalStateException if there were any other exceptions
    */
   IChainedMethodCall<?> then(Executable methodOrConstructor, Object... arguments);

   /**
    * Adds a step in the method call chain. The given {@code methodReference} must be instantiated using a method
    * reference; e.g., {@code IModel::getName}. Using lambdas (e.g. {@code m -> m.getName()}) or anonymous classes
    * will throw an exception.
    * 
    * @param methodReference method reference
    * @return a {@link IChainedMethodCall} of the method call's return type
    * @throws UnknownSourceLocationException if the method cannot be used in a source code location chain
    * @throws IllegalStateException if {@code methodReference} was not instantiated using a method reference
    */
   @SuppressWarnings("unchecked")
   default <O> IChainedMethodCall<O> then(NoParameterMethodCall<? super T, ? extends O> methodReference) {
      return (IChainedMethodCall<O>) then(methodReference.getMethodOrConstructor());
   }

   /**
    * Adds a step in the method call chain. The given {@code methodReference} must be instantiated using a method
    * reference; e.g., {@code IModel::getLinkByName}. Using lambdas (e.g. {@code m -> m.getLinkByName("link1")}) or
    * anonymous classes will throw an exception.
    * 
    * @param methodReference method reference
    * @param input input to the method call
    * @return a {@link IChainedMethodCall} of the method call's return type
    * @throws UnknownSourceLocationException if the method cannot be used in a source code location chain
    * @throws IllegalStateException if {@code methodReference} was not instantiated using a method reference
    */
   @SuppressWarnings("unchecked")
   default <I, O> IChainedMethodCall<O> then(
            OneParameterMethodCall<? super T, ? super I, ? extends O> methodReference,
            I input) {
      return (IChainedMethodCall<O>) then(methodReference.getMethodOrConstructor(), input);
   }

   /**
    * Adds a step in the method call chain. The given {@code methodReference} must be instantiated using a method
    * reference; e.g., {@code String::substring}. Using lambdas (e.g. {@code s -> s.substring(1, 5)}) or anonymous
    * classes will throw an exception.
    * 
    * @param methodReference method reference
    * @param input1 first input to the method call
    * @param input2 second input to the method call
    * @return a {@link IChainedMethodCall} for the method call's return type
    * @throws UnknownSourceLocationException if the method cannot be used in a source code location chain
    * @throws IllegalStateException if {@code methodReference} was not instantiated using a method reference
    */
   @SuppressWarnings("unchecked")
   default <I1, I2, O> IChainedMethodCall<O> then(
            TwoParameterMethodCall<? super T, ? super I1, ? super I2, ? extends O> methodReference,
            I1 input1, I2 input2) {
      return (IChainedMethodCall<O>) then(methodReference.getMethodOrConstructor(), input1, input2);
   }

   /**
    * Functional interface for a method call with no parameters. This interface is meant
    * only to be instantiated using method references; e.g., {@code IModel::getName}.
    * 
    * @param <T> the type of the instance with which the method is called
    * @param <O> return type
    */
   @FunctionalInterface
   interface NoParameterMethodCall<T, O> extends SerializedFunctionalInterface {
      O methodCall(T instance) throws Exception;
   }

   /**
    * Functional interface for a method with one parameter. This interface is meant
    * only to be instantiated using method references; e.g., {@code INamedChildCollection::getByName}.
    * 
    * @param <T> the type of the instance with which the method is called
    * @param <I> parameter type
    * @param <O> return type
    */
   @FunctionalInterface
   interface OneParameterMethodCall<T, I, O> extends SerializedFunctionalInterface {
      O methodCall(T instance, I input) throws Exception;
   }

   /**
    * Functional interface for a method with two parameters. This interface is meant
    * only to be instantiated using method references; e.g., {@code String::substring}.
    * 
    * @param <T> the type of the instance with which the method is called
    * @param <I1> first parameter type
    * @param <I2> seconds parameter type
    * @param <O> return type
    */
   @FunctionalInterface
   interface TwoParameterMethodCall<T, I1, I2, O> extends SerializedFunctionalInterface {
      O methodCall(T instance, I1 input1, I2 input2) throws Exception;
   }
}
