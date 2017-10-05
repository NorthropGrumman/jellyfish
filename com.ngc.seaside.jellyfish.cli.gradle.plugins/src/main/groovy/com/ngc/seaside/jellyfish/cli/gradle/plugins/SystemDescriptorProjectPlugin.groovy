package com.ngc.seaside.jellyfish.cli.gradle.plugins

import com.ngc.seaside.gradle.plugins.util.GradleUtil
import com.ngc.seaside.jellyfish.cli.gradle.tasks.JellyFishCliCommandTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.bundling.Zip

/**
 * A plugin that can be applied to a System Descriptor project.  When a build is executed, the System Descriptor project
 * will be validated with JellyFish and then a ZIP of the project will be created.  The ZIP can then be installed to
 * a local Maven repository or uploaded to a remote Maven repository.
 */
class SystemDescriptorProjectPlugin implements Plugin<Project> {

    @Override
    void apply(Project p) {
        p.configure(p) {

            // Make sure that all required properties are set.
            GradleUtil.requireProperties(p.properties,
                                         'nexusConsolidated',
                                         'nexusReleases',
                                         'nexusSnapshots',
                                         'nexusUsername',
                                         'nexusPassword')

            // This plugin requires the maven plugin to enable uploads to Nexus.
            plugins.apply 'maven'

            // Validate the model is correct.
            task('validateSd', type: JellyFishCliCommandTask) {
                command = 'validate'
            }

            // Copy all files to build for more processing.
            task('copyDistributionFiles', type: Copy, dependsOn: [validateSd]) {
                from 'src'
                into { "${project.distsDir}/stage/src" }
            }

            // Zip up the files.
            task('sdDistribution', type: Zip, dependsOn: [copyDistributionFiles]) {
                from { "${project.distsDir}/stage" }
            }

            afterEvaluate {
                // Configure the ZIP that contains the SD project to be releasable to Nexus.
                uploadArchives {
                    repositories {
                        mavenDeployer {
                            // Use the main repo for full releases.
                            repository(url: nexusReleases) {
                                // Make sure that nexusUsername and nexusPassword are in your
                                // ${gradle.user.home}/gradle.properties file.
                                authentication(userName: nexusUsername, password: nexusPassword)
                            }
                            // If the version has SNAPSHOT in the name, use the snapshot repo.
                            snapshotRepository(url: nexusSnapshots) {
                                authentication(userName: nexusUsername, password: nexusPassword)
                            }
                        }
                    }
                }

                // Configure the artifacts so that the zip gets pushed to Nexus.
                artifacts {
                    archives sdDistribution
                }

                // Configure the name of the distribution.  We do this here so that properties are evaluated correctly.
                tasks.getByName(
                      'sdDistribution').archiveName = "${project.group}.${project.name}-${project.version}.zip"

                // Set the default tasks.
                defaultTasks = ['build']
            }
        }
    }
}
