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
@Deprecated
public @interface JellyFishCommandConfiguration {

   /**
    * If true, the command provider will automatically invoke the {@code ITemplateService} to unpack the template
    * associated with the command.  Commands can use this option to opt out of this behavior if the command desires more
    * fine grained control of the invocation of the {@code ITemplateService}.
    */
   boolean autoTemplateProcessing() default true;

   /**
    * If false, the command provider will invoke the associated command even if the System Descriptor contains parsing
    * errors.
    */
   boolean requireValidSystemDescriptor() default true;
}
