package com.ngc.seaside.jellyfish.cli.gradle.adapter;

import com.google.common.base.Preconditions;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Module;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.service.resource.api.IFileResource;
import com.ngc.blocs.service.resource.api.IReadableFileResource;
import com.ngc.blocs.service.resource.api.IReadableResource;
import com.ngc.blocs.service.resource.api.IResourceService;
import com.ngc.blocs.service.resource.api.IWritableFileResource;
import com.ngc.blocs.service.resource.api.ResourceServiceException;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;

/**
 * An implementation of {@code IResourceService} that loads resources directly from the classpath.
 */
public class ClasspathResourceService implements IResourceService {

   private final ILogService logService;

   @Inject
   public ClasspathResourceService(ILogService logService) {
      this.logService = logService;
   }

   @Override
   public Path getResourceRootPath() {
      throw new UnsupportedOperationException("this implementation reads files directly from the classpath and has no"
                                                    + " single root path!");
   }

   @Override
   public boolean readResource(IReadableResource readableResource) throws ResourceServiceException {
      Preconditions.checkNotNull(readableResource, "readableResource may not be null!");
      try (InputStream is = readableResource.getURL().openStream()) {
         readableResource.read(is);
      } catch (Throwable t) {
         throw createException(t,
                               "Error occurred while reading the resource %s",
                               readableResource.getURL());
      }
      return true;
   }

   @Override
   public boolean readOptionalResource(IReadableResource readableResource) {
      Preconditions.checkNotNull(readableResource, "readableResource may not be null!");
      boolean success = false;
      try (InputStream is = readableResource.getURL().openStream()) {
         readableResource.read(is);
         success = true;
      } catch (Throwable t) {
         logService.warn(getClass(),
                         t,
                         "Unable to read the optional resource %s.",
                         readableResource.getURL());
      }
      return success;
   }

   @Override
   public boolean readFileResource(IReadableFileResource readableResource) throws ResourceServiceException {
      Preconditions.checkNotNull(readableResource, "readableResource may not be null!");
      try (InputStream is = ClasspathResourceService.class
            .getClassLoader()
            .getResourceAsStream(readableResource.getFile())) {
         if (is != null) {
            readableResource.read(is);
         } else {
            throw new FileNotFoundException(readableResource.getFile() + " not found on classpath!");
         }
      } catch (Throwable t) {
         throw createException(t,
                               "Error occurred while reading the file resource %s",
                               readableResource.getFile());
      }
      return true;
   }

   @Override
   public boolean readOptionalFileResource(IReadableFileResource readableResource) {
      Preconditions.checkNotNull(readableResource, "readableResource may not be null!");
      boolean success = false;
      try (InputStream is = ClasspathResourceService.class
            .getClassLoader()
            .getResourceAsStream(readableResource.getFile())) {
         if (is != null) {
            readableResource.read(is);
            success = true;
         }
      } catch (Throwable t) {
         logService.warn(getClass(),
                         t,
                         "Unable to read the optional resource %s from classpath.",
                         readableResource.getFile());
      }
      return success;
   }

   @Override
   public boolean writeFileResource(IWritableFileResource writableResource) throws ResourceServiceException {
      throw new UnsupportedOperationException("this implementation reads files directly from the classpath and does not"
                                                    + " support writing files!");
   }

   @Override
   public boolean exists(IFileResource fileResource) {
      Preconditions.checkNotNull(fileResource, "fileResource may not be null!");
      return ClasspathResourceService.class.getClassLoader().getResource(fileResource.getFile()) != null;
   }

   public static final Module MODULE = new AbstractModule() {

      @Override
      protected void configure() {
         bind(IResourceService.class).to(ClasspathResourceService.class);
      }
   };

   private ResourceServiceException createException(Throwable t, String message, Object... params) {
      String formatted = String.format(message, params);
      logService.error(getClass(), formatted, t);
      return new ResourceServiceException(formatted, t);
   }
}
