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
package com.ngc.seaside.jellyfish.service.analysis.api;

import java.util.List;

/**
 * A service for reporting and aggregating issues and other information about a System Descriptor project.
 */
public interface IAnalysisService {

   /**
    * Adds a finding to the analysis.
    * 
    * @param finding a finding within the System Descriptor project
    */
   void addFinding(SystemDescriptorFinding<? extends ISystemDescriptorFindingType> finding);

   /**
    * Returns the list of findings added to this service.
    * 
    * @return the list of findings
    */
   List<SystemDescriptorFinding<ISystemDescriptorFindingType>> getFindings();

}
