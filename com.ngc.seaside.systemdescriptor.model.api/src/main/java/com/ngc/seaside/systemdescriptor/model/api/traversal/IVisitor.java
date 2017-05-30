package com.ngc.seaside.systemdescriptor.model.api.traversal;

import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;

/**
 * A visitor that traverses all objects within a {@link ISystemDescriptor}.  A visitor may return a particular result
 * from the traversal with {@link IVisitorContext#setResult(Object)}.  A visitor may also abort a traversal with {@link
 * IVisitorContext#stop()}.
 *
 * Note that {@link com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata medadata} and individual {@link
 * com.ngc.seaside.systemdescriptor.model.api.model.link.IModelLink model links} are not directly visited.
 *
 * @see ISystemDescriptor#traverse(IVisitor)
 */
public interface IVisitor {

  /**
   * Invoked to visit a data field of a data object.
   *
   * @param ctx   the visitor context
   * @param field the data field
   * @see IDataField
   */
  default void visitDataField(IVisitorContext ctx, IDataField field) {
  }

  /**
   * Invoked to visit a data type.
   *
   * @param ctx  the visitor context
   * @param data the data type
   * @see IData
   */
  default void visitData(IVisitorContext ctx, IData data) {
  }

  /**
   * Invoked to visit a scenario of a model.
   *
   * @param ctx      the visitor context
   * @param scenario the scenario
   * @see IScenario
   */
  default void visitScenario(IVisitorContext ctx, IScenario scenario) {
  }

  /**
   * Invoked to visit an input field that references data.
   *
   * @param ctx   the visitor context
   * @param field the field
   * @see IDataReferenceField
   */
  default void visitDataReferenceFieldAsInput(IVisitorContext ctx, IDataReferenceField field) {
  }

  /**
   * Invoked to visit an output field that references data.
   *
   * @param ctx   the visitor context
   * @param field the field
   * @see IDataReferenceField
   */
  default void visitDataReferenceFieldAsOutput(IVisitorContext ctx, IDataReferenceField field) {
  }

  /**
   * Invoked to visit a requirement field that references another model.
   *
   * @param ctx   the visitor context
   * @param field the field
   * @see IModelReferenceField
   */
  default void visitModelReferenceFieldAsRequirement(IVisitorContext ctx, IModelReferenceField field) {
  }

  /**
   * Invoked to visit a part field that references another model.
   *
   * @param ctx   the visitor context
   * @param field the field
   * @see IModelReferenceField
   */
  default void visitModelReferenceFieldAsPart(IVisitorContext ctx, IModelReferenceField field) {
  }

  /**
   * Invoked to visit a model.
   *
   * @param ctx   the visitor context
   * @param model the model
   * @see IModel
   */
  default void visitModel(IVisitorContext ctx, IModel model) {
  }

  /**
   * Invoked to visit a package.
   *
   * @param ctx                     the visitor context
   * @param systemDescriptorPackage the package
   * @see IPackage
   */
  default void visitPackage(IVisitorContext ctx, IPackage systemDescriptorPackage) {
  }

  /**
   * Invoked to visit a system descriptor.
   *
   * @param ctx              the visitor context
   * @param systemDescriptor the descriptor
   * @see ISystemDescriptor
   */
  default void visitSystemDescriptor(IVisitorContext ctx, ISystemDescriptor systemDescriptor) {
  }
}
