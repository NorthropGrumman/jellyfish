package com.ngc.seaside.jellyfish.cli.command.analyze.inputsoutputs;

import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.utilities.command.AbstractJellyfishCommand;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IEnumeration;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.traversal.ModelPredicates;
import com.ngc.seaside.systemdescriptor.model.api.traversal.Traversals;

import java.util.Arrays;
import java.util.Collection;

public abstract class AbstractJellyfishAnalysisCommand extends AbstractJellyfishCommand {

   protected AbstractJellyfishAnalysisCommand(String name) {
      super(name);
   }

   @Override
   protected void doRun() {
      // Analysis commands have 3 modes of operation:
      // 1) A specific model was specified with the "model" argument.  This means only scan the given model.
      // 2) The models which match the given stereotypes if the "stereotypes" argument is used.
      // 3) Every element in the project if none of the above options where used.
      if (getOptions().getParameters().containsParameter(CommonParameters.MODEL.getName())) {
         analyzeModel(getModel());
      } else if (getOptions().getParameters().containsParameter(CommonParameters.STEREOTYPES.getName())) {
         analyzeStereotypedModels();
      } else {
         analyzeEntireProject();
      }
   }

   protected void analyzeStereotypedModels() {
      String[] stereotypes = getOptions().getParameters()
            .getParameter(CommonParameters.STEREOTYPES.getName())
            .getStringValue()
            .split(",");
      Collection<IModel> models = Traversals.collectModels(
            getOptions().getSystemDescriptor(),
            ModelPredicates.withAnyStereotype(Arrays.asList(stereotypes)));
      models.forEach(this::analyzeModel);
   }

   protected void analyzeEntireProject() {
      for(IPackage packagez : getOptions ().getSystemDescriptor().getPackages()) {
         packagez.getModels().forEach(this::analyzeModel);
         packagez.getData().forEach(this::analyzeData);
         packagez.getEnumerations().forEach(this::analyzeEnumeration);
      }
   }

   protected void analyzeModel(IModel model) {
   }

   protected void analyzeData(IData data) {

   }

   protected void analyzeEnumeration(IEnumeration enumeration) {

   }
}
