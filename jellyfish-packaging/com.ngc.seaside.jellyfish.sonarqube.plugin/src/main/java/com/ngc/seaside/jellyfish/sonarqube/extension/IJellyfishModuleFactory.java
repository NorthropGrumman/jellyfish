/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.ngc.seaside.jellyfish.sonarqube.extension;

import com.google.inject.Module;

import com.ngc.seaside.jellyfish.sonarqube.JellyfishPlugin;

import java.util.Collection;

/**
 * A basic extension point that can allow Sonarqube plugin writers to create a new Sonarqube plugin that contains
 * additional analysis, profiles, etc.  This factory should produce the modules used to run Jellyfish.  Most of the
 * time,
 * users can configure the {@link DefaultJellyfishModuleFactory} via
 * {@link JellyfishPlugin#configureDefaultModuleFactory(DefaultJellyfishModuleFactory)}
 * instead of implementing this interface directly.
 */
public interface IJellyfishModuleFactory {

   /**
    * Gets the modules that should be used when running Jellyfish.
    *
    * @param loggingEnabled if true, a module that enables logging should included.  If this value is false, a module
    *                       that disabled logging should be included
    * @return the modules used to run Jellyfish
    */
   Collection<Module> getJellyfishModules(boolean loggingEnabled);


}
