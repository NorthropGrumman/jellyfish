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
package com.ngc.seaside.systemdescriptor.service.impl.gherkin.model;

import com.google.common.base.Preconditions;
import com.ngc.seaside.systemdescriptor.model.api.INamedChild;
import com.ngc.seaside.systemdescriptor.model.api.INamedChildCollection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Implements the INamedChildCollection interface where duplicates are allowed. {@link #getByName(String)} returns the
 * most recent child with the given name added to the collection. {@link #iterator()} returns all children added, even
 * if the names are duplicated.
 *
 * @param <P> Parent class of the child
 * @param <T> INamedChild class type
 */
public class NamedChildCollectionWithDuplicateNames<P, T extends INamedChild<P>>
         implements INamedChildCollection<P, T> {

   private final Map<String, T> childrenByName;
   private final List<T> children;

   private NamedChildCollectionWithDuplicateNames(Map<String, T> children) {
      this.childrenByName = new LinkedHashMap<>(children);
      this.children = new ArrayList<>(children.values());
   }

   public NamedChildCollectionWithDuplicateNames() {
      this(new LinkedHashMap<>());
   }

   @Override
   public Optional<T> getByName(String name) {
      Preconditions.checkNotNull(name, "name may not be null!");
      Preconditions.checkArgument(!name.trim().isEmpty(), "name may not be empty!");
      return Optional.ofNullable(childrenByName.get(name));
   }

   @Override
   public int size() {
      return children.size();
   }

   @Override
   public boolean isEmpty() {
      return children.isEmpty();
   }

   @Override
   public boolean contains(Object o) {
      return children.contains(o);
   }

   @Override
   public Iterator<T> iterator() {
      return children.iterator();
   }

   @Override
   public Object[] toArray() {
      return children.toArray();
   }

   @Override
   public <T1> T1[] toArray(T1[] a) {
      return children.toArray(a);
   }

   @Override
   public boolean add(T t) {
      Preconditions.checkNotNull(t, "t may not be null!");
      childrenByName.put(t.getName(), t);
      if (!children.contains(t)) {
         children.add(t);
      }
      return true;
   }

   @Override
   public boolean remove(Object o) {
      Preconditions.checkNotNull(o, "o may not be null!");
      Preconditions.checkArgument(o instanceof INamedChild,
               "o must be an instance of %s!",
               INamedChild.class.getName());
      INamedChild<?> casted = (INamedChild<?>) o;
      boolean removed = children.remove(casted);
      if (removed) {
         childrenByName.remove(casted.getName(), casted);
      }
      return removed;
   }

   @Override
   public boolean containsAll(Collection<?> c) {
      Preconditions.checkNotNull(c, "c may not be null!");
      return children.containsAll(c);
   }

   @Override
   public boolean addAll(Collection<? extends T> c) {
      Preconditions.checkNotNull(c, "c may not be null!");
      boolean changed = false;
      for (T child : c) {
         changed |= add(child);
      }
      return changed;
   }

   @SuppressWarnings("unchecked")
   @Override
   public boolean removeAll(Collection<?> c) {
      Preconditions.checkNotNull(c, "c may not be null!");
      boolean changed = false;
      for (Object o : c) {
         boolean removed = children.remove(o);
         if (removed) {
            String name = ((T) o).getName();
            childrenByName.remove(name, (T) o);
            changed = true;
         }
      }
      return changed;
   }

   @Override
   public boolean retainAll(Collection<?> c) {
      Preconditions.checkNotNull(c, "c may not be null!");
      boolean changed = false;
      for (Iterator<T> itr = children.iterator(); itr.hasNext();) {
         T next = itr.next();
         if (!c.contains(next)) {
            itr.remove();
            childrenByName.remove(next.getName(), next);
            changed = true;
         }
      }
      return changed;
   }

   @Override
   public void clear() {
      childrenByName.clear();
      children.clear();
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      }
      if (!(o instanceof NamedChildCollectionWithDuplicateNames)) {
         return false;
      }
      NamedChildCollectionWithDuplicateNames<?, ?> that = (NamedChildCollectionWithDuplicateNames<?, ?>) o;
      return Objects.equals(children, that.children);
   }

   @Override
   public int hashCode() {
      return Objects.hash(children);
   }

   @Override
   public String toString() {
      return "NamedChildCollection["
               + "children=" + children
               + ']';
   }

}
