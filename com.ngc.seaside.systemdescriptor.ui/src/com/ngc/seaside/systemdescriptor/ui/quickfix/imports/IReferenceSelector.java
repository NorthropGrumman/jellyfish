package com.ngc.seaside.systemdescriptor.ui.quickfix.imports;

import java.util.List;
import java.util.OptionalInt;

import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.util.ITextRegion;

import com.google.inject.ImplementedBy;

/**
 * Interface for selecting an element from a list of elements
 */
@FunctionalInterface
@ImplementedBy(DialogSelector.class)
public interface IReferenceSelector {

   /**
    * Selects one of the given choices given the resource and usage context.
    * 
    * @param choices list of choices
    * @param resource resource
    * @param usage text region when making the choice
    * @return the index of the choice made, or {@link OptionalInt#empty()} if no choice was made
    */
   public <T> OptionalInt select(List<? extends T> choices, XtextResource resource, ITextRegion usage);

}
