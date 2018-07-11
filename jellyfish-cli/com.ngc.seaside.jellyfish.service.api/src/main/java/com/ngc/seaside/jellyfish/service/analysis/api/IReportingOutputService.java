package com.ngc.seaside.jellyfish.service.analysis.api;

public interface IReportingOutputService {

   /**
    * Converts a raw source message to some output suitable for reporting.
    *
    * @param message the raw source message to convert
    * @return the reporting output
    */
   String convert(String message);

}
