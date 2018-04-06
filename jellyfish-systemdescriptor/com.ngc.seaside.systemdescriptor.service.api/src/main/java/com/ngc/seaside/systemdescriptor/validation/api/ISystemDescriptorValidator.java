package com.ngc.seaside.systemdescriptor.validation.api;

/**
 * Allows for clients to define additional validation rules.  Validators are registered with the {@link
 * com.ngc.seaside.systemdescriptor.service.api.ISystemDescriptorService#addValidator(ISystemDescriptorValidator) system
 * descriptor service}.  Note that some application configurations will automatically register validators, so there is
 * no need to call {@code addValidator} to directly.
 * <p/>
 * Validators must check the type of the object being validated to know how to cast it.  To make this easier, most
 * validators should extend {@link AbstractSystemDescriptorValidator}.
 *
 * @see IValidationContext
 * @see AbstractSystemDescriptorValidator
 */
public interface ISystemDescriptorValidator {

   /**
    * Invoked to validate an object.
    *
    * @param context the context object that contains the object being validated.
    */
   void validate(IValidationContext<?> context);
}
