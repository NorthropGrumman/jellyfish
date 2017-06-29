package com.ngc.seaside.systemdescriptor.model.impl.basic.data;

import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IPrimitiveDataField;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.impl.basic.metadata.Metadata;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.json.Json;

import static com.ngc.seaside.systemdescriptor.model.impl.basic.TestUtils.demandImmutability;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class PrimitiveDataFieldTest {

	private PrimitiveDataField primitiveDataField;
	private static final String FIELD_NAME = "field1";

	@Mock
	private IData parent;

	private IMetadata metadata;

	@Before
	public void setup() throws Throwable {
		metadata = new Metadata();
		metadata.setJson(Json.createObjectBuilder().add("foo", "bar").build());
	}

	@Test
	public void testDoesMakePimitiveDataField() throws Throwable {
		primitiveDataField = new PrimitiveDataField(FIELD_NAME);
		assertEquals("name not correct!", FIELD_NAME, primitiveDataField.getName());
	}

	@Test
	public void testDoesMakeImmutablePrimitiveDataField() throws Throwable {
		primitiveDataField = new PrimitiveDataField(FIELD_NAME);
		primitiveDataField.setMetadata(new Metadata());

		IPrimitiveDataField immutable = PrimitiveDataField.immutable(primitiveDataField);
		demandImmutability(() -> immutable.setType(null));
		demandImmutability(() -> immutable.setMetadata(null));
	}

}
