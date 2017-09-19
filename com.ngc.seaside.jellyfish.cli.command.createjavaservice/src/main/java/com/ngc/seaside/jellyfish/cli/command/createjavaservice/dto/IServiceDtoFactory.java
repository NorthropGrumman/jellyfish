package com.ngc.seaside.jellyfish.cli.command.createjavaservice.dto;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

public interface IServiceDtoFactory {

   ServiceDto newDto(IJellyFishCommandOptions options, IModel model);
}
