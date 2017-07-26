package com.ngc.seaside.jellyfish.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A type of annotation that can be applied to implementations of {@link IJellyFishCommand}s to configure the command.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface JellyFishCommandConfiguration {

   /**
    * If true, the command provider will automatically invoke the {@code ITemplateService} to unpack the template
    * associated with the command.  Commands can use this option to opt out of this behavior if the command desires more
    * fine grained control of the invocation of the {@code ITemplateService}.
    */
   boolean autoTemplateProcessing() default true;

   /**
    * If true, the command provider will invoke the associated command even if the System Descriptor contains parsing
    * errors.
    */
   boolean requireValidSystemDescriptor() default true;
}
