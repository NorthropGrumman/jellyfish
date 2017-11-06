package com.ngc.seaside.jellyfish.service.api;

import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Represents a path of data fields referenced by a particular model. Note that instances of paths are directly
 * associated with specific models.
 *
 * Consider the path given by in model:
 * 
 * <pre>
 * systemTrack.header.correlationEventId
 * </pre>
 * 
 * This path might be part of some scenario located in a model called {@code FooService}. In this example {@code
 * systemTrack} is the name of an input data field whose type is {@code SystemTrack}. {@code header} is a field of
 * system track whose type is {@code Metadata}. Finally, metadata contains a primitive string field called {@code
 * correlationEventId}.
 *
 * This path my be created programmatically like this:
 * 
 * <pre>
 *    IModel fooService = systemDescriptor.findModel("FooService").orElse(null);
 *    IDataReferenceField systemTrackField = fooService.getInputs().getByName("systemTrack");
 *    IDataField headerField = systemTrackField.getType().getFields().getByName("header");
 *    IDataField correlationEventIdField = headerField.getReferencedDataType().getFields().getByName("correlationEventId")
 *    IDataPath path = IDataPath.of(systemTrackField
 *                                  headerField,
 *                                  correlationEventIdField);
 *    // path.evaluate() points to the correlationEventId field of Metadata.
 *    assert systemDescriptor.findData("Metadata").orElse(null)
 *      .getFields().getByName("correlationEventId") == path.evaluate()
 * </pre>
 */
public interface IDataPath {

   /**
    * Gets the starting element of this path, which begins with an {@code IDataReferenceField} contained by some model.
    */
   IDataReferenceField getStart();

   /**
    * Returns {@code true} if the starting element of this path, which begins with an {@code IDataReferenceField}, is an output of the model, and {@code false} if it is an input of the model.
    */
   boolean isOutput();

   /**
    * Gets the list of data fields that make up this path, in the order they should be evaluated. The last entry is the
    * result of the path.
    *
    * @return the list of data fields that make up this path, in the order they should be evaluated.
    */
   List<IDataField> getElements();

   /**
    * Returns the last data field referenced in the path.
    */
   default IDataField evaluate() {
      return getElements().get(getElements().size() - 1);
   }

   /**
    * Returns an IDataPath with the given path parameters.
    * 
    * @param start reference field that is the start of the path
    * @param field first element of the path
    * @param fields other elements of the path
    * @return an IDataPath with the given path parameters
    */
   public static IDataPath of(IDataReferenceField start, IDataField field, IDataField... fields) {
      Objects.requireNonNull(fields, "fields may not be null!");
      Collection<IDataField> all = new ArrayList<>(fields.length);
      all.add(field);
      all.addAll(Arrays.asList(fields));
      return of(start, all);
   }

   /**
    * Returns an IDataPath with the given path parameters.
    * 
    * @param start reference field that is the start of the path
    * @param fields elements of the path
    * @return an IDataPath with the given path parameters
    */
   public static IDataPath of(IDataReferenceField start, Collection<IDataField> fields) {
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
      final boolean isOutput = start.getParent().getOutputs().contains(start);
      return new IDataPath() {
         @Override
         public IDataReferenceField getStart() {
            return start;
         }

         @Override
         public List<IDataField> getElements() {
            return asList;
         }

         @Override
         public boolean isOutput() {
            return isOutput;
         }
      };
   }
}
