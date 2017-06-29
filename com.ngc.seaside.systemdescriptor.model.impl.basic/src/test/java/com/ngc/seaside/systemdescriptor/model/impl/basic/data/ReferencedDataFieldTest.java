package com.ngc.seaside.systemdescriptor.model.impl.basic.data;

import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IReferencedDataField;
import com.ngc.seaside.systemdescriptor.model.impl.basic.metadata.Metadata;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static com.ngc.seaside.systemdescriptor.model.impl.basic.TestUtils.demandImmutability;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class ReferencedDataFieldTest {

	private static final String FIELD_NAME = "dataRef1";
	private ReferencedDataField referencedDataField;
	private Data referencedData;

	@Mock
	private IData parent;

	@Before
	public void setup() throws Throwable {
		referencedData = new Data("referencedData1");
		referencedData.setMetadata(new Metadata());

		referencedDataField = new ReferencedDataField(FIELD_NAME);
		referencedDataField.setData(referencedData);
		referencedDataField.setMetadata(new Metadata());
	}

	@Test
	public void testDoesMakeDataRef() throws Throwable {
		assertEquals("name not correct!", FIELD_NAME, referencedDataField.getName());
	}

	@Test
	public void testDoesMakeImmutableDataRef() throws Throwable {
		IReferencedDataField immutable = ReferencedDataField.immutable(referencedDataField);
		demandImmutability(() -> immutable.setMetadata(null));
		demandImmutability(() -> immutable.setData(null));
	}

}
