/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
 */
package com.ngc.seaside.systemdescriptor.model.api.model;

import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Default implementation of the IDataPath interface.  Maintains all the data associated with the IDataPath.  Obtain
 * instances of this class with the overloaded {@link IDataPath#of(IModel, String) of} operations.
 */
class DataPath implements IDataPath {

   private final boolean isOutput;
   private final IDataReferenceField start;
   private final List<IDataField> fields;

   /**
    * Creates a data path starting from the given model and its input output.
    *
    * @param model model
    * @param start input/output field name of the model
    * @param path  nested data field name
    * @param paths other nested data field names
    */
   DataPath(IModel model, String start, String path, String... paths) {
      Objects.requireNonNull(model, "model may not be null!");
      Objects.requireNonNull(path, "path may not be null!");
      Objects.requireNonNull(paths, "paths may not be null!");
      Optional<IDataReferenceField> startField;
      if ((startField = model.getInputs().getByName(start)).isPresent()) {
         this.start = startField.get();
         this.isOutput = false;
      } else if ((startField = model.getOutputs().getByName(start)).isPresent()) {
         this.start = startField.get();
         this.isOutput = true;
      } else {
         throw new IllegalArgumentException(
               "Unknown input/output in model " + model.getFullyQualifiedName() + ": " + start);
      }
      List<String> pathsList = new ArrayList<>(paths.length + 1);
      pathsList.add(path);
      pathsList.addAll(Arrays.asList(paths));

      IData data = this.start.getType();
      StringBuilder pathStr = new StringBuilder(start);
      List<IDataField> fields = new ArrayList<>(pathsList.size());

      for (String part : pathsList) {
         if (part == null) {
            throw new IllegalArgumentException("paths may not contain a null value!");
         }
         if (data == null) {
            throw new IllegalArgumentException("Cannot resolve nested data field from model "
                                               + model.getFullyQualifiedName() + ": " + pathStr.toString());
         }
         pathStr.append('.').append(part);

         IDataField field;
         while (true) {
            Optional<IDataField> optionalField = data.getFields().getByName(path);
            if (optionalField.isPresent()) {
               field = optionalField.get();
               break;
            }
            if (data.getExtendedDataType().isPresent()) {
               data = data.getExtendedDataType().get();
            } else {
               throw new IllegalArgumentException(
                     "Cannot resolve nested data field from model " + model.getFullyQualifiedName() + ": "
                     + pathStr.toString());
            }
         }

         fields.add(field);
         if (field.getType() == DataTypes.DATA) {
            data = field.getReferencedDataType();
         } else {
            data = null;
         }
      }

      this.fields = Collections.unmodifiableList(fields);
   }

   /**
    * Creates a data path starting from the given model with the given path.
    *
    * @param model model
    * @param path  nested data field names (separated by '{@code .}')
    */
   DataPath(IModel model, String path) {
      Objects.requireNonNull(model, "model may not be null!");
      Objects.requireNonNull(path, "path may not be null!");
      if (path.isEmpty()) {
         throw new IllegalArgumentException("path may not be empty!");
      }
      String[] parts = path.split("\\.");
      if (parts.length <= 1) {
         throw new IllegalArgumentException("path must contain at least two parts");
      }
      Optional<IDataReferenceField> startField;
      if ((startField = model.getInputs().getByName(parts[0])).isPresent()) {
         this.start = startField.get();
         this.isOutput = false;
      } else if ((startField = model.getOutputs().getByName(parts[0])).isPresent()) {
         this.start = startField.get();
         this.isOutput = true;
      } else {
         throw new IllegalArgumentException(
               "Unknown input/output in model " + model.getFullyQualifiedName() + ": " + parts[0]);
      }

      IData data = this.start.getType();
      List<IDataField> fields = new ArrayList<>(parts.length - 1);
      for (int i = 1; i < parts.length; i++) {
         if (data == null) {
            throw new IllegalArgumentException(
                  "Cannot resolve nested data field from model " + model.getFullyQualifiedName() + ": "
                  + Arrays.asList(parts).subList(0, i).stream().collect(Collectors.joining(".")));
         }
         IDataField field;
         while (true) {
            Optional<IDataField> optionalField = data.getFields().getByName(parts[i]);
            if (optionalField.isPresent()) {
               field = optionalField.get();
               break;
            }
            if (data.getExtendedDataType().isPresent()) {
               data = data.getExtendedDataType().get();
            } else {
               throw new IllegalArgumentException(
                     "Cannot resolve nested data field from model " + model.getFullyQualifiedName() + ": "
                     + Arrays.asList(parts).subList(0, i + 1).stream().collect(Collectors.joining(".")));
            }
         }

         fields.add(field);
         if (field.getType() == DataTypes.DATA) {
            data = field.getReferencedDataType();
         } else {
            data = null;
         }
      }

      this.fields = Collections.unmodifiableList(fields);

   }

   /**
    * Creates a data path with the given fields.
    *
    * @param start  start field
    * @param fields nested fields
    */
   DataPath(IDataReferenceField start, Collection<IDataField> fields) {
      Objects.requireNonNull(start, "start may not be null!");
      Objects.requireNonNull(fields, "fields may not be null!");
      if (fields.isEmpty()) {
         throw new IllegalArgumentException("fields may not be empty!");
      }
      List<IDataField> asList = new ArrayList<>(fields.size());
      for (IDataField field : fields) {
         Objects.requireNonNull(field, "fields may not contain a null value!");
         asList.add(field);
      }
      List<IDataField> asUnmodifiableList = Collections.unmodifiableList(asList);
      this.start = start;
      this.fields = asUnmodifiableList;
      this.isOutput = start.getParent().getOutputs().contains(start);

   }

   @Override
   public IDataReferenceField getStart() {
      return start;
   }

   @Override
   public boolean isOutput() {
      return isOutput;
   }

   @Override
   public List<IDataField> getElements() {
      return fields;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      }
      if (!(o instanceof DataPath)) {
         return false;
      }
      DataPath path = (DataPath) o;
      return Objects.equals(isOutput, path.isOutput)
             && Objects.equals(start, path.start)
             && Objects.equals(fields, path.fields);
   }

   @Override
   public int hashCode() {
      return Objects.hash(isOutput, start, fields);
   }

   @Override
   public String toString() {
      return "DataPath[model=" + start.getParent().getFullyQualifiedName() + ", path=" + start.getName() + '.'
             + fields.stream().map(IDataField::getName).collect(Collectors.joining(".")) + "]";
   }

}
