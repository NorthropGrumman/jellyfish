package com.ngc.seaside.jellyfish.service.analysis.api;

import java.util.List;

public interface IAnalysisService {

   void addFinding(SystemDescriptorFinding o);
   
   void addReport(String markdown);
   
   List<SystemDescriptorFinding> getFindings();
   
   List<String> getReports();
   
}
