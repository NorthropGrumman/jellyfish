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
package com.ngc.seaside.systemdescriptor.tests.resources;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.testing.util.ResourceHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;

public class ParsingTestResource {

   private final String source;
   private final Collection<ParsingTestResource> requiredResources = new ArrayList<>();

   private ParsingTestResource(String source) {
      this.source = source;
   }

   /**
    * Gets teh source of the resource.
    */
   public String getSource() {
      return source;
   }

   /**
    * Gets the resources required by this resource.
    */
   public Collection<ParsingTestResource> getRequiredResources() {
      return requiredResources;
   }

   /**
    * Prepares this resource for parsing.
    */
   public Resource preparedForParse(ResourceHelper resourceHelper) throws Exception {
      return preparedForParse(resourceHelper, Arrays.asList(this));
   }

   /**
    * Prepares the given set of resources for parsing.
    */
   public static Resource preparedForParse(ResourceHelper resourceHelper, Collection<ParsingTestResource> resources)
         throws Exception {
      if (resources.isEmpty()) {
         throw new IllegalArgumentException("resources can't be empty!");
      }

      Collection<ParsingTestResource> all = new HashSet<>(resources);
      for (ParsingTestResource r : resources) {
         all.addAll(r.getRequiredResources());
      }

      Iterator<ParsingTestResource> i = all.iterator();
      ParsingTestResource currentResource = i.next();

      Resource prepared = resourceHelper.resource(currentResource.getSource(), URI.createURI("root.sd"));
      while (i.hasNext()) {
         currentResource = i.next();
         resourceHelper.resource(currentResource.getSource(), prepared.getResourceSet());
      }

      return prepared;
   }

   @Override
   public boolean equals(Object o) {
      if (!(o instanceof ParsingTestResource)) {
         return false;
      }
      ParsingTestResource that = (ParsingTestResource) o;
      return Objects.equals(source, that.source);
   }

   @Override
   public int hashCode() {
      return Objects.hash(source);
   }

   @Override
   public String toString() {
      return source;
   }

   /**
    * Prepares the source and its dependencies for parsing.
    */
   public static ParsingTestResource resource(String source, ParsingTestResource... required) {
      ParsingTestResource r = new ParsingTestResource(source);
      for (ParsingTestResource requiredResource : required) {
         r.requiredResources.add(requiredResource);
      }
      return r;
   }
}
