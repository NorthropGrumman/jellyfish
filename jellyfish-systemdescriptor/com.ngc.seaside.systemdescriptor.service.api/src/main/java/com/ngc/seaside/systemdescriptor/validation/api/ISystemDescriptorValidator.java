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
