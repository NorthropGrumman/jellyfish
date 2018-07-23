package com.ngc.seaside.systemdescriptor.model.api.model.link;

import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperties;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Represents a link between two fields of a model.  Links usually join together two {@link IModel}s or {@link
 * com.ngc.seaside.systemdescriptor.model.api.data.IData}s types.  Operations that change the state of this object may
 * throw {@code UnsupportedOperationException}s if the object is immutable.
 *
 * @param <T> the type of reference field that link is connecting
 */
public interface IModelLink<T extends IReferenceField> {

   /**
    * Gets the metadata for this link.
    *
    * @return the metadata for this link
    */
   IMetadata getMetadata();

   /**
    * Sets the metadata for this link.
    *
    * @param metadata the metadata for this link
    * @return this link
    */
   IModelLink<T> setMetadata(IMetadata metadata);

   /**
    * Gets the properties of this link.
    *
    * @return the properties of this link (never {@code null})
    */
   IProperties getProperties();

   /**
    * Sets the properties of this link.
    *
    * @param properties the properties of this link
    * @return this link
    */
   IModelLink<T> setProperties(IProperties properties);

   /**
    * Gets the source of the link.
    *
    * @return the source of the link
    */
   T getSource();

   /**
    * Sets the source of the link
    *
    * @param source the source of the link
    * @return this link
    */
   IModelLink<T> setSource(T source);

   /**
    * Traverses the fields that are referenced in the expression of the source of this link.  The consumer is invoked
    * for every part or requirement field that occurs in the expression in the order the fields are referenced in the
    * expression.  If the source of the link references a field directly and is not an expression, the consumer is
    * never invoked.  For example, consider this link:
    * <pre>{@code link timer.currentTIme -> display.currentTime
    * }</pre>
    * The consumer would be invoked for the field {@code timer} since {@code timer} is a field referenced in the source
    * of the link.
    *
    * </p>
    *
    * This operation is useful to determine which part(s) a link may reference in a model.  For convenience, {@link
    * com.ngc.seaside.systemdescriptor.model.api.SystemDescriptors#getReferencedFieldOfLinkSource(IModelLink)} is
    * provided to get the field referenced in a link's source directly. This operation is not supported if this
    * link is refining another link.  In this case, traverse the source of the link being refined.
    *
    * @param linkVisitor the callback to invoke to consume the fields that make up the source expression of the link
    */
   void traverseLinkSourceExpression(Consumer<IModelReferenceField> linkVisitor);

   /**
    * Gets the target of the link.
    */
   T getTarget();

   /**
    * Sets the target of the link
    *
    * @param target the target of the link
    * @return this link
    */
   IModelLink<T> setTarget(T target);

   /**
    * Traverses the fields that are referenced in the expression of the target of this link.  The consumer is invoked
    * for every part or requirement field that occurs in the expression in the order the fields are referenced in the
    * expression.  If the target of the link references a field directly and is not an expression, the consumer is
    * never invoked.  For example, consider this link:
    * <pre>{@code link timer.currentTIme -> display.currentTime
    * }</pre>
    * The consumer would be invoked for the field {@code display} since {@code display} is a field referenced in the
    * target of the link.
    *
    * </p>
    *
    * This operation is useful to determine which part(s) a link may reference in a model.  For convenience, {@link
    * com.ngc.seaside.systemdescriptor.model.api.SystemDescriptors#getReferencedFieldOfLinkTarget(IModelLink)} is
    * provided to get the field referenced in a link's source directly. This operation is not supported if this
    * link is refining another link.  In this case, traverse the source of the link being refined.
    *
    * @param linkVisitor the callback to invoke to consume the fields that make up the target expression of the link
    */
   void traverseLinkTargetExpression(Consumer<IModelReferenceField> linkVisitor);

   /**
    * Gets the link's name, if it has one.
    *
    * @return the name of the link, if one exists.
    */
   Optional<String> getName();

   /**
    * Sets the link name.
    *
    * @param name the name for the link
    * @return this link
    */
   IModelLink<T> setName(String name);

   /**
    * Gets the link that this link refines. If this link does not refine a link, the optional is empty.
    *
    * @return the link this link is refining
    */
   Optional<IModelLink<T>> getRefinedLink();

   /**
    * Gets the link that this link is refining.
    *
    * @param refinedLink the link that this link is refining
    * @return this link
    */
   IModelLink<T> setRefinedLink(IModelLink<T> refinedLink);

   /**
    * Gets the parent model that contains this link.
    *
    * @return the parent model that contains this link
    */
   IModel getParent();
}
