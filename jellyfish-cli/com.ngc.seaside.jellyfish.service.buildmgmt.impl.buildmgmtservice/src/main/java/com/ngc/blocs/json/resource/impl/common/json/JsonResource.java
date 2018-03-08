package com.ngc.blocs.json.resource.impl.common.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Objects;

public class JsonResource<T> implements IJsonResource<T> {

   private final static Gson GSON = new GsonBuilder()
         .setPrettyPrinting()
         .create();

   private final String file;
   private final Class<T> clazz;
   private T object;
   private Throwable error;

   public JsonResource(String file, Class<T> clazz) {
      if (file == null) {
         throw new NullPointerException("file may not be null!");
      }
      if (clazz == null) {
         throw new NullPointerException("clazz may not be null!");
      }
      this.file = file;
      this.clazz = clazz;
   }

   @Override
   public T get() {
      return object;
   }

   @Override
   public void set(T toSave) {
      object = toSave;
   }

   @Override
   public Throwable getError() {
      return error;
   }

   @Override
   public String getFile() {
      return file;
   }

   @Override
   public boolean read(InputStream stream) {
      boolean success = true;
      InputStreamReader reader = new InputStreamReader(stream);
      try {
         object = GSON.fromJson(reader, clazz);
      } catch (JsonIOException | JsonSyntaxException e) {
         error = e;
         success = false;
      }
      return success;
   }

   @Override
   public boolean write(FileOutputStream outputStream) {
      boolean success = true;
      if (object != null) {
         OutputStreamWriter writer = new OutputStreamWriter(outputStream);
         try {
            GSON.toJson(object, writer);
         } catch (JsonIOException e) {
            error = e;
            success = false;
         }
      }
      return success;
   }

   @Override
   public int hashCode() {
      return Objects.hash(file,
                          clazz);
   }

   @Override
   public boolean equals(Object obj) {
      if (obj == this) {
         return true;
      }

      if (!(obj instanceof JsonResource)) {
         return false;
      }
      JsonResource that = (JsonResource) obj;
      return Objects.equals(file, that.file)
             && Objects.equals(clazz, that.clazz);
   }
}
