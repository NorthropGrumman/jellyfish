/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
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
// See https://bugs.eclipse.org/bugs/show_bug.cgi?id=375027

/*******************************************************************************
 * Copyright (c) 2011 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package com.ngc.seaside.systemdescriptor.tests.util;

import org.eclipse.xtext.validation.Issue;

import java.util.List;

/**
 * Wrap issues found while parsing in an exception to make them easier
 * to handle in JUnit tests.
 *
 * @author Aaron Digulla - Initial contribution and API
 */
public class XtextParseError extends RuntimeException {

   private static final long serialVersionUID = 1L;

   private final List<Issue> issues;

   public XtextParseError(List<Issue> issues) {
      super(getMessage(issues));
      this.issues = issues;
   }

   private static String getMessage(List<Issue> issues) {
      if (issues == null || issues.isEmpty()) {
         return "No issues - why was I called?";
      }

      if (1 == issues.size()) {
         return issues.get(0).toString();
      }

      return issues.size() + " issues:\n" + join(issues, "\n");
   }

   private static String join(List<Issue> issues, String delim) {

      StringBuilder buffer = new StringBuilder();
      String d = "";

      for (Issue issue : issues) {
         buffer.append(d).append(issue);
         d = delim;
      }

      return buffer.toString();
   }

   public List<Issue> getIssues() {
      return issues;
   }
}

