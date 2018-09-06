/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
 */
package com.ngc.seaside.jellyfish.impl.provider;

import com.ngc.seaside.jellyfish.api.DefaultJellyFishCommandOptions;

public class LenientJellyFishCommandOptions extends DefaultJellyFishCommandOptions {

   private Throwable parsingException;

   public Throwable getParsingException() {
      return parsingException;
   }

   public LenientJellyFishCommandOptions setParsingException(Throwable parsingException) {
      this.parsingException = parsingException;
      return this;
   }
}
