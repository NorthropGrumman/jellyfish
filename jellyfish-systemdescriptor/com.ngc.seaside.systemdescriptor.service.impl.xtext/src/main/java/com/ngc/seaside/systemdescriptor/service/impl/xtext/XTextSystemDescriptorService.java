/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
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
package com.ngc.seaside.systemdescriptor.service.impl.xtext;

import com.google.common.base.Preconditions;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.inject.Inject;
import com.google.inject.Injector;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.systemdescriptor.SystemDescriptorStandaloneSetup;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.impl.view.AggregatedDataView;
import com.ngc.seaside.systemdescriptor.model.impl.view.AggregatedModelView;
import com.ngc.seaside.systemdescriptor.scenario.api.IScenarioStepHandler;
import com.ngc.seaside.systemdescriptor.service.api.IParsingResult;
import com.ngc.seaside.systemdescriptor.service.api.ISystemDescriptorService;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.module.XTextSystemDescriptorServiceModule;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.parsing.ParsingDelegate;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.validation.ValidationDelegate;
import com.ngc.seaside.systemdescriptor.validation.api.ISystemDescriptorValidator;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Provides an implementation of the {@code ISystemDescriptorService} that uses the XText JellyFish DSL. New instances
 * of this service should always be obtained via injection with Guice.  This class is configured in one of to ways:
 * either for use <i>within</i> Eclipse or for standalone use <i>outside</i> of Eclipse.  If using this service inside
 * eclipse include the {@link XTextSystemDescriptorServiceModule} with the Guice configuration like so:
 * <pre>
 *    {@code
 *      Collection<Module> modules = new ArrayList<>();
 *      modules.add(new XTextSystemDescriptorServiceModule());
 *      Injector injector = Guice.createInjector(modules);
 *      ISystemDescriptorService service = injector.getInstance(ISystemDescriptorService.class);
 *    }
 * </pre>
 * Alternately, use {@link XTextSystemDescriptorServiceModule#forStandaloneUsage()} to use the service outside of
 * Eclipse:
 * <pre>
 *    {@code
 *      Collection<Module> modules = new ArrayList<>();
 *      modules.add(XTextSystemDescriptorServiceModule.forStandaloneUsage());
 *      Injector injector = Guice.createInjector(modules);
 *      ISystemDescriptorService service = injector.getInstance(ISystemDescriptorService.class);
 *    }
 * </pre>
 * Most of the time the service should be injected into an application component instead of being resolved directly:
 * <pre>
 *    {@code
 *      public class MyApplication {
 *        private final ISystemDescriptorService service;
 *        {@literal @}Inject
 *        public MyApplication(ISystemDescriptorService service) {
 *          this.service = service;
 *        }
 *      }
 *    }
 * </pre>
 * <b>An implementation of the {@code ILogService} interface must be bound by an application's module</b> in order for
 * service to be created correctly.
 * <p/>
 * This implementation delegates most concerns to other objects.
 */
public class XTextSystemDescriptorService implements ISystemDescriptorService {

   /**
    * Contains all registered step handlers.  A copy on write list is used for thread safely since we expect to read
    * from this list much more often then we write to it.
    */
   private final Collection<IScenarioStepHandler> stepHandlers = new CopyOnWriteArrayList<>();

   /**
    * An evicting cache that stores aggregated views of data.  This enables the flyweight pattern so we can reuse
    * instances of the views.  The cache will only grow to a fixed size and then entries will be evicted.
    */
   private final Cache<Integer, AggregatedDataView> dataViews = CacheBuilder.newBuilder()
         .maximumSize(25)
         .build();

   /**
    * An evicting cache that stores aggregated views of models.  This enables the flyweight pattern so we can reuse
    * instances of the views.  The cache will only grow to a fixed size and then entries will be evicted.
    */
   private final Cache<Integer, AggregatedModelView> modelViews = CacheBuilder.newBuilder()
         .maximumSize(25)
         .build();

   private final ILogService logService;

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
   public XTextSystemDescriptorService(ILogService logService,
                                       ParsingDelegate parsingDelegate,
                                       ValidationDelegate validationDelegate,
                                       StepsHolder holder) {
      this.logService = Preconditions.checkNotNull(logService, "logService may not be null!");
      this.parsingDelegate = Preconditions.checkNotNull(parsingDelegate, "parsingDelegate may not be null!");
      this.validationDelegate = Preconditions.checkNotNull(validationDelegate, "validationDelegate may not be null!");
      Preconditions.checkNotNull(holder, "holder may not be null!");
      holder.handlers.forEach(this::addScenarioStepHandler);
   }

   /**
    * Creates a new instance of the standalone XText system descriptor service <b>for use outside of Eclipse</b>.  An
    * instance of this service should always be obtained via an {@link Injector} instead of invoking this constructor
    * directly.  See the {@link XTextSystemDescriptorService class javadoc}.
    */
   public XTextSystemDescriptorService(Injector injector,
                                       ILogService logService,
                                       ParsingDelegate parsingDelegate,
                                       ValidationDelegate validationDelegate,
                                       StepsHolder holder) {
      this(logService, parsingDelegate, validationDelegate, holder);
      Preconditions.checkNotNull(injector, "injector may not be null!");
      // Configure XText.
      new SystemDescriptorStandaloneSetup().register(injector);
   }

   @Override
   public IParsingResult parseProject(Path projectDirectory) {
      return parsingDelegate.parseProject(projectDirectory);
   }

   @Override
   public IParsingResult parseProject(String artifactIdentifier) {
      return parsingDelegate.parseProject(artifactIdentifier);
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
   public IData getAggregatedView(IData data) {
      Preconditions.checkNotNull(data, "data may not be null!");
      // Use the identity of the actual object as a key.  This avoids computing the hash code from the state of the
      // object.  If the object's state changes, we can still reuse the original view, so we don't want to use the
      // real hashCode(..) implementation.  identityHashCode gives us a unique value for each instance of IData.
      int key = System.identityHashCode(data);
      try {
         return dataViews.get(key, () -> new AggregatedDataView(data));
      } catch (ExecutionException e) {
         throw new RuntimeException(e.getMessage(), e);
      }
   }

   @Override
   public IModel getAggregatedView(IModel model) {
      Preconditions.checkNotNull(model, "model may not be null!");
      // See note above regarding the use of identityHashCode.
      int key = System.identityHashCode(model);
      try {
         return modelViews.get(key, () -> new AggregatedModelView(model));
      } catch (ExecutionException e) {
         throw new RuntimeException(e.getMessage(), e);
      }
   }

   @Override
   public Collection<IScenarioStepHandler> getScenarioStepHandlers() {
      return Collections.unmodifiableCollection(stepHandlers);
   }

   @Override
   public void addScenarioStepHandler(IScenarioStepHandler handler) {
      Preconditions.checkNotNull(handler, "stepHandlers may not be null!");
      stepHandlers.add(handler);
      logService.debug(XTextSystemDescriptorService.class,
                       "Added step handler %s.",
                       handler);
   }

   @Override
   public boolean removeScenarioStepHandler(IScenarioStepHandler handler) {
      Preconditions.checkNotNull(handler, "handler may not be null!");
      boolean result = stepHandlers.remove(handler);
      if (result) {
         logService.debug(XTextSystemDescriptorService.class,
                          "Removed step handler %s.",
                          handler);
      }
      return result;
   }

   @Override
   public void addValidator(ISystemDescriptorValidator validator) {
      validationDelegate.addValidator(validator);
   }

   @Override
   public boolean removeValidator(ISystemDescriptorValidator validator) {
      return validationDelegate.removeValidator(validator);
   }

   /**
    * A value holder to hold {@link IScenarioStepHandler}s.  This is a workaround to Guice to enable optional
    * constructor parameters.
    */
   public static class StepsHolder {

      @Inject(optional = true)
      Set<IScenarioStepHandler> handlers = Collections.emptySet();
   }

}
