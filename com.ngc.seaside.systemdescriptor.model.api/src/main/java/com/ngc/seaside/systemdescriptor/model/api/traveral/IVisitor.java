package com.ngc.seaside.systemdescriptor.model.api.traveral;

import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.link.IModelLink;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;

public interface IVisitor  {

  default void visitMetadata(IVisitorContext ctx, IMetadata metadata) {
  }

  default void visitDataField(IVisitorContext ctx, IDataField field) {
  }

  default void visitData(IVisitorContext ctx, IData data) {
  }

  default void visitLink(IVisitorContext ctx, IModelLink<?> link) {
  }

  default void visitScenario(IVisitorContext ctx, IScenario scenario) {
  }

  default void visitDataReferenceField(IVisitorContext ctx, IDataReferenceField field) {
  }

  default void visitModelReferenceField(IVisitorContext ctx, IModelReferenceField field) {
  }

  default void visitModel(IVisitorContext ctx, IModel model) {
  }

  default void visitPackage(IVisitorContext ctx, IPackage systemDescriptorPackage) {
  }

  default void visitSystemDescriptor(IVisitorContext ctx, ISystemDescriptor systemDescriptor) {
  }

}
