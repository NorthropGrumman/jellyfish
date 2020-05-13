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
package com.ngc.seaside.jellyfish.utilities;

import java.util.Objects;

/**
 * @author justan.provence@ngc.com
 */
public class TestItem {

   private String param1;
   private String param2;
   private String param3;

   /**
    * Constructor
    * @param param1 used to construct object
    * @param param2 used to construct object
    * @param param3 used to construct object
    */
   public TestItem(String param1, String param2, String param3) {
      this.param1 = param1;
      this.param2 = param2;
      this.param3 = param3;
   }

   public String getParam1() {
      return param1;
   }

   public String getParam2() {
      return param2;
   }

   public String getParam3() {
      return param3;
   }

   @Override
   public int hashCode() {
      return Objects.hash(
            param1, param2, param3
      );
   }

   @Override
   public boolean equals(Object obj) {
      if (obj == this) {
         return true;
      }

      if (!(obj instanceof TestItem)) {
         return false;
      }
      TestItem that = (TestItem) obj;
      return Objects.equals(param1, that.param1)
            && Objects.equals(param2, that.param2)
            && Objects.equals(param3, that.param3);
   }
}
