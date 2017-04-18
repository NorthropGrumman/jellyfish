package com.ngc.seaside.starfish.bootstrap;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;

public class TemplateGenerator implements FileVisitor<Path>
{

   public TemplateGenerator(Map<String, String> parametersAndValues)
   {}

   @Override
   public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes basicFileAttributes) throws IOException
   {
      return FileVisitResult.CONTINUE;
   }

   @Override
   public FileVisitResult visitFile(Path path, BasicFileAttributes basicFileAttributes) throws IOException
   {
      return FileVisitResult.CONTINUE;
   }

   @Override
   public FileVisitResult visitFileFailed(Path path, IOException e) throws IOException
   {
      return FileVisitResult.CONTINUE;
   }

   @Override
   public FileVisitResult postVisitDirectory(Path path, IOException e) throws IOException
   {
      return FileVisitResult.CONTINUE;
   }
}
