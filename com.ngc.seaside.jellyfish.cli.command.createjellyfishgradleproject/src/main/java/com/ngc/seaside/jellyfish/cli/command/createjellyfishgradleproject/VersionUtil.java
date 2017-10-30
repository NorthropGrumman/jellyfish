package com.ngc.seaside.jellyfish.cli.command.createjellyfishgradleproject;

import com.google.common.base.Preconditions;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class VersionUtil {

   private final static String CONFIG_PROPERTIES_FILE =
         "com.ngc.seaside.jellyfish.cli.command.createjellyfishgradleproject.config.properties";
   private final static String VERSION_PROPERTY = "version";

   private VersionUtil() {
   }

   public static String getCurrentJellyfishVersion() {
      String version;
      try (InputStream is = VersionUtil.class.getClassLoader().getResourceAsStream(CONFIG_PROPERTIES_FILE)) {
         Properties props = new Properties();
         props.load(is);
         version = props.getProperty(VERSION_PROPERTY);
         Preconditions.checkState(version != null, "version not set in configuration properties!");
      } catch (IOException e) {
         throw new IllegalStateException("failed to load configuration properties from classpath!", e);
      }
      return version;
   }
}
