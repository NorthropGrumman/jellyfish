package com.ngc.seaside.jellyfish.cli.gradle.plugins

import com.ngc.seaside.jellyfish.api.CommonParameters
import com.ngc.seaside.jellyfish.cli.gradle.internal.GradleUtil
import com.ngc.seaside.jellyfish.cli.gradle.tasks.JellyFishCliCommandTask
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.artifacts.Configuration
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.bundling.Zip
import java.util.Properties

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
            
            repositories {
                mavenLocal()

                maven {
                    credentials {
                        username nexusUsername
                        password nexusPassword
                    }
                    url nexusConsolidated
                }
            }
            
            // This plugin requires the maven plugin to enable uploads to Nexus.
            plugins.apply 'maven'

            // This is required to install a model project locally.
            plugins.apply 'java'
            configurations.testCompile.extendsFrom = []
            
            configurations {
                sd {
                    resolutionStrategy.failOnVersionConflict()
                }
            }
            
            afterEvaluate {
                dependencies {
                    configurations.sd.dependencies.each {
                        compileOnly it
                        testCompile "${it.group}:${it.name}:${it.version}:tests"
                    }
                }
            }

            sourceSets {
                main {
                    resources {
                        srcDirs = ['src/main/sd', 'src/main/resources']
                    }
                }
                test {
                    resources {
                        srcDirs = ['src/test/gherkin', 'src/test/resources']
                    }
                }
            }
            
            task('testJar', type: Jar) {
                classifier = 'tests'
                from sourceSets.test.output
            }
            
            artifacts {
                archives testJar
            }

            // Validate the model is correct.
            task('validateSd', type: JellyFishCliCommandTask) {
                command = 'validate'
                inputDir = "${project.projectDir}"
                build.dependsOn it
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

                // Set the default tasks.
                defaultTasks = ['build']
            }
        }
    }
}
