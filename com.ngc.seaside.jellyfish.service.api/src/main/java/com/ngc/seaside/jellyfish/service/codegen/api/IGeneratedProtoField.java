package com.ngc.seaside.jellyfish.service.codegen.api;

/**
 * Interface for generating fields for protocol buffers
 */
public interface IGeneratedProtoField extends IGeneratedField {

   /**
    * @return the name of the protocol buffers field
    */
   String getProtoFieldName();
   
   /**
    * @return the type of the protocol buffers field
    */
   String getProtoType();
   
   /**
    * @return the {@link IGeneratedJavaField} representing the generated java code from this protocol buffers field 
    */
   IGeneratedJavaField getJavaField();

}
