package com.ngc.seaside.jellyfish.cli.gradle.plugins

import com.ngc.seaside.jellyfish.api.CommonParameters
import com.ngc.seaside.jellyfish.cli.gradle.JellyFishProjectGenerator
import com.ngc.seaside.jellyfish.utilities.command.JellyfishCommandPhase
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.GradleException

class SystemDescriptorDerivedProjectPlugin implements Plugin<Project> {

    @Override
    void apply(Project p) {
        p.configure(p) {

            // Find jellyfish-cli dependency from classpath
            def cliDependency
            def proj = p
            while (true) {
                cliDependency = proj.buildscript
                                    .configurations
                                    .classpath
                                    .dependencies
                                    .find { it.group == 'com.ngc.seaside' && it.name == 'jellyfish.cli.gradle.plugins'}
                if (cliDependency != null) {
                    break
                }
                proj = proj.parent
                if (proj == null) {
                    throw new GradleException('com.ngc.seaside.jellyfish.system-descriptor-derived plugin requires com.ngc.seaside.jellyfish.cli.gradle.plugins on the classpath')
                }
            }

            // Generate md5 for jellyfish-cli dependency and its transitive dependencies
            def md5 = generateMD5(proj.buildscript.configurations.classpath.files(cliDependency))
            def md5File = file("${p.projectDir}/.cli-dependencies.md5")

            // Run clean-gen if anything in jellyfish-cli or its dependencies have changed
            if (!md5File.exists() || md5File.text != md5 || gradle.startParameter.taskNames.contains('clean-gen')) {
                logger.info(":${p.name}:clean-gen")
                delete 'src'
                delete 'build.generated.gradle'
                md5File.text = md5
            }

            // test for file changes
            if (!file("${project.projectDir}/build.generated.gradle").exists()) {
                logger.info(":${p.name}:generate")
                def sdProject = parent.systemDescriptor.project
                // Note when running Jellyfish from Gradle, we are always running in the deferred phase.
                new JellyFishProjectGenerator(logger)
                      .setCommand(command)
                      .setArguments([(CommonParameters.MODEL.name)                  : "${parent.systemDescriptor.model}",
                                     (CommonParameters.DEPLOYMENT_MODEL.name)       : "${parent.systemDescriptor.deploymentModel}",
                                     (CommonParameters.GROUP_ARTIFACT_VERSION.name) : "${sdProject.group}:${sdProject.name}:${sdProject.version}",
                                     (CommonParameters.OUTPUT_DIRECTORY.name)       : "${project.rootDir.absolutePath}",
                                     (CommonParameters.UPDATE_GRADLE_SETTING.name)  : 'false',
                                     (CommonParameters.PHASE.name) : JellyfishCommandPhase.DEFERRED.toString()])
                      .generate()
            }

            apply from: "${project.projectDir}/build.generated.gradle"
        }
    }

    private def generateMD5(Iterable<File> files) {
        def digest = java.security.MessageDigest.getInstance('MD5')
        files.sort { it.name }.each {
            digest.update(it.name.bytes)
            it.eachByte( 4096 ) { buffer, length ->
                digest.update(buffer, 0, length)
            }
        }
        return digest.digest().encodeHex().toString()
    }
}
