package com.ngc.seaside.jellyfish.utilities.console.api;

/**
 * Simple functional interface to receive callbacks on the events within the model.
 *
 * @author justan.provence@ngc.com
 */
public interface ITableModelListener {

   /**
    * Provide a callback if the model changes.
    */
   void modelChanged();

}
