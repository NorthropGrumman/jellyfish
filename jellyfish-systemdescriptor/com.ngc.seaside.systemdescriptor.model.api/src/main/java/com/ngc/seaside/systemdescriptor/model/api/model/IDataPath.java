package com.ngc.seaside.systemdescriptor.model.api.model;

import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;

import java.util.Collection;
import java.util.List;

/**
 * Represents a path of data fields referenced by a particular model. Note that instances of paths are directly
 * associated with specific models.
 * Consider the path given by in model:
 * <pre>
 * systemTrack.header.correlationEventId
 * </pre>
 * This path might be part of some scenario located in a model called {@code FooService}. In this example {@code
 * systemTrack} is the name of an input data field whose type is {@code SystemTrack}. {@code header} is a field of
 * system track whose type is {@code Metadata}. Finally, metadata contains a primitive string field called {@code
 * correlationEventId}.
 * This path my be created programmatically like this:
 * <pre>
 *    IModel fooService = systemDescriptor.findModel("FooService").orElse(null);
 *    IDataReferenceField systemTrackField = fooService.getInputs().getByName("systemTrack");
 *    IDataField headerField = systemTrackField.getType().getFields().getByName("header");
 *    IDataField correlationEventIdField = headerField.getReferencedDataType()
 *        .getFields().getByName("correlationEventId")
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
    * Returns {@code true} if the starting element of this path, which begins with an {@code IDataReferenceField}, is an
    * output of the model, and {@code false} if it is an input of the model.
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
   default IDataField getEnd() {
      return getElements().get(getElements().size() - 1);
   }

   /**
    * Returns an IDataPath with the given path parameters.
    *
    * @param start  reference field that is the start of the path
    * @param fields elements of the path
    * @return an IDataPath with the given path parameters
    */
   static IDataPath of(IDataReferenceField start, Collection<IDataField> fields) {
      return new DataPath(start, fields);
   }

   /**
    * Creates a data path starting from the given model with the given path.
    *
    * @param model model
    * @param path  nested data field names (separated by '{@code .}')
    */
   static IDataPath of(IModel model, String path) {
      return new DataPath(model, path);
   }

   /**
    * Creates a data path starting from the given model and its input output.
    *
    * @param model model
    * @param start input/output field name of the model
    * @param path  nested data field name
    * @param paths other nested data field names
    */
   static IDataPath of(IModel model, String start, String path, String... paths) {
      return new DataPath(model, start, path, paths);
   }
}

