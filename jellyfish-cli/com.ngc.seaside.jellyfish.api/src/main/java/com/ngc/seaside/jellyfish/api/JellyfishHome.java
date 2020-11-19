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
package com.ngc.seaside.jellyfish.api;

import java.nio.file.Path;
import java.nio.file.Paths;

public class JellyfishHome {

    /**
     * The names of the variables to use when determining the value of the home directory.  They are listed in order or
     * precedence.
     */
    static final String[] PROPERTY_NAMES = {"jellyfishHome", "JELLYFISH_HOME", "JF_HOME", "blocsHome", "BLOCS_HOME", "NG_FW_HOME"};

    private JellyfishHome() {
    }

    /**
     * Gets the directory that serves as the root directory for the application.  This value is configured by system
     * properties and environment variables.  If no value is configured, the {@link #defaultValue() default} is used.
     *
     * @return the directory that serves as the root directory for the application
     */
    public static Path get() {
       String home = null;
       for (int i = 0; i < PROPERTY_NAMES.length && home == null; i++) {
          home = System.getProperty(PROPERTY_NAMES[i]);
          if (home == null) {
             home = System.getenv(PROPERTY_NAMES[i]);
          }
       }

       // If no value was found, default to the current working directory.
       Path path = defaultValue();
       if (home != null && !home.trim().isEmpty()) {
          path = Paths.get(home);
       }
       return path;
    }

    /**
     * Gets the default directory that serves as the root directory for the application.  In most cases, uses will want
     * to use {@link #get()} as that method allows for users to override the default value.
     *
     * @return the default directory for the application
     */
    public static Path defaultValue() {
       return Paths.get("");
    }
}
