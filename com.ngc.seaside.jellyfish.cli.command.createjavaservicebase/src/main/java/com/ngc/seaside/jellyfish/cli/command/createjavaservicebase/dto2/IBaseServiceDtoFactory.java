package com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto2;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

public interface IBaseServiceDtoFactory {

   BaseServiceDto newDto(IJellyFishCommandOptions options, IModel model);
}
