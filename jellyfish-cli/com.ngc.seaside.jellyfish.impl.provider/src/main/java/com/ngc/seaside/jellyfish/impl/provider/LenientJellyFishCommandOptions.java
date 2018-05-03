package com.ngc.seaside.jellyfish.impl.provider;

import com.ngc.seaside.jellyfish.api.DefaultJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.DefaultParameterCollection;

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
