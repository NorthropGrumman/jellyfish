package com.ngc.seaside.systemdescriptor.model.impl.xtext.metadata;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.exception.UnrecognizedXtextTypeException;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Array;
import com.ngc.seaside.systemdescriptor.systemDescriptor.ArrayValue;
import com.ngc.seaside.systemdescriptor.systemDescriptor.BooleanValue;
import com.ngc.seaside.systemdescriptor.systemDescriptor.DblValue;
import com.ngc.seaside.systemdescriptor.systemDescriptor.IntValue;
import com.ngc.seaside.systemdescriptor.systemDescriptor.JsonObject;
import com.ngc.seaside.systemdescriptor.systemDescriptor.JsonValue;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Member;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Metadata;
import com.ngc.seaside.systemdescriptor.systemDescriptor.NullValue;
import com.ngc.seaside.systemdescriptor.systemDescriptor.StringValue;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorFactory;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Value;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.json.spi.JsonProvider;


/**
 * Adapts an {@link Metadata} instance to {@link IMetadata}.  Unlike other wrappers, changes to this object do not
 * "write thought" to the wrapped object.
 */
public class WrappedMetadata implements IMetadata {

   /**
    * The cached JSON provider.  We don't use synchronization here.  If it gets loaded twice, that is not a problem.
    */
   private static JsonProvider provider;

   private javax.json.JsonObject json;

   private WrappedMetadata(javax.json.JsonObject json) {
      this.json = json;
   }

   public WrappedMetadata() {
   }

   @Override
   public javax.json.JsonObject getJson() {
      return json;
   }

   @Override
   public IMetadata setJson(javax.json.JsonObject json) {
      this.json = json;
      return this;
   }

   /**
    * Creates a new {@code IMetadata} instance that is equivalent to the given {@code Metadata}.
    */
   public static IMetadata fromXtext(Metadata metadata) {
      return metadata == null ? IMetadata.EMPTY_METADATA : fromXtextJson(metadata.getJson());
   }

   /**
    * Creates a new {@code IMetadata} instance that is equivalent to the given {@code JsonObject}.  If the JSON is
    * {@code null}, {@link IMetadata#EMPTY_METADATA} is returned.
    */
   public static IMetadata fromXtextJson(JsonObject json) {
      return json == null ? IMetadata.EMPTY_METADATA : new WrappedMetadata(toJavaxJsonObject(json));
   }

   /**
    * Creates a new {@code Metadata} instance is is equivalent ot the given {@code IMetadata}.  If the metadata is empty
    * the new instanceewill have a null JSON object.
    */
   public static Metadata toXtext(IMetadata metadata) {
      Preconditions.checkNotNull(metadata, "metadata may not be null!");
      Metadata xtext = SystemDescriptorFactory.eINSTANCE.createMetadata();
      if (!metadata.getJson().isEmpty()) {
         xtext.setJson(toXtextJsonObject(metadata.getJson()));
      }
      return xtext;
   }

   /**
    * Creates a new {@code JsonObject} instance is is equivalent ot the given {@code IMetadata}.  If the metadata is
    * empty, {@code null} is returned.
    */
   public static JsonObject toXtextJson(IMetadata metadata) {
      Preconditions.checkNotNull(metadata, "metadata may not be null!");
      return metadata.getJson().isEmpty() ? null : toXtextJsonObject(metadata.getJson());
   }

   // These factory methods below are package protected so we can use them in unit tests.  They are not intended to be
   // public.

   static Member newMember(String key, Value value) {
      Member m = SystemDescriptorFactory.eINSTANCE.createMember();
      m.setKey(key);
      m.setValue(value);
      return m;
   }

   static StringValue newStringValue(String x) {
      StringValue value = SystemDescriptorFactory.eINSTANCE.createStringValue();
      value.setValue(x);
      return value;
   }

   static IntValue newIntValue(int x) {
      IntValue value = SystemDescriptorFactory.eINSTANCE.createIntValue();
      value.setValue(x);
      return value;
   }

   static DblValue newDblValue(double x) {
      DblValue value = SystemDescriptorFactory.eINSTANCE.createDblValue();
      value.setValue(x);
      return value;
   }

   static NullValue newNullValue() {
      return SystemDescriptorFactory.eINSTANCE.createNullValue();
   }

   static BooleanValue newBooleanValue(boolean x) {
      BooleanValue value = SystemDescriptorFactory.eINSTANCE.createBooleanValue();
      value.setValue(Boolean.toString(x));
      return value;
   }

   static ArrayValue newArrayValue(Collection<Value> values) {
      ArrayValue value = SystemDescriptorFactory.eINSTANCE.createArrayValue();
      Array array = SystemDescriptorFactory.eINSTANCE.createArray();
      for (Value v : values) {
         array.getValues().add(v);
      }
      value.setValue(array);
      return value;
   }

   static JsonValue newJsonValue(JsonObject x) {
      JsonValue value = SystemDescriptorFactory.eINSTANCE.createJsonValue();
      value.setValue(x);
      return value;
   }

   private static javax.json.JsonObject toJavaxJsonObject(JsonObject object) {
      JsonObjectBuilder builder = getProvider().createObjectBuilder();
      for (Member member : object.getMembers()) {
         builder.add(member.getKey(), convertValueToWrapped(member.getValue()));
      }
      return builder.build();
   }

   public static JsonObject toXtextJsonObject(javax.json.JsonObject object) {
      JsonObject xtext = SystemDescriptorFactory.eINSTANCE.createJsonObject();
      for (Map.Entry<String, javax.json.JsonValue> entry : object.entrySet()) {
         xtext.getMembers().add(newMember(entry.getKey(),
                                          convertWrappedToValue(entry.getValue())));
      }
      return xtext;
   }

   private static javax.json.JsonArray toJavaxJsonArray(Array array) {
      JsonArrayBuilder builder = getProvider().createArrayBuilder();
      for (Value value : array.getValues()) {
         builder.add(convertValueToWrapped(value));
      }
      return builder.build();
   }

   private static javax.json.JsonValue convertValueToWrapped(Value value) {
      switch (value.eClass().getClassifierID()) {
         case SystemDescriptorPackage.STRING_VALUE:
            return getProvider().createValue(((StringValue) value).getValue());
         case SystemDescriptorPackage.INT_VALUE:
            return getProvider().createValue(((IntValue) value).getValue());
         case SystemDescriptorPackage.DBL_VALUE:
            return getProvider().createValue(((DblValue) value).getValue());
         case SystemDescriptorPackage.BOOLEAN_VALUE:
            boolean bool = Boolean.valueOf(((BooleanValue) value).getValue());
            return bool ? javax.json.JsonValue.TRUE : javax.json.JsonValue.FALSE;
         case SystemDescriptorPackage.NULL_VALUE:
            return javax.json.JsonValue.NULL;
         case SystemDescriptorPackage.JSON_VALUE:
            return toJavaxJsonObject(((JsonValue) value).getValue());
         case SystemDescriptorPackage.ARRAY_VALUE:
            return toJavaxJsonArray(((ArrayValue) value).getValue());
         default:
            throw new UnrecognizedXtextTypeException(value);
      }
   }

   private static Value convertWrappedToValue(javax.json.JsonValue value) {
      switch (value.getValueType()) {
         case STRING:
            return newStringValue(((javax.json.JsonString) value).getString());
         case NUMBER:
            javax.json.JsonNumber number = (javax.json.JsonNumber) value;
            return number.isIntegral() ? newIntValue(number.intValue()) : newDblValue(number.doubleValue());
         case TRUE:
            return newBooleanValue(true);
         case FALSE:
            return newBooleanValue(false);
         case NULL:
            return newNullValue();
         case ARRAY:
            JsonArray array = (JsonArray) value;
            return newArrayValue(array.stream()
                                       .map(WrappedMetadata::convertWrappedToValue)
                                       .collect(Collectors.toList()));
         case OBJECT:
            javax.json.JsonObject object = (javax.json.JsonObject) value;
            JsonValue jsonValue = SystemDescriptorFactory.eINSTANCE.createJsonValue();
            jsonValue.setValue(toXtextJsonObject(object));
            return jsonValue;
         default:
            throw new IllegalStateException(
                  String.format("cannot convert JSON value %s to XText!", value.getValueType()));
      }
   }

   private static JsonProvider getProvider() {
      // Don't worry about synchronization, if the provider gets loaded twice that is fine.
      if (provider == null) {
         // Important note about OSGi and Glassfish.  If this bundle is running in OSGi *do not* deploy both the
         // Glassfish implementation bundle and the JSON API bundle.  Only deploy the Glassfish implementation bundle
         // or errors will result.  This is because the Glassfish implementation bundle contains the API classes
         // directly.
         provider = JsonProvider.provider();
      }
      return provider;
   }
}
