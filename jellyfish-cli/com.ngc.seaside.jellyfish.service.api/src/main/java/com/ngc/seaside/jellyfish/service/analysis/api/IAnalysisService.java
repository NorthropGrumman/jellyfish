package com.ngc.seaside.jellyfish.service.analysis.api;

import java.util.List;

/**
 * A service for reporting and aggregating issues and other information about a System Descriptor project.
 */
public interface IAnalysisService {

   /**
    * Adds a finding to the analysis.
    * 
    * @param finding an issue found within the System Descriptor project
    */
   void addFinding(SystemDescriptorFinding finding);

   /**
    * Returns the list of findings added to this service.
    * 
    * @return the list of findings
    */
   List<SystemDescriptorFinding> getFindings();

}
