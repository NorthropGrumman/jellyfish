package com.ngc.seaside.jellyfish.cli.command.createjavaservice.dto;

import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

public interface ITemplateDtoFactory {

   TemplateDto newDto(IModel model, String packagez);
}
