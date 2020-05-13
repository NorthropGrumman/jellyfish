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
package com.ngc.seaside.systemdescriptor.model.impl.xtext.exception;

import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;

import org.eclipse.emf.ecore.EClass;

/**
 * A type of exception that can be used to indicate a particular XText object was not found.
 */
public class XtextObjectNotFoundException extends RuntimeException {

   /**
    * Creates a new exception.
    */
   public XtextObjectNotFoundException(EClass eclass, String fullyQualifiedName) {
      super(String.format("could not find XText %s type with name %s.",
                          eclass.getName().toLowerCase(),
                          fullyQualifiedName));
   }

   /**
    * Creates a new exception for the given model.
    */
   public static XtextObjectNotFoundException forModel(IModel model) {
      return new XtextObjectNotFoundException(SystemDescriptorPackage.Literals.MODEL, model.getFullyQualifiedName());
   }

   /**
    * Creates a new exception for the given data object.
    */
   public static XtextObjectNotFoundException forData(IData data) {
      return new XtextObjectNotFoundException(SystemDescriptorPackage.Literals.DATA, data.getFullyQualifiedName());
   }

   /**
    * Creates a new exception for the given enum object.
    */
   public static XtextObjectNotFoundException forEnum(IData enumeration) {
      return new XtextObjectNotFoundException(SystemDescriptorPackage.Literals.ENUMERATION,
                                              enumeration.getFullyQualifiedName());
   }
}
