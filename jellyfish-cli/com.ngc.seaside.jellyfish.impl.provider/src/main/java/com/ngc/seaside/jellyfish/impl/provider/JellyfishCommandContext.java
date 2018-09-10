/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
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
package com.ngc.seaside.jellyfish.impl.provider;

import com.google.common.base.Preconditions;

import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.IParameter;
import com.ngc.seaside.jellyfish.api.IParameterCollection;

/**
 * A simple structure for storing information about how Jellyfish was invoked to perform a command.
 */
public class JellyfishCommandContext {

   /**
    * The name of the command being executed.
    */
   private final String command;

   /**
    * The unmodified set of parameters that Jellyfish was originally invoked with.
    */
   private final IParameterCollection originalParameters;

   /**
    * The parameters to run Jellyfish with which include the original parameters plus any parameters added later that
    * were automatically calculated.
    */
   private final DefaultParameterCollection parameters;

   /**
    * Creates a new context.
    *
    * @param command    the command that is being executed
    * @param parameters the original parameters as given by the user
    */
   public JellyfishCommandContext(String command, IParameterCollection parameters) {
      this.command = Preconditions.checkNotNull(command, "command may not be null!");
      this.originalParameters = Preconditions.checkNotNull(parameters, "parameters may not be null!");
      this.parameters = new DefaultParameterCollection(this.originalParameters);
   }

   /**
    * Returns true if the {@link CommonParameters#GROUP_ARTIFACT_VERSION} parameter was specified with the original
    * parameters, false otherwise.
    *
    * @return true if the {@link CommonParameters#GROUP_ARTIFACT_VERSION} parameter was specified
    */
   public boolean isGavSpecified() {
      return originalParameters.containsParameter(CommonParameters.GROUP_ARTIFACT_VERSION.name());
   }

   /**
    * Gets the command that is being executed
    *
    * @return the command being executed
    */
   public String getCommand() {
      return command;
   }

   /**
    * Gets the original, unmodified set of parameters that Jellyfish was initially invoked with.
    *
    * @return the original, unmodified set of parameters that Jellyfish was initially invoked with
    */
   public IParameterCollection getOriginalParameters() {
      return originalParameters;
   }

   /**
    * Gets the parameters to run Jellyfish with.  These parameters include the {@link #getOriginalParameters() original
    * parameters} plus any parameters {@link #addParameter(IParameter) added} to the context after it was created.
    *
    * @return the parameters to run Jellyfish with
    */
   public IParameterCollection getParameters() {
      return parameters;
   }

   /**
    * Adds a parameter to this context.  Parameters added this way will not be included in the {@link
    * #getOriginalParameters() original parameters}.
    *
    * @param parameter the parameter to add
    */
   public void addParameter(IParameter<?> parameter) {
      parameters.addParameter(Preconditions.checkNotNull(parameter, "parameter may not be null!"));
   }
}
