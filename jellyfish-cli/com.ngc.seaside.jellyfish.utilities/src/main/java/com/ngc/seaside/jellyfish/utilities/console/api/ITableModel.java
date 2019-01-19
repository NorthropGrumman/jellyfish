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
