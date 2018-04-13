package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.dto;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @param <T> the configuration dto used for generating the configuration template for this transport provider
 */
public interface ITransportProviderConfigDto<T> {

   /**
    * Returns the {@link TransportProviderDto} for the given configuration dto.
    * 
    * @param dto configuration dto returned by {@link #getConfigurationDto}
    * @return the {@link TransportProviderDto} for the given configuration dto
    */
   TransportProviderDto getTransportProviderDto(T dto);

   /**
    * Gets the configuration dto for this transport provider.
    * 
    * <p />
    * When generating the template for this transport provider, the template will be given the variable {@link dto} set to the returned value.
    * 
    * @param serviceConfigDto the base configuration dto for the generated service
    * @param options jellyfish options
    * @param model model
    * @param topicsClassName fully qualified name of the transport topics class
    * @param topics map of topics within the transport topics class with their associated data reference field
    * @return the configuration dto, or {@link Optional#empty()} if this configuration is unused
    */
   Optional<T> getConfigurationDto(GeneratedServiceConfigDto serviceConfigDto,
            IJellyFishCommandOptions options,
            IModel model,
            String topicsClassName,
            Map<String, IDataReferenceField> topics);

   /**
    * Returns the name of the template used to generate the configuration class.
    * 
    * @return the name of the template used to generate the configuration class
    */
   String getTemplate();

   /**
    * Returns the dependencies for the transport service in the form {@code "groupId:artifactId"}.
    * 
    * @param topic if {@code true}, includes the dependencies needed for the transport topic
    * @param provider if {@code true}, includes the dependencies needed for the transport provider
    * @param module if {@code true}, includes the dependencies needed for the transport Guice module
    * @return the dependencies for this transport configuration
    */
   Set<String> getDependencies(boolean topic, boolean provider, boolean module);

   /**
    * Returns a map of extra parameters needed to generate the template. The parameter {@code dto} will already be
    * included using what is returned by {@link #getConfigurationDto}.
    * 
    * @return a map of extra parameters needed to generate the template
    */
   default Map<String, Object> getExtraTemplateParameters() {
      return Collections.emptyMap();
   }

}
