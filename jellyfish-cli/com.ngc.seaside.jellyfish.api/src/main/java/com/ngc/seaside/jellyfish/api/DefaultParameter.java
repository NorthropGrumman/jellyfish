/**
 * Northrop Grumman Proprietary
 * ____________________________
 * Copyright (C) 2016, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 * NOTICE:  All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
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
   private boolean required;
   private ParameterCategory paramCategory;

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
   public boolean isRequired() {
      return required;
   }

   public DefaultParameter<T> setRequired(boolean required) {
      this.required = required;
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

   @Override
   public String toString() {
      return String.format("Name: %s, Required: %s, Value: %s", name, required, value);
   }

   @Override
   public boolean equals(Object obj) {
      if (obj == this) {
         return true;
      }
      if (!(obj instanceof IUsage)) {
         return false;
      }
      IParameter<?> that = (IParameter<?>) obj;
      return Objects.equals(name, that.getName())
             && Objects.equals(value, that.getValue())
             && Objects.equals(required, that.isRequired());
   }

   @Override
   public int hashCode() {
      return Objects.hash(name, required, value);
   }
}
