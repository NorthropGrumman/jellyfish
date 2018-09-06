/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
 */
package com.ngc.seaside.systemdescriptor.model.api.traversal;

import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.data.IEnumeration;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.link.IModelLink;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Performs traversals on system descriptors.
 */
public class Traversals {

   /**
    * Singleton instance of an {@code IVisitor} that will print the structure of a system descriptor to system out.
    * Useful for debugging.
    */
   public static final IVisitor SYSTEM_OUT_PRINTING_VISITOR = new PrintingVisitor(System.out);

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

   /**
    * Collects all models that are referenced from the given {@code SystemDescriptor} that match the provided {@code
    * Predicate}.  Common predicates are available at {@link ModelPredicates}.
    *
    * @param descriptor the descriptor
    * @param predicate  the predicate used to accept models
    * @return a collection of all models contained in the descriptor that have been accepted by the predicate
    */
   public static Collection<IModel> collectModels(ISystemDescriptor descriptor, Predicate<IModel> predicate) {
      if (descriptor == null) {
         throw new NullPointerException("descriptor may not be null!");
      }
      if (predicate == null) {
         throw new NullPointerException("predicate may not be null!");
      }

      Collection<IModel> acceptedModels = new ArrayList<>();
      for (IPackage p : descriptor.getPackages()) {
         for (IModel model : p.getModels()) {
            if (predicate.test(model)) {
               acceptedModels.add(model);
            }
         }
      }
      return acceptedModels;
   }

   /**
    * Returns a new {@code IVisitor} that will print the structure of a system descriptor to the given stream.  This can
    * be useful for debugging.  Use {@link #SYSTEM_OUT_PRINTING_VISITOR} to print to system out.
    */
   public static IVisitor pritingVisitor(PrintStream stream) {
      if (stream == null) {
         throw new NullPointerException("stream may not be null!");
      }
      return new PrintingVisitor(stream);
   }

   private static void doVisitPackage(IVisitor visitor, VisitorContext ctx, IPackage p) {
      visitor.visitPackage(ctx, p);
      for (Iterator<IData> data = p.getData().iterator(); data.hasNext() && !ctx.stopped; ) {
         doVisitData(visitor, ctx, data.next());
      }
      for (Iterator<IModel> models = p.getModels().iterator(); models.hasNext() && !ctx.stopped; ) {
         doVisitModel(visitor, ctx, models.next());
      }
      for (Iterator<IEnumeration> enums = p.getEnumerations().iterator(); enums.hasNext() && !ctx.stopped; ) {
         doVisitEnumeration(visitor, ctx, enums.next());
      }
   }

   private static void doVisitEnumeration(IVisitor visitor, VisitorContext ctx, IEnumeration enumeration) {
      visitor.visitEnumeration(ctx, enumeration);
   }

   private static void doVisitData(IVisitor visitor, VisitorContext ctx, IData data) {
      visitor.visitData(ctx, data);
      for (Iterator<IDataField> fields = data.getFields().iterator(); fields.hasNext() && !ctx.stopped; ) {
         visitor.visitDataField(ctx, fields.next());
      }
   }


   private static void doVisitModel(IVisitor visitor, VisitorContext ctx, IModel model) {
      visitor.visitModel(ctx, model);
      for (Iterator<IDataReferenceField> fields = model.getInputs().iterator();
            fields.hasNext() && !ctx.stopped; ) {
         visitor.visitDataReferenceFieldAsInput(ctx, fields.next());
      }
      for (Iterator<IDataReferenceField> fields = model.getOutputs().iterator();
            fields.hasNext() && !ctx.stopped; ) {
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
      for (Iterator<IModelLink<?>> links = model.getLinks().iterator();
            links.hasNext() && !ctx.stopped; ) {
         visitor.visitLink(ctx, links.next());
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

   /**
    * A visitor that can be used for debugging that prints the structure of a system descriptor.
    */
   private static class PrintingVisitor implements IVisitor {

      private final PrintStream stream;

      private PrintingVisitor(PrintStream stream) {
         this.stream = stream;
      }

      @Override
      public void visitDataField(IVisitorContext ctx, IDataField dataField) {
         stream.format("        - %s [%s] %s%n",
                       dataField.getName(),
                       dataField.getType(),
                       dataField.getMetadata().getJson().toString());
      }

      @Override
      public void visitData(IVisitorContext ctx, IData data) {
         stream.format("    + %s [Data - %d fields] %s%n",
                       data.getName(),
                       data.getFields().size(),
                       data.getMetadata().getJson().toString());
      }

      @Override
      public void visitScenario(IVisitorContext ctx, IScenario scenario) {
         stream.format("        - %s [(scenario) %d total steps]%n",
                       scenario.getName(),
                       scenario.getGivens().size() + scenario.getWhens().size() + scenario.getThens().size());
      }

      @Override
      public void visitLink(IVisitorContext ctx, IModelLink<?> link) {
         stream.format("        - link from %s.%s to %s.%s%n",
                       link.getSource().getParent().getFullyQualifiedName(),
                       link.getSource().getName(),
                       link.getTarget().getParent().getFullyQualifiedName(),
                       link.getTarget().getName());
      }

      @Override
      public void visitDataReferenceFieldAsInput(IVisitorContext ctx, IDataReferenceField field) {
         stream.format("        - %s [(input) %s]%n",
                       field.getName(),
                       field.getType().getFullyQualifiedName());
      }

      @Override
      public void visitDataReferenceFieldAsOutput(IVisitorContext ctx, IDataReferenceField field) {
         stream.format("        - %s [(output) %s]%n",
                       field.getName(),
                       field.getType().getFullyQualifiedName());
      }

      @Override
      public void visitModelReferenceFieldAsRequirement(IVisitorContext ctx, IModelReferenceField field) {
         stream.format("        - %s [(required) %s]%n",
                       field.getName(),
                       field.getType().getFullyQualifiedName());
      }

      @Override
      public void visitModelReferenceFieldAsPart(IVisitorContext ctx, IModelReferenceField field) {
         stream.format("        - %s [(part) %s]%n",
                       field.getName(),
                       field.getType().getFullyQualifiedName());
      }

      @Override
      public void visitModel(IVisitorContext ctx, IModel model) {
         stream.format("    + %s [Model - %d fields] %s%n",
                       model.getName(),
                       model.getInputs().size()
                       + model.getOutputs().size()
                       + model.getRequiredModels().size()
                       + model.getParts().size(),
                       model.getMetadata().getJson().toString());
      }

      @Override
      public void visitPackage(IVisitorContext ctx, IPackage systemDescriptorPackage) {
         stream.format("  + %s [Package - %d models, %d data]%n",
                       systemDescriptorPackage.getName(),
                       systemDescriptorPackage.getModels().size(),
                       systemDescriptorPackage.getData().size());
      }

      @Override
      public void visitSystemDescriptor(IVisitorContext ctx, ISystemDescriptor systemDescriptor) {
         stream.format("[System Descriptor - %d packages]%n",
                       systemDescriptor.getPackages().size());
      }
   }
}
