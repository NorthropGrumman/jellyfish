package com.ngc.seaside.systemdescriptor.ui.quickfix.imports;

import com.google.inject.Inject;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.xtext.ui.editor.XtextEditor;
import org.eclipse.xtext.ui.editor.utils.EditorUtils;

/**
 * Organizes the import section of a system descriptor file
 */
public class OrganizeImportsHandler extends AbstractHandler {

   @Inject
   private IImportsOrganizer importsOrganizer;

   @Override
   public Object execute(ExecutionEvent event) throws ExecutionException {
      XtextEditor editor = EditorUtils.getActiveXtextEditor(event);
      if (editor != null) {
         importsOrganizer.organizeImports(editor.getDocument());
      }
      return null;
   }

}
