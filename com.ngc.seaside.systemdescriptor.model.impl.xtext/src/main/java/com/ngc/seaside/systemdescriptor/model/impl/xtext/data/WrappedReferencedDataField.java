package com.ngc.seaside.systemdescriptor.model.impl.xtext.data;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.data.IPrimitiveDataField;
import com.ngc.seaside.systemdescriptor.model.api.data.IReferencedDataField;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.AbstractWrappedXtext;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.metadata.WrappedMetadata;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Data;
import com.ngc.seaside.systemdescriptor.systemDescriptor.ReferencedDataFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorFactory;

/**
 * Adapts a {@link DataRefDeclaration} instance to an {@link IDataRef}.
 *
 * This class is not threadsafe.
 */
public class WrappedReferencedDataField extends AbstractWrappedXtext<ReferencedDataFieldDeclaration>
		implements IReferencedDataField {

	private IMetadata metadata;

	public WrappedReferencedDataField(IWrapperResolver resolver, ReferencedDataFieldDeclaration wrapped) {
		super(resolver, wrapped);
	}

	@Override
	public String getName() {
		return wrapped.getName();
	}

	@Override
	public IData getParent() {
		return resolver.getWrapperFor((Data) wrapped.eContainer());
	}

	@Override
	public IData getData() {
		return resolver.getWrapperFor(wrapped.getData());
	}

	@Override
	public IMetadata getMetadata() {
		return metadata;
	}

	@Override
	public IReferencedDataField setMetadata(IMetadata metadata) {
		Preconditions.checkNotNull(metadata, "metadata may not be null!");
		this.metadata = metadata;
		wrapped.setMetadata(WrappedMetadata.toXtextJson(metadata));
		return this;
	}

	@Override
	public IReferencedDataField setData(IData data) {
		Preconditions.checkNotNull(data, "data may not be null!");
		wrapped.setData(resolver.findXTextData(data.getName(), data.getParent().getName()).get());
		return this;
	}

	/**
	 * Creates a new {@code ReferencedDataFieldDeclaration} that is equivalent
	 * to the given data ref. Changes to the {@code IReferencedDataField} are
	 * not reflected in the returned {@code ReferencedDataFieldDeclaration}
	 * after construction.
	 */
	public static ReferencedDataFieldDeclaration toXtext(IReferencedDataField dataRef, IWrapperResolver resolver) {
		Preconditions.checkNotNull(dataRef, "dataRef may not be null!");
		ReferencedDataFieldDeclaration x = SystemDescriptorFactory.eINSTANCE.createReferencedDataFieldDeclaration();
		x.setName(dataRef.getName());
		x.setData(resolver.findXTextData(dataRef.getData().getName(), dataRef.getData().getParent().getName()).get());
		return x;
	}
}
