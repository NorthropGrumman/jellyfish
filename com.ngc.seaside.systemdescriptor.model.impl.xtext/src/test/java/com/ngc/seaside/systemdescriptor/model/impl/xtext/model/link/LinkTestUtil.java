package com.ngc.seaside.systemdescriptor.model.impl.xtext.model.link;

import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
import com.ngc.seaside.systemdescriptor.systemDescriptor.LinkDeclaration;

public class LinkTestUtil {

  private LinkTestUtil() {
  }

  public static class LinkTestSetup {
    public final LinkDeclaration declaration;
    public final IWrapperResolver resolver;

    public LinkTestSetup(LinkDeclaration declaration,
                         IWrapperResolver resolver) {
      this.declaration = declaration;
      this.resolver = resolver;
    }
  }
}
