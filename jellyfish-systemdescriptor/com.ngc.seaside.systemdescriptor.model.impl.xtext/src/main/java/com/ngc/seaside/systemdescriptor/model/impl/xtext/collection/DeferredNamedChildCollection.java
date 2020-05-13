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
