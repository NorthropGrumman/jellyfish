package com.ngc.seaside.systemdescriptor.ui.quickfix.imports;

import com.google.common.collect.Iterables;
import com.google.inject.Inject;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ListDialog;
import org.eclipse.ui.dialogs.SearchPattern;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.XtextEditor;
import org.eclipse.xtext.ui.editor.utils.EditorUtils;
import org.eclipse.xtext.ui.label.AbstractLabelProvider;
import org.eclipse.xtext.util.ITextRegion;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;

/**
 * A dialog for selecting imports.
 */

public class DialogSelector<T> implements IReferenceSelector<T> {

   @Inject
   private ILabelProvider labelProvider;

   /**
    * Display a dialog for the user to select between the given choices and returns the choice.
    * @param choices collection of choices
    * @param usage region in the document that is under consideration, can be null
    * @param resource resource
    * @return the choice or {@link Optional#empty()} if no choice was made
    */
   public OptionalInt select(List<? extends T> choices, XtextReferenceContext context) {
      XtextEditor editor = EditorUtils.getActiveXtextEditor();
      if (editor == null || choices.isEmpty()) {
         return OptionalInt.empty();
      }
      ITextRegion usage = context.getUsage();
      XtextResource resource = context.getResource();
      ISelection previousSelection = showSelectionInEditor(editor, usage, resource);
      Shell shell = Display.getDefault().getActiveShell();
      ChoiceDialog dialog = new ChoiceDialog(shell, new LabelProvider(labelProvider), new ContentProvider());
      dialog.setInput(choices);
      dialog.setInitialSelections(new Object[] { choices.iterator().next() });
      int result = dialog.open();
      if (previousSelection != null) {
         editor.getSelectionProvider().setSelection(previousSelection);
      }
      if (result == Window.OK) {
         Object selection = dialog.getResult()[0];
         int index = choices.indexOf(selection);
         if (index < 0) {
            return OptionalInt.empty();
         } else {
            return OptionalInt.of(index);
         }
      } else {
         return OptionalInt.empty();
      }
   }

   private static ISelection showSelectionInEditor(XtextEditor editor, ITextRegion usage, XtextResource resource) {
      boolean isRevealUsages = editor.getDocument()
                                     .readOnly(state -> Objects.equals(state.getURI(), resource.getURI()));
      ISelection originalSelection = null;
      if (isRevealUsages && usage != null) {
         originalSelection = editor.getSelectionProvider().getSelection();
         editor.selectAndReveal(usage.getOffset(), usage.getLength());
      }
      return originalSelection;
   }

   private static class ContentProvider implements IStructuredContentProvider {
      @Override
      public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {}

      @Override
      public void dispose() {}

      @Override
      public Object[] getElements(Object inputElement) {
         return Iterables.toArray((Iterable<?>) inputElement, Object.class);
      }
   }

   private static final class LabelProvider extends AbstractLabelProvider {

      public LabelProvider(ILabelProvider delegate) {
         super(delegate);
      }

      @Override
      protected String doGetText(Object element) {
         return element.toString();
      }
   }

   private static final class Filter extends ViewerFilter {

      private final SearchPattern searchPattern;

      public Filter(String pattern) {
         searchPattern = new SearchPattern();
         searchPattern.setPattern(pattern);
      }

      @Override
      public boolean select(Viewer viewer, Object parentElement, Object element) {
         return searchPattern.matches(element.toString());
      }
   }

   private static final class ChoiceDialog extends ListDialog {

      private Text searchBar;

      public ChoiceDialog(Shell parent, ILabelProvider labelProvider, IStructuredContentProvider contentProvider) {
         super(parent);
         setTitle("Organize Imports");
         setMessage("Choose data/model to import:");
         setAddCancelButton(true);
         setLabelProvider(labelProvider);
         setContentProvider(contentProvider);
      }

      @Override
      protected Label createMessageArea(Composite composite) {
         Label label = super.createMessageArea(composite);
         searchBar = new Text(composite, SWT.SEARCH | SWT.ICON_CANCEL | SWT.BORDER);
         searchBar.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));
         searchBar.addModifyListener(event -> applyFilter());
         searchBar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
               if (e.keyCode == SWT.ARROW_DOWN) {
                  TableViewer tableViewer = getTableViewer();
                  tableViewer.getTable().setFocus();
                  if (tableViewer.getSelection().isEmpty()) {
                     Object firstElement = tableViewer.getElementAt(0);
                     if (firstElement != null) {
                        tableViewer.setSelection(new StructuredSelection(firstElement));
                     }
                  }
               }
            }
         });
         return label;
      }

      private void applyFilter() {
         String searchPattern = searchBar.getText();
         if (searchPattern != null) {
            getTableViewer().resetFilters();
            getTableViewer().addFilter(new Filter(searchPattern));
         }
      }
   }
}
