package com.ngc.seaside.jellyfish.cli.command.createjellyfishgradleproject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import com.ngc.seaside.bootstrap.utilities.file.FileUtilitiesException;
import com.ngc.seaside.command.api.CommandException;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultParameterCollection;
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
    public static final String PROJECT_NAME_PROPERTY = "projectName";
    public static final String GROUP_ID_PROPERTY = "groupId";
    public static final String ARTIFACT_ID_PROPERTY = "artifactId";
    public static final String VERSION_PROPERTY = "version";
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
        DefaultParameterCollection collection = new DefaultParameterCollection();
        collection.addParameters(commandOptions.getParameters().getAllParameters());
        
        if (!collection.containsParameter(PROJECT_NAME_PROPERTY)) {
            String projectName = promptService.prompt(PROJECT_NAME_PROPERTY, "", null);
            collection.addParameter(new DefaultParameter(PROJECT_NAME_PROPERTY).setValue(projectName));
        }
         
        final String projectName = collection.getParameter(PROJECT_NAME_PROPERTY).getValue();
        
        if (!collection.containsParameter(OUTPUT_DIR_PROPERTY)) {
            collection.addParameter(new DefaultParameter(OUTPUT_DIR_PROPERTY).setValue(Paths.get(".").toAbsolutePath().toString()));
        }         
        final Path outputDirectory = Paths.get(collection.getParameter(OUTPUT_DIR_PROPERTY).getValue());
        final Path projectDirectory = outputDirectory.resolve(projectName);
        try {
 		    Files.createDirectories(projectDirectory);
        } catch (IOException e) {
            logService.error(CreateJellyFishGradleProjectCommand.class, e);
            throw new CommandException(e);
        }
        
        if (!collection.containsParameter(GROUP_ID_PROPERTY)) {
            collection.addParameter(new DefaultParameter(GROUP_ID_PROPERTY).setValue(DEFAULT_GROUP_ID));
        }
        
        if (!collection.containsParameter(ARTIFACT_ID_PROPERTY)) {
             String artifact = promptService.prompt(ARTIFACT_ID_PROPERTY, "", null);
             collection.addParameter(new DefaultParameter(ARTIFACT_ID_PROPERTY).setValue(artifact));
        }

        if (!collection.containsParameter(VERSION_PROPERTY)) {
            String version = promptService.prompt(VERSION_PROPERTY, "", null);
            collection.addParameter(new DefaultParameter(VERSION_PROPERTY).setValue(version));
        }
        
        final boolean clean;
        if (collection.containsParameter(CLEAN_PROPERTY)) {
	        String value = collection.getParameter(CLEAN_PROPERTY).getValue();
	        switch (value.toLowerCase()) {
		        case "true":
		            clean = true;
		            break;
		        case "false":
		            clean = false;
		            break;
		        default:
		       	    throw new CommandException("Invalid value for clean: " + value + ". Expected either true or false.");
	        }
        } else {
	        clean = false;
        }
        
        templateService.unpack("JellyFishGradleProject", collection, projectDirectory, clean);
        
        logService.info(CreateJellyFishGradleProjectCommand.class, "%s project successfully created", projectName);
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
                new DefaultParameter(OUTPUT_DIR_PROPERTY)
                        .setDescription("The directory to generate the Gradle project in").setRequired(false),
                new DefaultParameter(PROJECT_NAME_PROPERTY)
                        .setDescription("The name of the Gradle project. This should use hyphens and lower case letters. i.e.  my-project")
                        .setRequired(false),
                new DefaultParameter(GROUP_ID_PROPERTY)
                        .setDescription("The groupId. This is usually similar to com.ngc.myprojectname")
                        .setRequired(false),
                new DefaultParameter(ARTIFACT_ID_PROPERTY)
                        .setDescription("The artifactId, usually the lowercase version of the classname")
                        .setRequired(false),
                new DefaultParameter(VERSION_PROPERTY)
                        .setDescription("The version to use for the Gradle project")
                        .setRequired(false),
                new DefaultParameter(CLEAN_PROPERTY)
                        .setDescription("If true, recursively deletes the Gradle project (if it already exists), before generating the Gradle project again")
                        .setRequired(false));
    }

}
