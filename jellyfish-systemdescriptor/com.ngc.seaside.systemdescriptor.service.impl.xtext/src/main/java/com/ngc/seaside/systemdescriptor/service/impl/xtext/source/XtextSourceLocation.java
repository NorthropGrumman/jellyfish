/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
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
package com.ngc.seaside.systemdescriptor.service.impl.xtext.source;

import com.ngc.seaside.systemdescriptor.service.api.ParsingException;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.location.IDetailedSourceLocation;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.impl.InternalNodeModelUtils;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.util.ITextRegion;
import org.eclipse.xtext.util.LineAndColumn;
import org.eclipse.xtext.util.TextRegion;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Collections;

public class XtextSourceLocation implements IDetailedSourceLocation {

   private ITextRegion region;
   private Path path;
   private final EObject object;
   private final int line;
   private final int column;
   
   /**
    * @param object xtext object
    * @param region text region within object
    */
   public XtextSourceLocation(EObject object, ITextRegion region) {
      this.object = object;
      this.region = region;
      this.path = getPathFromUri(EcoreUtil2.getNormalizedURI(object));
      INode node = NodeModelUtils.findActualNodeFor(object);
      LineAndColumn lineAndColumn = WrapperInternalNodeModelUtils.getLineAndColumn(node, region.getOffset());
      this.line = lineAndColumn.getLine();
      this.column = lineAndColumn.getColumn();
   }
   
   @Override
   public Path getPath() {
      return path;
   }

   @Override
   public int getLineNumber() {
      return line;
   }

   @Override
   public int getColumn() {
      return column;
   }

   @Override
   public int getLength() {
      return region.getLength();
   }

   @Override
   public int getOffset() {
      return region.getOffset();
   }

   @Override
   public IDetailedSourceLocation getSubLocation(int offset, int length) {
      ITextRegion subRegion = new TextRegion(region.getOffset() + offset, length);
      return new XtextSourceLocation(object, subRegion);
   }

   private static class WrapperInternalNodeModelUtils extends InternalNodeModelUtils {
      public static LineAndColumn getLineAndColumn(INode node, int offset) {
         return InternalNodeModelUtils.getLineAndColumn(node, offset);
      }
   }

   private static Path getPathFromUri(URI uri) {
      Path path = null;
      if (uri != null) {
         String fileString = uri.toFileString();
         if (fileString != null) {
            path = new File(fileString).toPath();
         } else {
            String devicePath = uri.devicePath();
            String filePath;
            int index = devicePath.toLowerCase().indexOf(".zip!");
            if (index >= 0) {
               index += 4;
               filePath = devicePath.substring(index + 1);
               devicePath = "jar:" + devicePath.substring(0, index);
            } else {
               throw new ParsingException("Unable to get path for " + uri);
            }
            java.net.URI i;
            try {
               i = new java.net.URI(devicePath);
            } catch (URISyntaxException e) {
               throw new ParsingException(e);
            }
            FileSystem fileSystem;
            try {
               fileSystem = FileSystems.getFileSystem(i);
            } catch (FileSystemNotFoundException e) {
               try {
                  fileSystem = FileSystems.newFileSystem(i, Collections.emptyMap());
               } catch (IOException e2) {
                  throw new ParsingException(e);
               }
            }
            path = fileSystem.getPath(filePath);
         }
      }
      return path;
   }

}
