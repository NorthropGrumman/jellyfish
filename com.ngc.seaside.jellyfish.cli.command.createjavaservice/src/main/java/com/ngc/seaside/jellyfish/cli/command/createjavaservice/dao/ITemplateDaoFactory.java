package com.ngc.seaside.jellyfish.cli.command.createjavaservice.dao;

import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

public interface ITemplateDaoFactory {

   TemplateDao newDao(IModel model, String packagez);
}
