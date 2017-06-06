package com.ngc.seaside.systemdescriptor.service.impl.xtext;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;

import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.service.api.IParsingResult;
import com.ngc.seaside.systemdescriptor.service.api.ISystemDescriptorService;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.parsing.ParsingDelegate;
import com.ngc.seaside.systemdescriptor.validation.api.ISystemDescriptorValidator;

import java.nio.file.Path;
import java.util.Collection;
import java.util.function.Consumer;

/**
 * Provides an implementation of the {@code ISystemDescriptorService} that uses the XText JellyFish DSL.  New instances
 * of this service should always be obtained via {@link XTextSystemDescriptorServiceBuilder#forIntegration(Consumer)}
 * or {@link XTextSystemDescriptorServiceBuilder#forApplication()}.  In most classes, application simply obtain a
 * reference to this service via injection.  See the Javadoc of {@link XTextSystemDescriptorServiceBuilder} for more
 * information.
 *
 * <p/>
 *
 * This implementation delegates most concerns to other objects.
 */
public class XTextSystemDescriptorService implements ISystemDescriptorService {

   /**
    * Responsible for parsing system descriptor files.
    */
   private final ParsingDelegate parsingDelegate;

   /**
    * Creates a new {@code XTextSystemDescriptorService}.  <b>Applications should not invoke this constructor directly.
    * See the Javadoc for more information.</b>
    */
   @Inject
   public XTextSystemDescriptorService(ParsingDelegate parsingDelegate) {
      this.parsingDelegate = Preconditions.checkNotNull(parsingDelegate, "parsingDelegate may not be null!");
   }

   @Override
   public IParsingResult parseProject(Path projectDirectory) {
      return parsingDelegate.parseProject(projectDirectory);
   }

   @Override
   public IParsingResult parseFiles(Collection<Path> paths) {
      return parsingDelegate.parseFiles(paths);
   }

   @Override
   public ISystemDescriptor immutableCopy(ISystemDescriptor descriptor) {
      throw new UnsupportedOperationException("not implemented");
   }

   @Override
   public void addValidator(ISystemDescriptorValidator validator) {
      throw new UnsupportedOperationException("not implemented");
   }

   @Override
   public boolean removeValidator(ISystemDescriptorValidator validator) {
      throw new UnsupportedOperationException("not implemented");
   }
}
