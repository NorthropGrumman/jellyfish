package com.ngc.seaside.bootstrap.service.impl.templateservice;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.ngc.blocs.service.log.api.ILogService;

// TODO The ignore file is really a list of files for velocity to ignore,
//      which is really a list of non-ascii files in the project. Should the name
//      be adjusted accordingly?
public class TemplateIgnoreComponent {

	private static final String TEMPLATE_IGNORE_FILENAME = "template.ignore";

	private final Path templateFile;
	private final Path templateFolder;
	private final List<Path> ignoreList = new ArrayList<>();
	private final ILogService logService;

	public TemplateIgnoreComponent(Path templateDir, String templateFolder, ILogService logService) {
		this.templateFolder = templateDir.resolve(templateFolder);
		this.templateFile = templateDir.resolve(TEMPLATE_IGNORE_FILENAME);
		this.logService = logService;
	}

	/**
	 * Parses the template's ignore file, collecting the list of files that
	 * velocity should ignore.
	 * 
	 * @return This instance.
	 * @throws IOException
	 */
	public TemplateIgnoreComponent parse() throws IOException {
		ignoreList.clear();

		// Only parse the template file if it exists
		if (Files.exists(templateFile)) {
			for (String eachPathStr : Files.readAllLines(templateFile)) {
				final Path eachPath = templateFolder.resolve(eachPathStr);
				ignoreList.add(eachPath);
			}
		} else {
			logService.trace(getClass(), 
					"Template ignore file (%s) does not exist, no files will be ignored.",
					TEMPLATE_IGNORE_FILENAME);
		}

		return this;
	}

	/**
	 * Checks if the specified path is to be ignored.
	 * 
	 * @param path
	 *            The path to check.
	 * @return true if the path should be ignored, false otherwise.
	 */
	public boolean contains(Path path) {
		return ignoreList.contains(path);
	}
}
