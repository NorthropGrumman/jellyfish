package com.ngc.seaside.systemdescriptor.ui.wizard.project.page;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
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
   private static final String DEFAULT_CLI_VERSION = "1.9.0";

   private final List<Function<String, String>> VALIDATORS = Arrays.asList(
      name -> name != null && name.matches("[a-zA-Z_][\\w]*([-\\.][a-zA-Z_][\\w]*)*") ? null : "Invalid project name",
      group -> group != null && group.matches("[a-zA-Z_][\\w]*([-\\.][a-zA-Z_][\\w]*)*") ? null : "Invalid group ID",
      version -> version != null && version.matches("\\d+(\\.\\d+)*(-SNAPSHOT)?") ? null : "Invalid version",
      cliVersion -> cliVersion != null && cliVersion.matches("\\d+(\\.\\d+)*(-SNAPSHOT)?") ? null : "Invalid version");
   private final Supplier<String> DEFAULT_PROJECT_NAME;
   private Text projectNameField;
   private Text groupField;
   private Text versionField;
   private Text cliField;
   private Composite container;
   private boolean hasBeenVisible = false;

   public ProjectInfoPage(Supplier<String> defaultProjectName) {
      super("Project Info Page");
      setTitle("Project Info Page");
      setDescription("Enter gradle project information");
      this.DEFAULT_PROJECT_NAME = defaultProjectName;
   }

   @Override
   public void setVisible(boolean visible) {
      if (!hasBeenVisible && visible) {
         hasBeenVisible = true;
         projectNameField.setText(DEFAULT_PROJECT_NAME.get());
         setPageComplete(finished());
      }
      super.setVisible(visible);
   }

   public boolean finished() {
      return VALIDATORS.get(0).apply(projectNameField.getText()) == null
         && VALIDATORS.get(1).apply(groupField.getText()) == null
         && VALIDATORS.get(2).apply(versionField.getText()) == null
         && VALIDATORS.get(3).apply(cliField.getText()) == null;
   }

   @Override
   public void createControl(Composite parent) {
      container = new Composite(parent, SWT.NONE);
      container.setLayout(new GridLayout(2, false));
      final int TEXT_STYLE = SWT.DRAW_TAB | SWT.BORDER | SWT.FILL;

      createField(() -> projectNameField = new Text(container, TEXT_STYLE),
         "Project Name",
         DEFAULT_PROJECT_NAME.get(),
         VALIDATORS.get(0));
      createField(() -> groupField = new Text(container, TEXT_STYLE),
         "Group ID",
         DEFAULT_GROUP,
         VALIDATORS.get(1));
      createField(() -> versionField = new Text(container, TEXT_STYLE),
         "Version",
         DEFAULT_VERSION,
         VALIDATORS.get(2));
      createField(() -> cliField = new Text(container, TEXT_STYLE),
         "Jellyfish Gradle Plugins Version",
         DEFAULT_CLI_VERSION,
         VALIDATORS.get(3));

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

   /**
    * @return the cli plugin version
    */
   public String getCliPluginVersion() {
      return cliField.getText();
   }

   private void updateStatus(String message) {
      setErrorMessage(message);
      setPageComplete(isPageComplete());
   }

   private void createField(Supplier<Text> textSupplier, String name, String defaultValue,
            Function<String, String> validator) {
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
   }

   /**
    * Adds an empty Label to the next cell in the container which uses a Grid layout.
    * This is just to take up a cell.
    * 
    * @param container
    *           The container to add the empty label to.
    */
   private void takeUpCellInGrid(Composite container) {
      new Label(container, SWT.NONE);
   }

}
