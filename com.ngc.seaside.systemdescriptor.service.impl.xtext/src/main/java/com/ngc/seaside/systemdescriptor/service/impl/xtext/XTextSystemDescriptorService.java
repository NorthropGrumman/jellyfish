package com.ngc.seaside.systemdescriptor.service.impl.xtext;

import com.google.common.base.Preconditions;
import com.google.inject.Injector;

import com.ngc.seaside.systemdescriptor.SystemDescriptorStandaloneSetup;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.service.api.IParsingResult;
import com.ngc.seaside.systemdescriptor.service.api.ISystemDescriptorService;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.module.XTextSystemDescriptorServiceModule;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.parsing.ParsingDelegate;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.validation.ValidationDelegate;
import com.ngc.seaside.systemdescriptor.validation.api.ISystemDescriptorValidator;

import java.nio.file.Path;
import java.util.Collection;

/**
 * Provides an implementation of the {@code ISystemDescriptorService} that uses the XText JellyFish DSL. New instances
 * of this service should always be obtained via injection with Guice.  This class is configured in one of to ways:
 * either for use <i>within</i> Eclipse or for standalone use <i>outside</i> of Eclipse.  If using this service inside
 * eclipse include the {@link XTextSystemDescriptorServiceModule} with the Guice configuration like so:
 *
 * <pre>
 *    {@code
 *      Collection<Module> modules = new ArrayList<>();
 *      modules.add(new XTextSystemDescriptorServiceModule());
 *      Injector injector = Guice.createInjector(modules);
 *      ISystemDescriptorService service = injector.getInstance(ISystemDescriptorService.class);
 *    }
 * </pre>
 *
 * Alternately, use {@link XTextSystemDescriptorServiceModule#forStandaloneUsage()} to use the service outside of
 * Eclipse:
 *
 * <pre>
 *    {@code
 *      Collection<Module> modules = new ArrayList<>();
 *      modules.add(XTextSystemDescriptorServiceModule.forStandaloneUsage());
 *      Injector injector = Guice.createInjector(modules);
 *      ISystemDescriptorService service = injector.getInstance(ISystemDescriptorService.class);
 *    }
 * </pre>
 *
 * Most of the time the service should be injected into an application component instead of being resolved directly:
 *
 * <pre>
 *    {@code
 *      public class MyApplication {
 *        private final ISystemDescriptorService service;
 *
 *        {@literal @}Inject
 *        public MyApplication(ISystemDescriptorService service) {
 *          this.service = service;
 *        }
 *      }
 *    }
 * </pre>
 *
 * <b>An implementation of the {@code ILogService} interface must be bound by an application's module</b> in order for
 * service to be created correctly.
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
    * Responsible for validating system descriptor files and handling validators plugins.
    */
   private final ValidationDelegate validationDelegate;

   /**
    * Creates an instance of the {@code XTextSystemDescriptorService} <b>for use within Eclipse</b>.  An instance of
    * this service should always be obtained via an {@link Injector} instead of invoking this constructor directly.  See
    * the {@link XTextSystemDescriptorService class javadoc}.
    */
   public XTextSystemDescriptorService(ParsingDelegate parsingDelegate,
                                       ValidationDelegate validationDelegate) {
      this.parsingDelegate = Preconditions.checkNotNull(parsingDelegate, "parsingDelegate may not be null!");
      this.validationDelegate = Preconditions.checkNotNull(validationDelegate, "validationDelegate may not be null!");
   }

   /**
    * Creates a new instance of the standalone XText system descriptor service <b>for use outside of Eclipse</b>.  An
    * instance of this service should always be obtained via an {@link Injector} instead of invoking this constructor
    * directly.  See the {@link XTextSystemDescriptorService class javadoc}.
    */
   public XTextSystemDescriptorService(Injector injector,
                                       ParsingDelegate parsingDelegate,
                                       ValidationDelegate validationDelegate) {
      this(parsingDelegate, validationDelegate);
      Preconditions.checkNotNull(injector, "injector may not be null!");
      // Configure XText.
      new SystemDescriptorStandaloneSetup().register(injector);
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
      validationDelegate.addValidator(validator);
   }

   @Override
   public boolean removeValidator(ISystemDescriptorValidator validator) {
      return validationDelegate.removeValidator(validator);
   }
}
