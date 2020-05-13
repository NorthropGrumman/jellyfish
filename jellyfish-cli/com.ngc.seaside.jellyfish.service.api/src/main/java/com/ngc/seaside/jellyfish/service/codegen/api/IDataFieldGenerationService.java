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
