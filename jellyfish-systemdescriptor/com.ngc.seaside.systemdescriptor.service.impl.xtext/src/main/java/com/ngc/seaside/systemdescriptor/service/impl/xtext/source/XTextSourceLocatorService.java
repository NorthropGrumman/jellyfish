package com.ngc.seaside.systemdescriptor.service.impl.xtext.source;

import com.ngc.seaside.systemdescriptor.model.impl.xtext.IUnwrappable;
import com.ngc.seaside.systemdescriptor.source.api.ISourceLocation;
import com.ngc.seaside.systemdescriptor.source.api.ISourceLocatorService;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.impl.InternalNodeModelUtils;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.resource.ILocationInFileProvider;
import org.eclipse.xtext.util.ITextRegion;
import org.eclipse.xtext.util.LineAndColumn;

import javax.inject.Inject;

public class XTextSourceLocatorService implements ISourceLocatorService {

   private ILocationInFileProvider provider;

   @Inject
   public XTextSourceLocatorService(ILocationInFileProvider provider) {
      this.provider = provider;
   }

   @Override
   public ISourceLocation getLocation(Object element, boolean fullRegion) {
      if (element instanceof IUnwrappable) {
         @SuppressWarnings("unchecked")
         IUnwrappable<? extends EObject> wrappable = (IUnwrappable<? extends EObject>) element;
         EObject object = wrappable.unwrap();
         URI uri = EcoreUtil2.getNormalizedURI(object);
         INode node = NodeModelUtils.findActualNodeFor(object);
         ITextRegion region;
         if (fullRegion) {
            region = provider.getFullTextRegion(object);
         } else {
            region = provider.getSignificantTextRegion(object);
         }
         LineAndColumn lineAndColumn = WrapperInternalNodeModelUtils.getLineAndColumn(node, region.getOffset());
         return new XTextSourceLocation(uri, lineAndColumn.getLine(), lineAndColumn.getColumn(), region.getLength());
      }
      throw new IllegalArgumentException("Unable to get source location for element " + element);
   }

   private static class WrapperInternalNodeModelUtils extends InternalNodeModelUtils {
      public static LineAndColumn getLineAndColumn(INode node, int offset) {
         return InternalNodeModelUtils.getLineAndColumn(node, offset);
      }
   }
}
