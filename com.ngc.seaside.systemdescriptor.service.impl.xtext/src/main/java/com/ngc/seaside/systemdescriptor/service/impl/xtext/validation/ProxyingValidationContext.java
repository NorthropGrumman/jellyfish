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

   /**
    * The object that is being validated.  This is some type of "wrapped" XText object with is either an instance of
    * {@link IUnwrappable} or {@link IUnwrappableCollection}.
    */
   private final T object;

   /**
    * The validation helper as supplied by the JellyFish DSL itself.
    */
   private final IValidatorExtension.ValidationHelper validationHelper;

   /**
    * Creates a new {@code ProxyingValidationContext} for the given object.
    *
    * @param object           the wrapped XText object that is being validated
    * @param validationHelper the supplied validation helper
    */
   public ProxyingValidationContext(T object,
                                    IValidatorExtension.ValidationHelper validationHelper) {
      Preconditions.checkArgument(object instanceof IUnwrappable || object instanceof IUnwrappableCollection,
                                  "object must be an instance of %s or %s!",
                                  IUnwrappable.class.getName(),
                                  IUnwrappableCollection.class.getName());
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
      // Return a dynamic proxy of the wrapped object.  This allows us to "record" the methods the validator calls on
      // the object when declaring an issue.  Note the proxy will actually pass through to the wrapped object so the
      // actual call will complete as normal.  In this way, this is really a method interceptor.
      // Safe because this is a proxy object.
      return (T) Proxy.newProxyInstance(object.getClass().getClassLoader(),
                                        object.getClass().getInterfaces(),
                                        (p, m, a) -> interceptMethodCall(p, m, a, severity, message));
   }

   /**
    * Intercepts the invocation of a method on a proxy object.  Uses the name of the method to determine which
    * structural feature the validation issue should be attached to.  Once that is complete, the call continues to the
    * target object.
    *
    * @param proxy    the proxy object
    * @param method   the method called
    * @param args     the arguments to the method
    * @param severity the severity level of the issue
    * @param message  the message associated with issue
    * @return the result of the invocation on the target object
    */
   private Object interceptMethodCall(Object proxy,
                                      Method method,
                                      Object[] args,
                                      Severity severity,
                                      String message) throws Throwable {
      // Get the XText objects that are being wrapped.  In most cases, only one object is being wrapped.  However, in
      // the case of IPackage, multiple XText packages are being wrapped at the same time.
      for (EObject xtext : getEObjects(object)) {
         switch (severity) {
            case ERROR:
               // Get the structural feature and use the validation help to actually declare the issue.
               validationHelper.error(message, xtext, ValidationBridgeUtil.getFeature(object, xtext, method));
               break;
            case WARNING:
               validationHelper.warning(message, xtext, ValidationBridgeUtil.getFeature(object, xtext, method));
               break;
            case SUGGESTION:
               validationHelper.info(message, xtext, ValidationBridgeUtil.getFeature(object, xtext, method));
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
