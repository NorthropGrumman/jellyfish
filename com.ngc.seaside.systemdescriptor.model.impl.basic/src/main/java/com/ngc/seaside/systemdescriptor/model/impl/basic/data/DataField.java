package com.ngc.seaside.systemdescriptor.model.impl.basic.data;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.impl.basic.metadata.Metadata;

import java.util.Objects;

/**
 * Implements an IDataField.
 *
 * @author psnell
 */
public class DataField implements IDataField {

	protected final String name;
	protected IData parent;
	protected IMetadata metadata;

	public DataField(String name) {
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
	public IMetadata getMetadata() {
		return metadata;
	}

	@Override
	public DataField setMetadata(IMetadata metadata) {
		this.metadata = metadata;
		return this;
	}

	public DataField setParent(IData parent) {
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
		DataField dataField = (DataField) o;
		return Objects.equals(name, dataField.name) && parent == dataField.parent
				&& Objects.equals(metadata, dataField.metadata);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, System.identityHashCode(parent), metadata);
	}

	@Override
	public String toString() {
		return "DataField[" + "name='" + name + '\'' + ", parent=" + (parent == null ? "null" : parent.getName())
				+ ", metadata=" + metadata + ']';
	}

	public static IDataField immutable(IDataField dataField) {
		Preconditions.checkNotNull(dataField, "dataField may not be null!");
		ImmutableDataField immutable = new ImmutableDataField(dataField.getName());
		immutable.parent = dataField.getParent();
		immutable.metadata = Metadata.immutable(dataField.getMetadata());
		return immutable;
	}

	static class ImmutableDataField extends DataField {

		private ImmutableDataField(String name) {
			super(name);
		}

		@Override
		public DataField setMetadata(IMetadata metadata) {
			throw new UnsupportedOperationException("object is not modifiable!");
		}

		@Override
		public DataField setParent(IData parent) {
			throw new UnsupportedOperationException("object is not modifiable!");
		}
	}
}
