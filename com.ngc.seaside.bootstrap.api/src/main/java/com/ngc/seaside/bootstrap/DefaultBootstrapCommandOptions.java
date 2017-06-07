package com.ngc.seaside.bootstrap;

import com.ngc.seaside.command.api.IParameter;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class DefaultBootstrapCommandOptions implements IBootstrapCommandOptions {

   private List<IParameter> parameters = new ArrayList<>();

   @Override
   public List<IParameter> getParameters() {
      return parameters;
   }

   public void setParameters(List<IParameter> parameters) {
      this.parameters = parameters;
   }
}
