package com.ngc.seaside.sonar.jellyfish;

import com.ngc.seaside.sonar.jellyfish.languages.SystemDescriptorLanguage;
import com.ngc.seaside.sonar.jellyfish.rules.SystemDescriptorSensor;

import org.sonar.api.Plugin;

/**
 * The main entry point for the Sonarqube Jellyfish plugin.
 */
public class JellyfishPlugin implements Plugin {
    @Override
    public void define(Context c) {
       c.addExtension(SystemDescriptorLanguage.class);
       c.addExtension(SystemDescriptorSensor.class);
    }
}
