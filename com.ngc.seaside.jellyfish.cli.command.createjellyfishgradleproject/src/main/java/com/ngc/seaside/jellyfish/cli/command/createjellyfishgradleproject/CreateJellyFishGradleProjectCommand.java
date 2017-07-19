package com.ngc.seaside.jellyfish.cli.command.createjellyfishgradleproject;

import java.util.regex.Pattern;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.bootstrap.service.promptuser.api.IPromptUserService;
import com.ngc.seaside.bootstrap.service.template.api.ITemplateService;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultUsage;
import com.ngc.seaside.command.api.IUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.cli.command.createjellyfishgradleproject.CreateJellyFishGradleProjectCommand;

/**
 * 
 */
@Component(service = IJellyFishCommand.class)
public class CreateJellyFishGradleProjectCommand implements IJellyFishCommand {
	private static final String NAME = "create-jellyfish-gradle-project";
	private static final IUsage USAGE = createUsage();

	private static final Pattern JAVA_IDENTIFIER = Pattern.compile("[a-zA-Z$_][a-zA-Z$_0-9]*");
	private static final Pattern JAVA_QUALIFIED_IDENTIFIER = Pattern.compile("[a-zA-Z$_][a-zA-Z$_0-9]*(?:\\.[a-zA-Z$_][a-zA-Z$_0-9]*)*");

	public static final String OUTPUT_DIR_PROPERTY = "outputDirectory";
	public static final String GROUP_ID_PROPERTY = "groupId";
	public static final String ARTIFACT_ID_PROPERTY = "artifactId";
	public static final String PACKAGE_PROPERTY = "package";
	public static final String PROJECT_NAME_PROPERTY = "projectname";
	public static final String CLEAN_PROPERTY = "clean";

	static final String DEFAULT_GROUP_ID = "com.ngc.seaside";
	static final String DEFAULT_ARTIFACT_ID_FORMAT = "jellyfish.cli.command.%s";

	private ILogService logService;
	private IPromptUserService promptService;
	private ITemplateService templateService;

	@Activate
	public void activate() {
		logService.trace(getClass(), "Activated");
	}

	@Deactivate
	public void deactivate() {
		logService.trace(getClass(), "Deactivated");
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public IUsage getUsage() {
		return USAGE;
	}

	@Override
	public void run(IJellyFishCommandOptions commandOptions) {
		// DefaultParameterCollection collection = new
		// DefaultParameterCollection();
		// collection.addParameters(commandOptions.getParameters().getAllParameters());
		//
		// if (!collection.containsParameter(COMMAND_NAME_PROPERTY)) {
		// String commandName = promptService.prompt(COMMAND_NAME_PROPERTY, "",
		// null);
		// collection.addParameter(new
		// DefaultParameter(COMMAND_NAME_PROPERTY).setValue(commandName));
		// }
		// final String commandName =
		// collection.getParameter(COMMAND_NAME_PROPERTY).getValue();
		//
		// if (!collection.containsParameter(OUTPUT_DIR_PROPERTY)) {
		// collection.addParameter(
		// new
		// DefaultParameter(OUTPUT_DIR_PROPERTY).setValue(Paths.get(".").toAbsolutePath().toString()));
		// }
		// final Path outputDirectory =
		// Paths.get(collection.getParameter(OUTPUT_DIR_PROPERTY).getValue());
		// try {
		// Files.createDirectories(outputDirectory);
		// } catch (IOException e) {
		// logService.error(CreateJellyFishGradleProjectCommand.class, e);
		// throw new CommandException(e);
		// }
		//
		// if
		// (Files.isDirectory(outputDirectory.resolve("com.ngc.seaside.jellyfish.api")))
		// {
		// collection.addParameter(new
		// DefaultParameter("withApi").setValue("true"));
		// } else {
		// collection.addParameter(new
		// DefaultParameter("withApi").setValue("false"));
		// }
		//
		// if (!collection.containsParameter(GROUP_ID_PROPERTY)) {
		// collection.addParameter(new
		// DefaultParameter(GROUP_ID_PROPERTY).setValue(DEFAULT_GROUP_ID));
		// }
		// final String group =
		// collection.getParameter(GROUP_ID_PROPERTY).getValue();
		//
		// if (!collection.containsParameter(ARTIFACT_ID_PROPERTY)) {
		// String artifact =
		// collection.getParameter(COMMAND_NAME_PROPERTY).getValue().replace("-",
		// "").toLowerCase();
		// collection.addParameter(
		// new
		// DefaultParameter(ARTIFACT_ID_PROPERTY).setValue(String.format(DEFAULT_ARTIFACT_ID_FORMAT,
		// artifact)));
		// }
		// final String artifact =
		// collection.getParameter(ARTIFACT_ID_PROPERTY).getValue();
		//
		// if (!collection.containsParameter(PACKAGE_PROPERTY)) {
		// collection.addParameter(new
		// DefaultParameter(PACKAGE_PROPERTY).setValue(group + '.' + artifact));
		// }
		// String pkg = collection.getParameter(PACKAGE_PROPERTY).getValue();
		// if (!JAVA_QUALIFIED_IDENTIFIER.matcher(pkg).matches()) {
		// throw new CommandException("Invalid package name: " + pkg);
		// }
		//
		// if (!collection.containsParameter(CLASSNAME_PROPERTY)) {
		// String classname = WordUtils.capitalize(commandName,
		// '-').replaceAll("[^a-zA-Z0-9_$]", "") + "Command";
		// collection.addParameter(new
		// DefaultParameter(CLASSNAME_PROPERTY).setValue(classname));
		// }
		// final String classname =
		// collection.getParameter(CLASSNAME_PROPERTY).getValue();
		// if (!JAVA_IDENTIFIER.matcher(classname).matches()) {
		// throw new CommandException("Invalid classname for command " +
		// commandName + ": " + classname);
		// }
		//
		// try {
		// GradleSettingsUtilities.addProject(collection);
		// } catch (FileUtilitiesException e) {
		// logService.warn(getClass(), e, "Unable to add the new project to
		// settings.gradle.");
		// throw new CommandException(e);
		// }
		//
		// final boolean clean;
		// if (collection.containsParameter(CLEAN_PROPERTY)) {
		// String value = collection.getParameter(CLEAN_PROPERTY).getValue();
		// switch (value.toLowerCase()) {
		// case "true":
		// clean = true;
		// break;
		// case "false":
		// clean = false;
		// break;
		// default:
		// throw new CommandException("Invalid value for clean: " + value + ".
		// Expected either true or false.");
		// }
		// } else {
		// clean = false;
		// }
		//
		// templateService.unpack("JellyFishCommand", collection,
		// outputDirectory, clean);
		logService.info(CreateJellyFishGradleProjectCommand.class, "%s project successfully created", "some project name");
	}

	/**
	 * Sets log service.
	 *
	 * @param ref
	 *            the ref
	 */
	@Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "removeLogService")
	public void setLogService(ILogService ref) {
		this.logService = ref;
	}

	/**
	 * Remove log service.
	 */
	public void removeLogService(ILogService ref) {
		setLogService(null);
	}

	/**
	 * Sets prompt user service.
	 *
	 * @param ref
	 *            the ref
	 */
	@Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "removePromptService")
	public void setPromptService(IPromptUserService ref) {
		this.promptService = ref;
	}

	/**
	 * Remove prompt user service.
	 */
	public void removePromptService(IPromptUserService ref) {
		setPromptService(null);
	}

	/**
	 * Sets template service.
	 *
	 * @param ref
	 *            the ref
	 */
	@Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "removeTemplateService")
	public void setTemplateService(ITemplateService ref) {
		this.templateService = ref;
	}

	/**
	 * Remove template service.
	 */
	public void removeTemplateService(ITemplateService ref) {
		setTemplateService(null);
	}

	/**
	 * Create the usage for this command.
	 *
	 * @return the usage.
	 */
	private static IUsage createUsage() {
		return new DefaultUsage(
				"Creates a new JellyFish Gradle project. This requires that a settings.gradle file be present in the output directory. It also requires that the jellyfishAPIVersion be set in the parent build.gradle.",
				new DefaultParameter(PROJECT_NAME_PROPERTY)
						.setDescription("The name of the Gradle project. This should use hyphens and lower case letters. i.e.  my-project")
						.setRequired(false),
				new DefaultParameter(GROUP_ID_PROPERTY)
						.setDescription("The groupId. This is usually similar to com.ngc.myprojectname")
						.setRequired(false),
				new DefaultParameter(ARTIFACT_ID_PROPERTY)
						.setDescription("The artifactId, usually the lowercase version of the classname")
						.setRequired(false),
				new DefaultParameter(OUTPUT_DIR_PROPERTY)
						.setDescription("The directory to generate the Gradle project in").setRequired(false),
				new DefaultParameter(CLEAN_PROPERTY)
						.setDescription("If true, recursively deletes the Gradle project (if it already exists), before generating the Gradle project again")
						.setRequired(false));
	}

}
