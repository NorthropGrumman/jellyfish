package com.ngc.seaside.systemdescriptor.ui.quickfix.imports;

import com.google.inject.ImplementedBy;

import java.util.List;
import java.util.OptionalInt;

/**
 * Interface for selecting an element from a list of elements
 *
 * @param <T> element type
 * @param <C> context type
 */
@FunctionalInterface
@ImplementedBy(DialogSelector.class)
public interface IReferenceSelector<T> {

   /**
    * Selects one of the given choices given the context.
    * 
    * @param choices list of choices
    * @param context the context for this choice
    * @return the index of the choice made, or {@link OptionalInt#empty()} if no choice was made
    */
   public OptionalInt select(List<? extends T> choices, XtextReferenceContext context);

}
