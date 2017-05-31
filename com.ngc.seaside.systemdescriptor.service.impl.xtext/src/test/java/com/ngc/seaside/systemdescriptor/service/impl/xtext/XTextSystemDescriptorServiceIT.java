package com.ngc.seaside.systemdescriptor.service.impl.xtext;

import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.traversal.IVisitor;
import com.ngc.seaside.systemdescriptor.model.api.traversal.IVisitorContext;
import com.ngc.seaside.systemdescriptor.service.api.IParsingResult;

import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class XTextSystemDescriptorServiceIT {

   private XTextSystemDescriptorService service;

   @Before
   public void setup() throws Throwable {
      service = new XTextSystemDescriptorService();
   }

   @Test
   public void testDoesParseSingleFile() throws Throwable {
      Path time = Paths.get("build", "resources", "test", "clocks", "datatypes", "Time.sd");
      IParsingResult result = service.parseFiles(Collections.singletonList(time));
      assertTrue("parsing should be successful!",
                 result.isSuccessful());
      assertNotNull("system descriptor not set!",
                    result.getSystemDescriptor());
      result.getSystemDescriptor().traverse(new IVisitor() {
         @Override
         public void visitDataField(IVisitorContext ctx, IDataField field) {
            System.out.println("    " + field.getName());
         }

         @Override
         public void visitData(IVisitorContext ctx, IData data) {
            System.out.println("  " + data.getName());
         }

         @Override
         public void visitPackage(IVisitorContext ctx, IPackage systemDescriptorPackage) {
            System.out.println(systemDescriptorPackage.getName());
         }
      });
   }

//   @Test
//   public void testDoesReturnParsingErrors() throws Throwable {
//   }
//
//   @Test
//   public void testDoesParseMultipleFiles() throws Throwable {
//   }
}
