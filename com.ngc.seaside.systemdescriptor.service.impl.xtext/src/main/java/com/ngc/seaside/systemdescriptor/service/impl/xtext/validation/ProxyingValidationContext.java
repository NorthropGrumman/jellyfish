package com.ngc.seaside.systemdescriptor.service.impl.xtext.validation;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.extension.IValidatorExtension;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.IUnwrappable;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.IUnwrappableCollection;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.exception.UnconvertableTypeException;
import com.ngc.seaside.systemdescriptor.validation.api.IValidationContext;
import com.ngc.seaside.systemdescriptor.validation.api.Severity;

import org.eclipse.emf.ecore.EObject;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.Collections;

public class ProxyingValidationContext<T> implements IValidationContext<T> {

   private final T object;
   private final IValidatorExtension.ValidationHelper validationHelper;

   public ProxyingValidationContext(T object,
                                    IValidatorExtension.ValidationHelper validationHelper) {
      this.object = object;
      this.validationHelper = validationHelper;
   }

   @Override
   public T getObject() {
      return object;
   }

   @SuppressWarnings("unchecked")
   @Override
   public T declare(Severity severity, String message, T object) {
      Preconditions.checkNotNull(severity, "severity may not be null!");
      Preconditions.checkNotNull(message, "message may not be null!");
      Preconditions.checkNotNull(object, "object may not be null!");
      // Safe because this is a proxy object.
      return (T) Proxy.newProxyInstance(object.getClass().getClassLoader(),
                                        object.getClass().getInterfaces(),
                                        (p, m, a) -> handleProxyInvocation(p, m, a, severity, message));
   }

   private Object handleProxyInvocation(Object proxy,
                                        Method method,
                                        Object[] args,
                                        Severity severity,
                                        String message) throws Throwable {
      for (EObject xtext : getEObjects(object)) {
         switch (severity) {
            case ERROR:
               validationHelper.error(message, xtext, ValidationBridgeUtil.getFeature(object, xtext, method));
               break;
            default:
               throw new UnconvertableTypeException(severity);
         }
      }

      // Just pass through the invocation.
      return method.invoke(object, args);
   }

   private static Collection<? extends EObject> getEObjects(Object o) {
      if (o instanceof IUnwrappable) {
         return Collections.singletonList(((IUnwrappable) o).unwrap());
      }
      if (o instanceof IUnwrappableCollection) {
         return ((IUnwrappableCollection<?>) o).unwrapAll();
      }
      throw new IllegalArgumentException(String.format(
            "object must be wrapping an XText object and be of type %s or %s, but found %s!",
            IUnwrappable.class.getName(),
            IUnwrappableCollection.class.getName(),
            o.getClass().getName()));
   }
}
