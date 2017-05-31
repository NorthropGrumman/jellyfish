package com.ngc.seaside.systemdescriptor.model.api.traversal;

import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;

import java.util.Iterator;
import java.util.Optional;

/**
 * Performs traversals on system descriptors.
 */
public class Traversals {

  private Traversals() {
  }

  /**
   * Performs a traversal of the given system descriptor.
   *
   * @param descriptor the descriptor to traverse
   * @param visitor    the visitor
   * @return the result of the traversal
   * @see ISystemDescriptor#traverse(IVisitor)
   */
  public static Optional<Object> traverse(ISystemDescriptor descriptor, IVisitor visitor) {
    if (descriptor == null) {
      throw new NullPointerException("descriptor may not be null!");
    }
    if (visitor == null) {
      throw new NullPointerException("visitor may not be null!");
    }

    VisitorContext ctx = new VisitorContext(descriptor);
    visitor.visitSystemDescriptor(ctx, descriptor);
    for (Iterator<IPackage> packages = descriptor.getPackages().iterator(); packages.hasNext() && !ctx.stopped; ) {
      doVisitPackage(visitor, ctx, packages.next());
    }

    return Optional.ofNullable(ctx.getResult());
  }

  private static void doVisitPackage(IVisitor visitor, VisitorContext ctx, IPackage p) {
    visitor.visitPackage(ctx, p);
    for (Iterator<IData> data = p.getData().iterator(); data.hasNext() && !ctx.stopped; ) {
      doVisitData(visitor, ctx, data.next());
    }
    for (Iterator<IModel> models = p.getModels().iterator(); models.hasNext() && !ctx.stopped; ) {
      doVisitModel(visitor, ctx, models.next());
    }
  }

  private static void doVisitData(IVisitor visitor, VisitorContext ctx, IData data) {
    visitor.visitData(ctx, data);
    for (Iterator<IDataField> fields = data.getFields().iterator(); fields.hasNext() && !ctx.stopped; ) {
      visitor.visitDataField(ctx, fields.next());
    }
  }


  private static void doVisitModel(IVisitor visitor, VisitorContext ctx, IModel model) {
    visitor.visitModel(ctx, model);
    for (Iterator<IDataReferenceField> fields = model.getInputs().iterator(); fields.hasNext() && !ctx.stopped; ) {
      visitor.visitDataReferenceFieldAsInput(ctx, fields.next());
    }
    for (Iterator<IDataReferenceField> fields = model.getOutputs().iterator(); fields.hasNext() && !ctx.stopped; ) {
      visitor.visitDataReferenceFieldAsOutput(ctx, fields.next());
    }
    for (Iterator<IModelReferenceField> fields = model.getRequiredModels().iterator();
         fields.hasNext() && !ctx.stopped; ) {
      visitor.visitModelReferenceFieldAsRequirement(ctx, fields.next());
    }
    for (Iterator<IModelReferenceField> fields = model.getParts().iterator();
         fields.hasNext() && !ctx.stopped; ) {
      visitor.visitModelReferenceFieldAsPart(ctx, fields.next());
    }
    for (Iterator<IScenario> scenarios = model.getScenarios().iterator();
         scenarios.hasNext() && !ctx.stopped; ) {
      visitor.visitScenario(ctx, scenarios.next());
    }
  }

  /**
   * A simple implementation of {@code IVisitorContext}.
   */
  private static class VisitorContext implements IVisitorContext {

    private final ISystemDescriptor descriptor;
    private Object result;
    private boolean stopped;

    private VisitorContext(ISystemDescriptor descriptor) {
      this.descriptor = descriptor;
    }

    @Override
    public ISystemDescriptor getSystemDescriptor() {
      return descriptor;
    }

    @Override
    public Object getResult() {
      return result;
    }

    @Override
    public void setResult(Object result) {
      this.result = result;
    }

    @Override
    public void stop() {
      stopped = true;
    }
  }
}
