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
package com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained;

import com.google.common.base.Preconditions;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.model.ModelChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.location.IDetailedSourceLocation;
import com.ngc.seaside.systemdescriptor.service.source.api.IChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.source.api.UnknownSourceLocationException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

/**
 * Abstract class for implementations of {@link IChainedMethodCall}. Implementations should use one of the
 * {@link #register(Executable, NoParameterChainedMethodCallIntercept)} to correlate a method with a function to
 * get the corresponding IChainedMethodCall.
 * 
 * @see ModelChainedMethodCall for an example
 */
public abstract class AbstractChainedMethodCall<T> implements IChainedMethodCall<T> {

   private final Map<MethodSignature, Function<Object[], IChainedMethodCall<?>>> registry = new HashMap<>();
   protected final ChainedMethodCallContext context;
   protected final T instance;

   protected AbstractChainedMethodCall(T instance, ChainedMethodCallContext context) {
      this.context = Preconditions.checkNotNull(context, "context cannot be null");
      this.instance = instance;
   }

   @Override
   public abstract IDetailedSourceLocation getLocation();

   /**
    * Registers the given method or constructor that will call the given function intercept with the
    * method/constructor's arguments to get the chained method call.
    */
   protected void register(Executable methodOrConstructor, Function<Object[], IChainedMethodCall<?>> intercept) {
      registry.put(new MethodSignature(methodOrConstructor), intercept);
   }

   /**
    * Registers the given method or constructor that will call the given function to get the chained method call.
    * 
    * <p>
    * This method is a convenience method to allow registering methods/constructors with lambdas.
    * </p>
    */
   protected <O> void register(Executable methodOrConstructor, NoParameterChainedMethodCallIntercept<O> methodCall) {
      register(methodOrConstructor, (Function<Object[], IChainedMethodCall<?>>) args -> {
         if (args != null && args.length != 0) {
            throw new IllegalStateException(
                     "Invalid arguments for method/constructor " + methodOrConstructor + ": " + Arrays.toString(args));
         }
         return methodCall.methodCall();
      });
   }

   /**
    * Registers the given method or constructor that will call the given function with the
    * method/constructor's single argument to get the chained method call.
    * 
    * <p>
    * This method is a convenience method to allow registering methods/constructors with lambdas.
    * </p>
    */
   @SuppressWarnings("unchecked")
   protected <O, I> void register(Executable methodOrConstructor,
            OneParameterChainedMethodCallIntercept<O, I> methodCall) {
      register(methodOrConstructor, (Function<Object[], IChainedMethodCall<?>>) args -> {
         if (args.length != 1) {
            throw new IllegalStateException(
                     "Invalid arguments for method/constructor " + methodOrConstructor + ": " + Arrays.toString(args));
         }
         return methodCall.methodCall((I) args[0]);
      });
   }

   /**
    * Registers the given method or constructor that will call the given function with the
    * method/constructor's two arguments to get the chained method call.
    * 
    * <p>
    * This method is a convenience method to allow registering methods/constructors with lambdas.
    * </p>
    */
   @SuppressWarnings("unchecked")
   protected <O, I1, I2> void register(Executable methodOrConstructor,
            TwoParameterChainedMethodCallIntercept<O, I1, I2> methodCall) {
      register(methodOrConstructor, (Function<Object[], IChainedMethodCall<?>>) args -> {
         if (args.length != 2) {
            throw new IllegalStateException(
                     "Invalid arguments for method/constructor " + methodOrConstructor + ": " + Arrays.toString(args));
         }
         return methodCall.methodCall((I1) args[0], (I2) args[1]);
      });
   }

   @Override
   public IChainedMethodCall<?> then(Executable methodOrConstructor, Object... arguments) {
      Function<Object[], IChainedMethodCall<?>> intercept = registry.get(new MethodSignature(methodOrConstructor));
      if (intercept == null) {
         throw new UnknownSourceLocationException(
                  "There is no source location associated with the method/constructor " + methodOrConstructor);
      }
      return intercept.apply(arguments);
   }

   @SuppressWarnings("unchecked")
   @Override
   public <O> IChainedMethodCall<O> calling(Function<? super T, ? extends O> chainedCalls) {
      if (instance == null) {
         throw new UnknownSourceLocationException("Cannot get source location");
      }
      @SuppressWarnings("rawtypes")
      AtomicReference<IChainedMethodCall> lastMethodCall = new AtomicReference<>();
      T input = getProxy(instance, this, lastMethodCall);
      chainedCalls.apply(input);
      @SuppressWarnings("rawtypes")
      IChainedMethodCall nextMethodCall = lastMethodCall.get();
      if (nextMethodCall == null) {
         throw new UnknownSourceLocationException();
      }
      return nextMethodCall;
   }

   @SuppressWarnings({ "unchecked", "rawtypes" })
   private static <T> T getProxy(T instance, IChainedMethodCall<T> methodCall,
            AtomicReference<IChainedMethodCall> lastMethodCall) {
      lastMethodCall.set(methodCall);
      T proxy = (T) Proxy.newProxyInstance(instance.getClass().getClassLoader(),
            getInterfaces(instance), (proxyInstance, method, args) -> {
               Object returned = method.invoke(instance, args);
               IChainedMethodCall nextMethodCall;
               try {
                  nextMethodCall = methodCall.then(method, args);
                  lastMethodCall.set(nextMethodCall);
               } catch (UnknownSourceLocationException e) {
                  return returned;
               }
               if (method.getReturnType().isInterface()) {
                  return getProxy(returned, nextMethodCall, lastMethodCall);
               } else {
                  return returned;
               }
            });
      return proxy;
   }

   private static <T> Class<?>[] getInterfaces(T instance, Class<?>... extraInterfaces) {
      Preconditions.checkNotNull(instance, "instance cannot be null");
      Set<Class<?>> interfaces = new LinkedHashSet<>();
      Class<?> cls = instance.getClass();
      while (cls != null) {
         interfaces.addAll(Arrays.asList(cls.getInterfaces()));
         cls = cls.getSuperclass();
      }
      for (Class<?> extraInterface : extraInterfaces) {
         if (extraInterface.isInterface()) {
            interfaces.add(extraInterface);
         } else {
            while (extraInterface != null) {
               interfaces.addAll(Arrays.asList(extraInterface.getInterfaces()));
               extraInterface = extraInterface.getSuperclass();
            }
         }
      }
      return interfaces.toArray(new Class<?>[interfaces.size()]);
   }

   protected static interface NoParameterChainedMethodCallIntercept<O> {
      IChainedMethodCall<O> methodCall();
   }

   protected static interface OneParameterChainedMethodCallIntercept<O, I> {
      IChainedMethodCall<O> methodCall(I input);
   }

   protected static interface TwoParameterChainedMethodCallIntercept<O, I1, I2> {
      IChainedMethodCall<O> methodCall(I1 input1, I2 input2);
   }

   /**
    * Container for methods/constructors. This class evaluates to methods as equal if their names and parameter types
    * are equal, regardless of the return type or declaring class since the return type can be more specific than that
    * of a super-class or interface.
    */
   private static class MethodSignature {

      private final String name;
      private final Class<?>[] parameters;
      private final boolean isVarArgs;

      public MethodSignature(Executable methodOrConstructor) {
         Preconditions.checkNotNull(methodOrConstructor, "method/constructor cannot be null");
         this.parameters = methodOrConstructor.getParameterTypes();
         this.isVarArgs = methodOrConstructor.isVarArgs();
         if (methodOrConstructor instanceof Method) {
            Method method = ((Method) methodOrConstructor);
            this.name = method.getName();
         } else {
            Constructor<?> constructor = ((Constructor<?>) methodOrConstructor);
            this.name = constructor.getDeclaringClass().getCanonicalName();
         }
      }

      @Override
      public boolean equals(Object o) {
         if (!(o instanceof MethodSignature)) {
            return false;
         }
         MethodSignature that = (MethodSignature) o;
         return Objects.equals(this.name, that.name)
                  && Arrays.equals(this.parameters, that.parameters)
                  && this.isVarArgs == that.isVarArgs;
      }

      @Override
      public int hashCode() {
         return Objects.hash(name, Arrays.hashCode(parameters), isVarArgs);
      }
   }

}
