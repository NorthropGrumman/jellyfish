package com.ngc.seaside.systemdescriptor.model.impl.view;

import com.ngc.seaside.systemdescriptor.model.api.FieldCardinality;
import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.link.IModelLink;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperties;
import com.ngc.seaside.systemdescriptor.model.impl.basic.metadata.Metadata;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.properties.Properties;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.properties.Property;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.properties.PropertyPrimitiveValue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigInteger;
import java.util.Collections;
import java.util.Optional;

import javax.json.spi.JsonProvider;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AggregatedLinkViewTest {

   private AggregatedLinkView<IModelReferenceField> view;

   @Mock
   private IModelLink<IModelReferenceField> link;

   @Mock
   private IModelLink<IModelReferenceField> parent;

   @Mock
   private IModelLink<IModelReferenceField> grandparent;

   @Mock
   private IModelReferenceField source;

   @Mock
   private IModelReferenceField target;

   @Before
   public void setup() {
      Metadata metadata = new Metadata();
      metadata.setJson(JsonProvider.provider().createObjectBuilder()
                             .add("hello", "world")
                             .build());

      Properties properties = new Properties();
      properties.add(new Property("blah",
                                  DataTypes.INT,
                                  FieldCardinality.SINGLE,
                                  Collections.singleton(new PropertyPrimitiveValue(BigInteger.ONE)),
                                  null));

      when(link.getRefinedLink()).thenReturn(Optional.of(parent));
      when(link.getMetadata()).thenReturn(IMetadata.EMPTY_METADATA);
      when(link.getName()).thenReturn(Optional.empty());
      when(link.getProperties()).thenReturn(IProperties.EMPTY_PROPERTIES);

      when(parent.getRefinedLink()).thenReturn(Optional.of(grandparent));
      when(parent.getName()).thenReturn(Optional.of("foo"));
      when(parent.getMetadata()).thenReturn(metadata);
      when(parent.getSource()).thenReturn(source);
      when(parent.getTarget()).thenReturn(target);
      when(parent.getProperties()).thenReturn(IProperties.EMPTY_PROPERTIES);

      when(grandparent.getRefinedLink()).thenReturn(Optional.empty());
      when(grandparent.getMetadata()).thenReturn(IMetadata.EMPTY_METADATA);
      when(grandparent.getProperties()).thenReturn(properties);

      view = new AggregatedLinkView<>(link);
   }

   @Test
   public void testDoesGetNameFromRefinedLinks() {
      assertEquals("name not correct!",
                   "foo",
                   view.getName().get());
   }

   @Test
   public void testDoesGetSourceAndTargetFromRefinedLinks() {
      assertEquals("target not correct!",
                   target,
                   view.getTarget());
      assertEquals("source not correct!",
                   source,
                   view.getSource());
   }

   @Test
   public void testDoesGetMetadataFromRefinedLinks() {
      assertEquals("did not overwrite metadata from parent object!",
                   "world",
                   view.getMetadata().getJson().getString("hello"));
   }

   @Test
   public void testDoesGetPropertiesFromRefinedLinks() {
      assertEquals("did not get properties from refined link!",
                   BigInteger.ONE,
                   view.getProperties().resolveAsInteger("blah").orElse(null));

   }
}
