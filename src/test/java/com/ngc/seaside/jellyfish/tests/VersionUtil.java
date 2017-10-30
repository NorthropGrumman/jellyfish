package com.ngc.seaside.jellyfish.tests;

import com.google.common.base.Preconditions;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class VersionUtil {

   private final static String CONFIG_PROPERTIES_FILE = "config.properties";
   private final static String JELLYFISH_CLI_VERSION_PROPERTY = "jellyfishCliVersion";

   private VersionUtil() {
   }

   public static String getJellyfishVersion() {
      String version;
      try (InputStream is = VersionUtil.class.getClassLoader().getResourceAsStream(CONFIG_PROPERTIES_FILE)) {
         Properties props = new Properties();
         props.load(is);
         version = props.getProperty(JELLYFISH_CLI_VERSION_PROPERTY);
         Preconditions.checkState(version != null, "jellyfishCliVersion not set in configuration properties!");
      } catch (IOException e) {
         throw new IllegalStateException("failed to load configuration properties from classpath!", e);
      }
      return version;
   }
}
