package com.ngc.seaside.systemdescriptor.model.impl.basic.data;

import java.util.Objects;

import com.google.common.base.Preconditions;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.data.IReferencedDataField;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.impl.basic.metadata.Metadata;

public class ReferencedDataField implements IReferencedDataField {

	protected final String name;
	protected IData parent;
	protected IData data;
	protected IMetadata metadata;

	public ReferencedDataField(final String name) {
		Preconditions.checkNotNull(name, "name may not be null!");
		Preconditions.checkArgument(!name.trim().isEmpty(), "name may not be empty!");
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public IData getParent() {
		return parent;
	}

	@Override
	public IData getData() {
		return data;
	}

	@Override
	public IMetadata getMetadata() {
		return metadata;
	}

	@Override
	public ReferencedDataField setMetadata(IMetadata metadata) {
		this.metadata = metadata;
		return this;
	}

	@Override
	public ReferencedDataField setData(IData data) {
		this.data = data;
		return this;
	}

	public ReferencedDataField setParent(IData parent) {
		this.parent = parent;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof DataField)) {
			return false;
		}
		ReferencedDataField dataRef = (ReferencedDataField) o;
		return Objects.equals(name, dataRef.name) && parent == dataRef.parent && Objects.equals(data, dataRef.data);
	}

	@Override
	public int hashCode() {
		// TODO Should I use identifyHashCode for "data" as well? Why use this?
		return Objects.hash(name, System.identityHashCode(parent), data);
	}

	@Override
	public String toString() {
		return "ReferencedDataField[" + "name='" + name + '\'' + ", parent=" + (parent == null ? "null" : parent.getName())
				+ ", data=" + data.getFullyQualifiedName() + ']';
	}

	public static IReferencedDataField immutable(IReferencedDataField dataRef) {
		Preconditions.checkNotNull(dataRef, "dataRef may not be null!");
		ImmutableReferencedDataField immutable = new ImmutableReferencedDataField(dataRef.getName());
		immutable.parent = dataRef.getParent();
		immutable.metadata = Metadata.immutable(dataRef.getMetadata());
		// TODO Should data be immutable as well?
		immutable.data = dataRef.getData();
		return immutable;
	}

	static class ImmutableReferencedDataField extends ReferencedDataField {

		private ImmutableReferencedDataField(String name) {
			super(name);
		}

		@Override
		public ReferencedDataField setParent(IData parent) {
			throw new UnsupportedOperationException("object is not modifiable!");
		}

		@Override
		public ReferencedDataField setMetadata(IMetadata metadata) {
			throw new UnsupportedOperationException("object is not modifiable!");
		}

		@Override
		public ReferencedDataField setData(IData data) {
			throw new UnsupportedOperationException("object is not modifiable!");
		}
	}
}
