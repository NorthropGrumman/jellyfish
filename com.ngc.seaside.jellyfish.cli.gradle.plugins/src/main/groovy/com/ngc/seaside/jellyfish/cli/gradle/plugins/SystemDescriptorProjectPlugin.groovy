package com.ngc.seaside.jellyfish.cli.gradle.plugins

import com.ngc.seaside.jellyfish.api.CommonParameters
import com.ngc.seaside.jellyfish.cli.gradle.internal.GradleUtil
import com.ngc.seaside.jellyfish.cli.gradle.tasks.JellyFishCliCommandTask
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.artifacts.Configuration
import org.gradle.api.publish.maven.tasks.GenerateMavenPom
import org.gradle.api.publish.maven.MavenPublication
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

            // This is required to install a model project locally.
            plugins.apply 'java'
            jar.enabled = false
            configurations.all {
                outgoing.artifacts.removeAll { artifact ->
                    artifact.extension == 'jar'
                }
            }

            configurations.testCompile.extendsFrom = []

            configurations {
                sd {
                    resolutionStrategy.failOnVersionConflict()
                }
            }

            // This plugin requires the maven-publish plugin to enable uploads to Nexus.
            plugins.apply 'maven-publish'
            // Alias previously-used tasks from maven plugin
            task('install', dependsOn: publishToMavenLocal)
            //task('uploadArchives', dependsOn: publish)

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

            task('sdJar', type: Jar) {
                extension = 'zip'
                from sourceSets.main.output
            }

            task('testJar', type: Jar) {
                classifier = 'tests'
                extension = 'zip'
                from sourceSets.test.output
            }

            // Validate the model is correct.
            task('validateSd', type: JellyFishCliCommandTask) {
                command = 'validate'
                inputDir = "${project.projectDir}"
                build.dependsOn it
                it.dependsOn tasks.withType(GenerateMavenPom)
            }

            afterEvaluate {
                dependencies {
                    configurations.sd.dependencies.each {
                        compile "${it.group}:${it.name}:${it.version}"
                        testCompile "${it.group}:${it.name}:${it.version}:tests@zip"
                    }
                }

                publishing {
                    publications {
                        mavenSd(MavenPublication) {
                            artifact sdJar
                            artifact testJar
                            pom.withXml { xml ->
                                def dependenciesNode = xml.asNode().appendNode('dependencies')
                                p.configurations.sd.dependencies.each { dependency ->
                                    def sdNode = dependenciesNode.appendNode('dependency')
                                    sdNode.appendNode('groupId', dependency.group)
                                    sdNode.appendNode('artifactId', dependency.name)
                                    sdNode.appendNode('version', dependency.version)
                                    sdNode.appendNode('type', sdJar.extension)
                                    sdNode.appendNode('scope', 'compile')
                                    def featureNode = dependenciesNode.appendNode('dependency')
                                    featureNode.appendNode('groupId', dependency.group)
                                    featureNode.appendNode('artifactId', dependency.name)
                                    featureNode.appendNode('version', dependency.version)
                                    featureNode.appendNode('classifier', testJar.classifier)
                                    featureNode.appendNode('type', testJar.extension)
                                    featureNode.appendNode('scope', 'test')
                                }
                            }
                        }
                    }
                    repositories {
                        maven {
                            credentials {
                                username nexusUsername
                                password nexusPassword
                            }
                            url p.version.endsWith('-SNAPSHOT') ? nexusSnapshots : nexusReleases
                        }
                    }
                }

            }
        }
    }
}
