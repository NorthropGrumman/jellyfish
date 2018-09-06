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
