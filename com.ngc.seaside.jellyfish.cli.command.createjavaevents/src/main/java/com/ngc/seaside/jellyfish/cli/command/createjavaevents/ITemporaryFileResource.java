package com.ngc.seaside.jellyfish.cli.command.createjavaevents;

import com.ngc.blocs.service.resource.api.IReadableResource;

import java.nio.file.Path;

public interface ITemporaryFileResource extends IReadableResource {

   Path getTemporaryFile();
}
