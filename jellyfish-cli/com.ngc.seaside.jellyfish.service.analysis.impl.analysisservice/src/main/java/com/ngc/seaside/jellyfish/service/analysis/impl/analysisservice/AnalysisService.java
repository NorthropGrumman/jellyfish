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
package com.ngc.seaside.jellyfish.service.analysis.impl.analysisservice;

import com.ngc.seaside.jellyfish.service.analysis.api.IAnalysisService;
import com.ngc.seaside.jellyfish.service.analysis.api.ISystemDescriptorFindingType;
import com.ngc.seaside.jellyfish.service.analysis.api.SystemDescriptorFinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AnalysisService implements IAnalysisService {

   private final List<SystemDescriptorFinding<ISystemDescriptorFindingType>> findings = new ArrayList<>();
   
   @SuppressWarnings("unchecked")
   @Override
   public void addFinding(SystemDescriptorFinding<? extends ISystemDescriptorFindingType> finding) {
      findings.add((SystemDescriptorFinding<ISystemDescriptorFindingType>) finding);
   }

   @Override
   public List<SystemDescriptorFinding<ISystemDescriptorFindingType>> getFindings() {
      return Collections.unmodifiableList(findings);
   }

}
