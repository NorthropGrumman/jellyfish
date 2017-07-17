package com.ngc.seaside.systemdescriptor.service.help.api;

import com.ngc.seaside.systemdescriptor.scenario.api.ScenarioStepVerb;

import java.util.Optional;

/**
 * The help service is a simple service that serves as a repository for help information that can be presented to the
 * user.  Implementations are free to load help content however they want to.
 */
public interface IHelpService {

   /**
    * Gets a description of the given step verb.
    *
    * @param verb the verb to get help for
    * @return an optional that contains the description of the verb or an empty optional if there is no description for
    * the verb
    */
   Optional<String> getDescription(ScenarioStepVerb verb);
}
