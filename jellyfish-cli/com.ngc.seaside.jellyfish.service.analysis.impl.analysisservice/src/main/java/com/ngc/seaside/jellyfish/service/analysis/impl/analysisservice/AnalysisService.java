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
