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
package com.ngc.seaside.command.api;

import java.util.Objects;

/**
 * Default implementation of a usage parameter.
 *
 * @author justan.provence@ngc.com
 */
public class DefaultParameter implements IParameter {

   private String name;
   private String value = "";
   private boolean required;

   /**
    * Constructor.
    *
    * @param name      the name of the parameter.
    * @param required  true if the parameter is required.
    */
   public DefaultParameter(String name, boolean required) {
      this.name = name;
      this.required = required;
   }

   @Override
   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   @Override
   public String getValue() {
      return value;
   }

   public void setValue(String value) {
      this.value = value;
   }

   @Override
   public boolean isRequired() {
      return required;
   }

   public void setRequired(boolean required) {
      this.required = required;
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
      IParameter that = (IParameter) obj;
      return Objects.equals(name, that.getName()) &&
             Objects.equals(value, that.getValue()) &&
             Objects.equals(required, that.isRequired());
   }

   @Override
   public int hashCode() {
      return Objects.hash(name, required, value);
   }
}
