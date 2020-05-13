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

import java.util.List;

/**
 * The table model. This provides the data presented in the table.
 * Listeners can be added to this class to determine if changes to the model have occurred.
 *
 * @author justan.provence@ngc.com
 */
public interface ITableModel<T> {

   /**
    * Add a listener to the model.
    *
    * @param listener the listener.
    */
   void addTableModelListener(ITableModelListener listener);

   /**
    * Remove a listener from the model.
    *
    * @param listener the listener.
    */
   void removeTableModelListener(ITableModelListener listener);

   /**
    * Get the items in the model.
    *
    * @return the list of items.
    */
   List<T> getItems();

   /**
    * Add the item to the model.
    *
    * @param item the item to add.
    */
   void addItem(T item);

   /**
    * Add the items to the model.
    *
    * @param items the items to add.
    */
   void addItems(List<T> items);

   /**
    * Remove the item from the model.
    *
    * @param item the item to remove.
    */
   void removeItem(T item);

   /**
    * Remove the items from the model.
    */
   void removeAll();

}
