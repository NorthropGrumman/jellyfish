package com.ngc.seaside.jellyfish.service.codegen.api;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.codegen.api.java.IGeneratedJavaField;
import com.ngc.seaside.jellyfish.service.codegen.api.proto.IGeneratedProtoField;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;

/**
 * Service for determining field names, types, and method names when generating code from {@link IDataField}.
 */
public interface IDataFieldGenerationService {

   /**
    * Gets a description of the generated java field in the events service from the given data field.
    * 
    * @param options options the options the current command is being executed with
    * @param field the data field
    * @return a description of the generated field from the given data field
    */
   IGeneratedJavaField getEventsField(IJellyFishCommandOptions options, IDataField field);

   /**
    * Gets a description of the generated protocol buffers field in the messages service from the given data field.
    * 
    * @param options options the options the current command is being executed with
    * @param field the data field
    * @return a description of the generated field from the given data field
    */
   IGeneratedProtoField getMessagesField(IJellyFishCommandOptions options, IDataField field);

}
