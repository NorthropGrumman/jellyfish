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
package com.ngc.seaside.systemdescriptor.model.impl.xtext.exception;

/**
 * A type of exception that indicates the adapting code doesn't recognize some XText type.  These type of errors usually
 * indicate that the packaged adapters and the XText DSL are not in sync or one is out of date.
 */
public class UnrecognizedXtextTypeException extends RuntimeException {

   private final Object xtextType;

   public UnrecognizedXtextTypeException(Object xtextType) {
      super(String.format("unrecognized XText type: %s", xtextType.getClass().getName()));
      this.xtextType = xtextType;
   }

   public Object getXtextType() {
      return xtextType;
   }
}
