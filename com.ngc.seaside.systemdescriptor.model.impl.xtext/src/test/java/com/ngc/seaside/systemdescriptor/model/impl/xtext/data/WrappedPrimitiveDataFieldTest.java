package com.ngc.seaside.systemdescriptor.model.impl.xtext.data;

import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.data.IPrimitiveDataField;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.AbstractWrappedXtextTest;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Data;
import com.ngc.seaside.systemdescriptor.systemDescriptor.DataFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PrimitiveDataFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PrimitiveDataType;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WrappedPrimitiveDataFieldTest extends AbstractWrappedXtextTest {

	private WrappedData wrappedData;

	private Data data;

	@Mock
	private IPackage parent;

	@Before
	public void setup() throws Throwable {
		data = factory().createData();
		data.setName("Foo");

		PrimitiveDataFieldDeclaration field = factory().createPrimitiveDataFieldDeclaration();
		field.setName("field1");
		field.setType(PrimitiveDataType.STRING);
		data.getFields().add(field);

		Package p = factory().createPackage();
		p.setName("my.package");
		p.setElement(data);
		when(resolver().getWrapperFor(p)).thenReturn(parent);
	}

	@Test
	public void testDoesWrapXtextObject() throws Throwable {
		wrappedData = new WrappedData(resolver(), data);
		assertEquals("name not correct!", wrappedData.getName(), data.getName());
		assertEquals("fully qualified name not correct!", "my.package.Foo", wrappedData.getFullyQualifiedName());
		assertEquals("parent not correct!", parent, wrappedData.getParent());
		assertEquals("metadata not set!", IMetadata.EMPTY_METADATA, wrappedData.getMetadata());

		String fieldName = data.getFields().get(0).getName();
		assertEquals("did not get fields!", fieldName, wrappedData.getFields().getByName(fieldName).get().getName());
	}

	@Test
	public void testDoesUpdateXtextObject() throws Throwable {
		IPrimitiveDataField newField = mock(IPrimitiveDataField.class);
		when(newField.getName()).thenReturn("newField");
		when(newField.getMetadata()).thenReturn(IMetadata.EMPTY_METADATA);

		wrappedData = new WrappedData(resolver(), data);
		wrappedData.getFields().add(newField);
		assertEquals("newField name not correct!", newField.getName(), data.getFields().get(1).getName());

		wrappedData.setMetadata(newMetadata("foo", "bar"));
		assertNotNull("metadata not set!", data.getMetadata());
	}
}
