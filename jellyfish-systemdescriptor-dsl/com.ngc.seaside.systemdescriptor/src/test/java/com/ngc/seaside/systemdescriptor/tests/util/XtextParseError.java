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

