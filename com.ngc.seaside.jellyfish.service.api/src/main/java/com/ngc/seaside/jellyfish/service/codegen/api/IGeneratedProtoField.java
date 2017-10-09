package com.ngc.seaside.jellyfish.service.codegen.api;

import com.google.protobuf.WireFormat;

/**
 * Interface for generating fields for protocol buffers
 */
public interface IGeneratedProtoField extends IGeneratedField {

   /**
    * @return the name of the protocol buffers field
    */
   String getJavaFieldName();
   
   /**
    * @return the type of the protocol buffers field
    */
   WireFormat.FieldType getProtoType();
   
   /**
    * @return the {@link IGeneratedJavaField} representing the generated java code from this protocol buffers field 
    */
   IGeneratedJavaField getJavaField();

}
