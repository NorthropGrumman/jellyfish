package com.ngc.seaside.jellyfish.cli.command.createjavaservicepubsubbridge.dto;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

public interface IPubSubBridgeDtoFactory {

   PubSubBridgeDto newDto(IJellyFishCommandOptions options, IModel model);
}
