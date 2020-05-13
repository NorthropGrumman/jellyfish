/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.ngc.seaside.systemdescriptor.ui.wizard.project.page;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * This class enables the user to enter the default values for the gradle project.
 */
public class ProjectInfoPage extends WizardPage {

   private static final String DEFAULT_GROUP = "com.ngc";
   private static final String DEFAULT_VERSION = "1.0.0-SNAPSHOT";

   private static final List<Function<String, String>> VALIDATORS = Arrays.asList(
         name -> name != null && name.matches("[a-zA-Z_][\\w]*([-\\.][a-zA-Z_][\\w]*)*") ? null
                                                                                         : "Invalid project name",
         group -> group != null && group.matches("[a-zA-Z_][\\w]*([-\\.][a-zA-Z_][\\w]*)*") ? null : "Invalid group ID",
         version -> version != null && version.matches("\\d+(\\.\\d+)*(-SNAPSHOT)?") ? null : "Invalid version");
   private final Supplier<String> defaultProjectName;
   private Text projectNameField;
   private Text groupField;
   private Text versionField;
   private Composite container;
   private boolean hasBeenVisible = false;

   /**
    * Creates a new project info page.
    */
   public ProjectInfoPage(Supplier<String> defaultProjectName) {
      super("Project Info Page");
      setTitle("Project Info Page");
      setDescription("Enter gradle project information");
      this.defaultProjectName = defaultProjectName;
   }

   @Override
   public void setVisible(boolean visible) {
      if (!hasBeenVisible && visible) {
         hasBeenVisible = true;
         projectNameField.setText(defaultProjectName.get());
         setPageComplete(finished());
      }
      super.setVisible(visible);
   }

   /**
    * I don't know what this does.  Or why its public.
    */
   public boolean finished() {
      return VALIDATORS.get(0).apply(projectNameField.getText()) == null
            && VALIDATORS.get(1).apply(groupField.getText()) == null
            && VALIDATORS.get(2).apply(versionField.getText()) == null;
   }

   @Override
   public void createControl(Composite parent) {
      container = new Composite(parent, SWT.NONE);
      container.setLayout(new GridLayout(2, false));
      final int TEXT_STYLE = SWT.DRAW_TAB | SWT.BORDER | SWT.FILL;

      createField(() -> projectNameField = new Text(container, TEXT_STYLE),
                  "Project Name",
                  defaultProjectName.get(),
                  VALIDATORS.get(0));
      createField(() -> groupField = new Text(container, TEXT_STYLE),
                  "Group ID",
                  DEFAULT_GROUP,
                  VALIDATORS.get(1));
      createField(() -> versionField = new Text(container, TEXT_STYLE),
                  "Version",
                  DEFAULT_VERSION,
                  VALIDATORS.get(2));

      setControl(container);
      setPageComplete(finished());
   }

   /**
    * @return the project name
    */
   public String getProjectName() {
      return projectNameField.getText();
   }

   /**
    * @return the project's group id
    */
   public String getGroupId() {
      return groupField.getText();
   }

   /**
    * @return the project's version
    */
   public String getVersion() {
      return versionField.getText();
   }

   private void updateStatus(String message) {
      setErrorMessage(message);
      setPageComplete(isPageComplete());
   }

   private void createField(Supplier<Text> textSupplier, String name, String defaultValue,
                            Function<String, String> validator) {
      GridData gd = new GridData(GridData.FILL_HORIZONTAL);
      Label label = new Label(container, SWT.NONE);
      label.setText(name + ":");

      Text text = textSupplier.get();
      text.setText(defaultValue);
      text.setEnabled(true);
      text.addKeyListener(new KeyAdapter() {
         public void keyReleased(KeyEvent event) {
            String message = validator.apply(text.getText());
            updateStatus(message);
         }
      });
      text.setLayoutData(gd);
   }

}
