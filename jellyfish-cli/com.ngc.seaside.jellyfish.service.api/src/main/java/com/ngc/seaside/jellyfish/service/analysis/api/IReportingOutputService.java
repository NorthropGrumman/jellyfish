package com.ngc.seaside.jellyfish.service.analysis.api;

import com.ngc.seaside.systemdescriptor.service.source.api.ISourceLocation;

public interface IReportingOutputService {

   /**
    * Converts a raw source message to some output suitable for reporting.
    *
    * @param message the raw source message to convert
    * @return the reporting output
    */
   String convert(String message);

   /**
    * Converts the given source location to some output suitable for reporting.
    *
    * @param sourceLocation the source location
    * @return the reporting output
    */
   String convert(ISourceLocation sourceLocation);

}
