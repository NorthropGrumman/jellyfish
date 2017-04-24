package com.ngc.seaside.starfish.bootstrap;

import org.apache.velocity.app.VelocityEngine;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

public class TemplateGenerator implements FileVisitor<Path>
{

   private VelocityEngine engine = new VelocityEngine();
   private Map<String, String> parametersAndValues;

   public TemplateGenerator(Map<String, String> parametersAndValues, boolean clean)
   {
      this.parametersAndValues = new HashMap<>(parametersAndValues);
      for (Map.Entry<String, String> entry : parametersAndValues.entrySet()) {
         engine.setProperty(entry.getKey(), entry.getValue());
      }
   }

   @Override
   public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes basicFileAttributes) throws IOException
   {
      System.out.println("PRE:" + path);
      return FileVisitResult.CONTINUE;
   }

   @Override
   public FileVisitResult visitFile(Path path, BasicFileAttributes basicFileAttributes) throws IOException
   {
      System.out.println("VISIT:" + path);
      return FileVisitResult.CONTINUE;
   }

   @Override
   public FileVisitResult visitFileFailed(Path path, IOException e) throws IOException
   {
      System.out.println("FAILED:" + path);
      return FileVisitResult.CONTINUE;
   }

   @Override
   public FileVisitResult postVisitDirectory(Path path, IOException e) throws IOException
   {
      System.out.println("POST:" + path);
      return FileVisitResult.CONTINUE;
   }
}
