package com.ngc.seaside.systemdescriptor.validation.api;

import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

/**
 * A context object passed to an {@link ISystemDescriptorValidator} during validation.  This context contains the object
 * that is being validated and allows for errors, warnings, and suggestions to be declared.  The object being validated
 * is an instance of one of the interfaces in the {@code com.ngc.seaside.systemdescriptor.model.api} package or
 * subpackages.
 * <p/>
 * Errors can be declared using {@link #declare(Severity, String, Object) declare}.
 * <pre>
 *    {@code
 *    ctx.declare(Severity.ERROR, "Name cannot contain spaces.", ctx.getObject()).getName()
 *    }
 * </pre>
 * This indicates that the name value of the context object is not valid.  Note any getter method may be called on the
 * context object returned by {@code declare} to indicate the value returned by that method is invalid.  There are some
 * limitations:
 * <pre>
 * <ol>
 *   <li>
 *     It is not possible to declare an error on the fully qualified name of a
 *     {@link IModel#getFullyQualifiedName() model} or {@link IData#getFullyQualifiedName() data}.
 *   </li>
 *   <li>
 *     The children (ie, the {@link com.ngc.seaside.systemdescriptor.model.api.INamedChild named children}) of
 *     an object cannot be declared to be invalid.
 *   </li>
 * </ol>
 * </pre>
 * For example, consider the following validator:
 * <pre>
 *    {@code
 *    public class MyValidator extends AbstractSystemDescriptorValidator {
 *       {@literal @}Override
 *       protected void validateModel(IValidationContext<IModel> context) {
 *         IModel model = context.getObject();
 *         IScenario scenario = model.getScenarios().getByName("foo").get();
 *         if(scenario.getGivens().size() > 10) {
 *           context.declare(Severity.ERROR, "To many given steps!", scenario).getGivenSteps();
 *         }
 *       }
 *    }
 *    }
 * </pre>
 * This will not compile because the object being validated is the {@code IModel}, not the {@code IScenario}.  The
 * current way validate the scenario is to below:
 * <pre>
 *    {@code
 *    public class MyValidator extends AbstractSystemDescriptorValidator {
 *       {@literal @}Override
 *       protected void validateScenario(IValidationContext<IScenario> context) {
 *         IScenario scenario = context.getObject();
 *         if(scenario.getGivens().size() > 10) {
 *           context.declare(Severity.ERROR, "To many given steps!", scenario).getGivens();
 *         }
 *         // Note you can get the parent model with
 *         IModel model = scenario.getParent();
 *       }
 *    }
 *    }
 * </pre>
 * Note you may examine parent objects of the objects being validated, but you may not always be able to examine the
 * children of the object because those items may not be parsed yet.
 *
 * @param <T> the type of object being validated
 */
public interface IValidationContext<T> {

   /**
    * Gets the object being validated.
    *
    * @return the object being validated
    */
   T getObject();

   /**
    * Declares an issue with given severity.  Be sure to call the getter method that contains the value that is invalid
    * on the returned object.
    *
    * @param severity the severity of the issue
    * @param message  the message to associate with the issue
    * @param object   the object that contains the invalid value
    * @param <S>      the type of the object that contains the invalid value
    * @return the object that contains the invalid value
    */
   <S> S declare(Severity severity, String message, S object);
}
