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
package com.ngc.seaside.jellyfish.utilities.console.api;

import java.util.ArrayList;
import java.util.List;

/**
 * Default implementation of the Table model backed by a {@link List}
 *
 * @author justan.provence@ngc.com
 */
public class DefaultTableModel<T> implements ITableModel<T> {

   private List<T> items = new ArrayList<>();
   private List<ITableModelListener> listeners = new ArrayList<>();

   @Override
   public void addTableModelListener(ITableModelListener listener) {
      listeners.add(listener);
   }

   @Override
   public void removeTableModelListener(ITableModelListener listener) {
      listeners.remove(listener);
   }

   @Override
   public List<T> getItems() {
      return new ArrayList<>(items);
   }

   @Override
   public void addItem(T item) {
      items.add(item);
      notifyListeners();
   }

   @Override
   public void addItems(List<T> items) {
      this.items.addAll(items);
      notifyListeners();
   }

   @Override
   public void removeItem(T item) {
      items.remove(item);
      notifyListeners();
   }

   @Override
   public void removeAll() {
      items.clear();
      notifyListeners();
   }

   /**
    * This method will notify the listeners in a fail-safe way. This means that if one listener
    * was to error it would not cause other listeners to not be notified.
    */
   private void notifyListeners() {
      for (ITableModelListener listener : listeners) {
         try {
            listener.modelChanged();
         } catch (Throwable t) {
            //catch any error the listener might throw
            //this is to protect any subsequent listeners from getting called due to
            //a listener prior to it throwing an error
         }
      }
   }

}
