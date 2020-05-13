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
package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultVariableManager implements IVariableManager {

   private List<Map<Object, VariableName>> variableScopes;

   public DefaultVariableManager() {
      variableScopes = new ArrayList<>();
      variableScopes.add(new HashMap<>());
   }

   @Override
   public void add(String variableName) {
      for (int i = variableScopes.size() - 1; i >= 0; i--) {
         Map<Object, VariableName> scope = variableScopes.get(i);
         if (scope.get(variableName) != null) {
            throw new IllegalStateException("Variable name " + variableName + " has already been assigned.");
         }
         for (VariableName variable : scope.values()) {
            if (variable.isResolved() && variable.getActualName().equals(variableName)) {
               throw new IllegalStateException("Variable name " + variableName + " has already been assigned.");
            }
         }
      }
      VariableName name = new VariableName(null, variableName);
      variableScopes.get(variableScopes.size() - 1).put(variableName, name);
   }

   @Override
   public void add(Object key, String requestedVariableName) {
      for (int i = variableScopes.size() - 1; i >= 0; i--) {
         Map<Object, VariableName> scope = variableScopes.get(i);
         VariableName name = scope.get(key);
         if (name != null) {
            if (!name.isResolved()) {
               throw new IllegalStateException("Add variable has alread been added for the key " + key);
            }
         }
      }
      VariableName name = new VariableName(requestedVariableName);
      variableScopes.get(variableScopes.size() - 1).put(key, name);
   }

   @Override
   public String get(Object key) {
      VariableName name = null;
      for (int i = variableScopes.size() - 1; i >= 0; i--) {
         Map<Object, VariableName> scope = variableScopes.get(i);
         name = scope.get(key);
         if (name != null) {
            break;
         }
      }
      if (name == null) {
         name = new VariableName("a");
         variableScopes.get(variableScopes.size() - 1).put(key, name);
      }

      if (!name.isResolved()) {
         String requested = name.getRequestedName();
         int index = 1;
         outer:
         while (true) {
            for (int i = variableScopes.size() - 1; i >= 0; i--) {
               Map<Object, VariableName> scope = variableScopes.get(i);
               for (VariableName variable : scope.values()) {
                  if (variable.isResolved() && variable.getActualName().equals(requested)) {
                     requested = name.getRequestedName() + index;
                     index++;
                     continue outer;
                  }
               }
            }
            break;
         }
         name.setActualName(requested);
      }
      return name.getActualName();
   }

   @Override
   public void enterScope() {
      variableScopes.add(new HashMap<>());
   }

   @Override
   public void exitScope() {
      variableScopes.remove(variableScopes.size() - 1);
      if (variableScopes.isEmpty()) {
         variableScopes.add(new HashMap<>());
      }
   }

   @Override
   public void clear() {
      variableScopes.clear();
      variableScopes.add(new HashMap<>());
   }

   private static class VariableName {

      private String requestedName;
      private String actualName;

      public VariableName(String requestedName) {
         this(requestedName, null);
      }

      public VariableName(String requestedName, String actualName) {
         this.requestedName = requestedName;
         this.actualName = actualName;
      }

      public boolean isResolved() {
         return actualName != null;
      }

      public void setActualName(String actualName) {
         this.actualName = actualName;
      }

      public String getRequestedName() {
         return requestedName;
      }

      public String getActualName() {
         return actualName;
      }

   }

}
