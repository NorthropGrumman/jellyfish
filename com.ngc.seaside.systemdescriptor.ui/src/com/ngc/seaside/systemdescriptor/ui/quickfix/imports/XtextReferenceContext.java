package com.ngc.seaside.systemdescriptor.ui.quickfix.imports;

import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.util.ITextRegion;

/**
 * Context for resolving conflicting possible imports.
 */
public final class XtextReferenceContext {

   private final XtextResource resource;
   private final ITextRegion usage;

   public XtextReferenceContext(XtextResource resource, ITextRegion usage) {
      this.resource = resource;
      this.usage = usage;
   }

   /**
    * @return the xtext resource
    */
   public XtextResource getResource() {
      return resource;
   }

   /**
    * @return the text region of the reference trying to be resolved
    */
   public ITextRegion getUsage() {
      return usage;
   }

}
