package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.utils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DefaultImportManager implements IImportManager {

   private static final Pattern PACKAGE_PATTERN =
            Pattern.compile("[\\w&&\\D]\\w*(?:\\.[\\w&&\\D]\\w*)*");
   private static final Pattern IMPORT_PATTERN =
            Pattern.compile("[\\w&&\\D]\\w*(?:\\.[\\w&&\\D]\\w*)*(?:\\.\\*)?");

   private final TreeSet<String> imports = new TreeSet<>(PACKAGE_COMPARATOR);
   private final TreeSet<String> staticImports = new TreeSet<>(PACKAGE_COMPARATOR);
   private String pkg;

   @Override
   public void add(String imp) {
      Matcher m = IMPORT_PATTERN.matcher(imp);
      if (m.matches()) {
         imports.add(imp);
      } else {
         throw new IllegalArgumentException("Invalid import: <" + imp  + ">");
      }
   }

   @Override
   public void addStatic(String imp) {
      Matcher m = IMPORT_PATTERN.matcher(imp);
      if (m.matches()) {
         staticImports.add(imp);
      } else {
         throw new IllegalArgumentException("Invalid static import: <" + imp + ">");
      }
   }

   @Override
   public void clear() {
      imports.clear();
      staticImports.clear();
   }

   @Override
   public String generateJava() {
      Stream<String> imports = this.imports.stream().filter(imp -> !samePackage(imp, pkg));
      return Stream.concat(imports, staticImports.stream())
               .map(imp -> "import " + imp + ";")
               .collect(Collectors.joining("\n"));
   }

   @Override
   public String getType(String fullyQualifiedName) {
      String pkg = fullyQualifiedName.substring(0, fullyQualifiedName.lastIndexOf('.'));
      String type = fullyQualifiedName.substring(fullyQualifiedName.lastIndexOf('.') + 1);
      if (imports.contains(fullyQualifiedName) || imports.contains(pkg + ".*") || this.pkg.equals(pkg)) {
         return type;
      }
      return fullyQualifiedName;
   }

   /**
    * Compares two imports. Third party libraries come first, then {@code org.*} libraries, then {@code java.*}
    * libraries.
    */
   private static Comparator<String> PACKAGE_COMPARATOR = (s1, s2) -> {
      List<String> l1 = Arrays.asList(s1.split("\\."));
      List<String> l2 = Arrays.asList(s2.split("\\."));
      String first1 = l1.get(0);
      String first2 = l2.get(0);
      int comp1 = first1.equals("java") ? 2 : (first1.equals("org") ? 1 : 0);
      int comp2 = first2.equals("java") ? 2 : (first2.equals("org") ? 1 : 0);
      if (comp1 != comp2) {
         return Integer.compare(comp1, comp2);
      }
      int size = Math.min(l1.size(), l2.size());
      for (int n = 0; n < size; n++) {
         int comp = l1.get(n).compareTo(l2.get(n));
         if (comp != 0) {
            return comp;
         }
      }
      return Integer.compare(l1.size(), l2.size());
   };

   /**
    * Returns {@code true} if the given import has the same package as what's given.
    * 
    * @param imp import
    * @param pkg package
    * @return {@code true} if the given import has the same package as what's given
    */
   private static boolean samePackage(String imp, String pkg) {
      List<String> importParts = Arrays.asList(imp.split("\\."));
      List<String> packageParts = Arrays.asList(pkg.split("\\."));

      if (importParts.size() == packageParts.size() + 1) {
         return importParts.subList(0, packageParts.size()).equals(packageParts);
      }

      return false;
   }

   @Override
   public void setPackage(String pkg) {
      Matcher m = PACKAGE_PATTERN.matcher(pkg);
      if (!m.matches()) {
         throw new IllegalArgumentException("Invalid package: " + pkg);
      }
      this.pkg = pkg;
   }

}
