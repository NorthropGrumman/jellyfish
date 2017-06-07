package com.ngc.seaside.systemdescriptor.model.impl.basic;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterators;

import com.ngc.seaside.systemdescriptor.model.api.INamedChild;
import com.ngc.seaside.systemdescriptor.model.api.INamedChildCollection;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Implements the INamedChildCollection interface.
 *
 * @param <P> Parent class of the child
 * @param <T> INamedChild class type
 * @author thooper
 */
public class NamedChildCollection<P, T extends INamedChild<P>> implements INamedChildCollection<P, T> {

   private final Map<String, T> children;
   private Consumer<T> onChildAdded;
   private Consumer<T> onChildRemoved;

   private NamedChildCollection(Map<String, T> children) {
      this.children = new LinkedHashMap<>(children);
   }

   public NamedChildCollection() {
      this(new LinkedHashMap<>());
   }

   @Override
   public Optional<T> getByName(String name) {
      Preconditions.checkNotNull(name, "name may not be null!");
      Preconditions.checkArgument(!name.trim().isEmpty(), "name may not be empty!");
      return Optional.ofNullable(children.get(name));
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
      return children.containsValue(o);
   }

   @Override
   public Iterator<T> iterator() {
      return new DelegatingIterator(children.values().iterator());
   }

   @Override
   public Object[] toArray() {
      return children.values().toArray();
   }

   @Override
   public <T1> T1[] toArray(T1[] a) {
      return children.values().toArray(a);
   }

   @Override
   public boolean add(T t) {
      Preconditions.checkNotNull(t, "t may not be null!");
      T previousChild = children.put(t.getName(), t);
      if (previousChild != null) {
         postChildRemoved(previousChild);
      }
      postChildAdded(t);
      return true;
   }

   @SuppressWarnings("unchecked")
   @Override
   public boolean remove(Object o) {
      Preconditions.checkNotNull(o, "o may not be null!");
      Preconditions.checkArgument(o instanceof INamedChild,
                                  "o must be an instance of %s!",
                                  INamedChild.class.getName());
      INamedChild<?> casted = (INamedChild<?>) o;
      boolean removed = children.remove(casted.getName(), casted);
      if (removed) {
         // Safe because the item was removed the list and it couldn't have been inserted unless it was the right type.
         postChildRemoved((T) casted);
      }
      return removed;
   }

   @Override
   public boolean containsAll(Collection<?> c) {
      Preconditions.checkNotNull(c, "c may not be null!");
      return children.values().containsAll(c);
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

   @Override
   public boolean removeAll(Collection<?> c) {
      Preconditions.checkNotNull(c, "c may not be null!");
      boolean changed = false;
      for (Object child : c) {
         changed |= remove(child);
      }
      return changed;
   }

   @Override
   public boolean retainAll(Collection<?> c) {
      Preconditions.checkNotNull(c, "c may not be null!");
      boolean changed = false;
      for (Map.Entry<String, T> e : children.entrySet()) {
         if (!c.contains(e.getValue())) {
            changed |= remove(e.getValue());
         }
      }
      return changed;
   }

   @Override
   public void clear() {
      for (T child : children.values()) {
         postChildRemoved(child);
      }
      children.clear();
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      }
      if (!(o instanceof NamedChildCollection)) {
         return false;
      }
      NamedChildCollection<?, ?> that = (NamedChildCollection<?, ?>) o;
      return Objects.equals(children, that.children);
   }

   @Override
   public int hashCode() {
      return Objects.hash(children);
   }

   @Override
   public String toString() {
      return "NamedChildCollection[" +
             "children=" + children +
             ']';
   }

   public NamedChildCollection<P, T> setOnChildAdded(Consumer<T> onChildAdded) {
      this.onChildAdded = onChildAdded;
      return this;
   }

   public NamedChildCollection<P, T> setOnChildRemoved(Consumer<T> onChildRemoved) {
      this.onChildRemoved = onChildRemoved;
      return this;
   }

   protected void postChildAdded(T child) {
      if (onChildAdded != null) {
         onChildAdded.accept(child);
      }
   }

   protected void postChildRemoved(T child) {
      if (onChildRemoved != null) {
         onChildRemoved.accept(child);
      }
   }

   private class DelegatingIterator implements Iterator<T> {

      private final Iterator<T> wrapped;
      private T current;

      private DelegatingIterator(Iterator<T> wrapped) {
         this.wrapped = wrapped;
      }

      @Override
      public boolean hasNext() {
         return wrapped.hasNext();
      }

      @Override
      public T next() {
         T next = wrapped.next();
         current = next;
         return next;
      }

      @Override
      public void remove() {
         wrapped.remove();
         postChildRemoved(current);
         current = null;
      }

      @Override
      public void forEachRemaining(Consumer<? super T> action) {
         wrapped.forEachRemaining(action);
      }
   }

   public static <P, T extends INamedChild<P>> INamedChildCollection<P, T> immutable(
         INamedChildCollection<P, T> collection) {
      Map<String, T> children = new LinkedHashMap<>(collection.size());
      for (T child : collection) {
         children.put(child.getName(), child);
      }
      return new ImmutableNamedChildCollection<>(children);
   }

   private static class ImmutableNamedChildCollection<P, T extends INamedChild<P>>
         extends NamedChildCollection<P, T> {

      private ImmutableNamedChildCollection(Map<String, T> children) {
         super(children);
      }

      @Override
      public Iterator<T> iterator() {
         return Iterators.unmodifiableIterator(super.iterator());
      }

      @Override
      public boolean add(T t) {
         throw new UnsupportedOperationException("object is not modifiable!");
      }

      @Override
      public boolean remove(Object o) {
         throw new UnsupportedOperationException("object is not modifiable!");
      }

      @Override
      public boolean addAll(Collection<? extends T> c) {
         throw new UnsupportedOperationException("object is not modifiable!");
      }

      @Override
      public boolean removeAll(Collection<?> c) {
         throw new UnsupportedOperationException("object is not modifiable!");
      }

      @Override
      public boolean retainAll(Collection<?> c) {
         throw new UnsupportedOperationException("object is not modifiable!");
      }

      @Override
      public void clear() {
         throw new UnsupportedOperationException("object is not modifiable!");
      }
   }
}
