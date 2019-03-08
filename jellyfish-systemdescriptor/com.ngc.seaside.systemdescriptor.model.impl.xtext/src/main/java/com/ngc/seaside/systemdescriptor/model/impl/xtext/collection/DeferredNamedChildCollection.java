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
package com.ngc.seaside.systemdescriptor.model.impl.xtext.collection;

import com.ngc.seaside.systemdescriptor.model.api.INamedChild;
import com.ngc.seaside.systemdescriptor.model.impl.basic.NamedChildCollection;

import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class DeferredNamedChildCollection<P, T extends INamedChild<P>> extends NamedChildCollection<P, T> {

   private AtomicBoolean initialized = new AtomicBoolean(false);

   @Override
   public Optional<T> getByName(String name) {
      if (!initialized.getAndSet(true)) {
         initialize();
      }
      return super.getByName(name);
   }

   @Override
   public int size() {
      if (!initialized.getAndSet(true)) {
         initialize();
      }
      return super.size();
   }

   @Override
   public boolean isEmpty() {
      if (!initialized.getAndSet(true)) {
         initialize();
      }
      return super.isEmpty();
   }

   @Override
   public boolean contains(Object o) {
      if (!initialized.getAndSet(true)) {
         initialize();
      }
      return super.contains(o);
   }

   @Override
   public Iterator<T> iterator() {
      if (!initialized.getAndSet(true)) {
         initialize();
      }
      return super.iterator();
   }

   @Override
   public Object[] toArray() {
      if (!initialized.getAndSet(true)) {
         initialize();
      }
      return super.toArray();
   }

   @Override
   public <T1> T1[] toArray(T1[] a) {
      if (!initialized.getAndSet(true)) {
         initialize();
      }
      return super.toArray(a);
   }

   @Override
   public boolean add(T t) {
      if (!initialized.getAndSet(true)) {
         initialize();
      }
      return super.add(t);
   }

   @Override
   public boolean remove(Object o) {
      if (!initialized.getAndSet(true)) {
         initialize();
      }
      return super.remove(o);
   }

   @Override
   public boolean containsAll(Collection<?> c) {
      if (!initialized.getAndSet(true)) {
         initialize();
      }
      return super.containsAll(c);
   }

   @Override
   public boolean addAll(Collection<? extends T> c) {
      if (!initialized.getAndSet(true)) {
         initialize();
      }
      return super.addAll(c);
   }

   @Override
   public boolean removeAll(Collection<?> c) {
      if (!initialized.getAndSet(true)) {
         initialize();
      }
      return super.removeAll(c);
   }

   @Override
   public boolean retainAll(Collection<?> c) {
      if (!initialized.getAndSet(true)) {
         initialize();
      }
      return super.retainAll(c);
   }

   @Override
   public void clear() {
      if (!initialized.getAndSet(true)) {
         initialize();
      }
      super.clear();
   }

   protected abstract void initialize();
}
