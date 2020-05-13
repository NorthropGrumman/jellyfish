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
package com.ngc.seaside.jellyfish.api;

import java.util.Objects;

/**
 * Default implementation of a usage parameter.
 *
 * @author justan.provence@ngc.com
 */
public class DefaultParameter<T> implements IParameter<T> {

   private String name;
   private T value;
   private String description = "";
   private ParameterCategory paramCategory = ParameterCategory.REQUIRED;

   /**
    * Creates a new parameter with the given nam
    *
    * @param name the name of the parameter.
    */
   public DefaultParameter(String name) {
      this.name = name;
   }

   /**
    * Creates a new parameter with the given name and value.
    *
    * @param name  the name of the parameter
    * @param value the value of the parameter
    */
   public DefaultParameter(String name, T value) {
      this(name);
      this.value = value;
   }

   @Override
   public String getName() {
      return name;
   }

   @Override
   public String getStringValue() {
      return value == null ? "" : value.toString();
   }

   @Override
   public T getValue() {
      return value;
   }

   public DefaultParameter<T> setValue(T value) {
      this.value = value;
      return this;
   }

   @Override
   public String getDescription() {
      return description;
   }

   public DefaultParameter<T> setDescription(String description) {
      this.description = description;
      return this;
   }
   
   @Override
   public ParameterCategory getParameterCategory() {
      return paramCategory;
   }
   
   public DefaultParameter<T> setParameterCategory(ParameterCategory paramCategory) {
      this.paramCategory = paramCategory;
      return this;
   }
   
   public DefaultParameter<T> required() {
      this.paramCategory = ParameterCategory.REQUIRED;
      return this;
   }
   
   public DefaultParameter<T> optional() {
      this.paramCategory = ParameterCategory.OPTIONAL;
      return this;
   }
   
   public DefaultParameter<T> advanced() {
      this.paramCategory = ParameterCategory.ADVANCED;
      return this;
   }

   @Override
   public String toString() {
      return String.format("Name: %s, Required: %s, Value: %s", name, paramCategory, value);
   }

   @Override
   public boolean equals(Object obj) {
      if (obj == this) {
         return true;
      }
      if (!(obj instanceof IParameter)) {
         return false;
      }
      IParameter<?> that = (IParameter<?>) obj;
      return Objects.equals(name, that.getName())
             && Objects.equals(value, that.getValue())
             && Objects.equals(paramCategory, that.getParameterCategory());
   }

   @Override
   public int hashCode() {
      return Objects.hash(name, paramCategory, value);
   }
}
