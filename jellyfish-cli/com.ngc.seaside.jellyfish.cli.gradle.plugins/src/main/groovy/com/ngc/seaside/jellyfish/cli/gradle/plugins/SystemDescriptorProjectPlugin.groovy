package com.ngc.seaside.jellyfish.cli.gradle.plugins

import com.ngc.seaside.gradle.util.GradleUtil
import com.ngc.seaside.jellyfish.api.CommonParameters
import com.ngc.seaside.jellyfish.cli.gradle.tasks.JellyFishCliCommandTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.tasks.GenerateMavenPom
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.tasks.bundling.Jar
import com.ngc.seaside.gradle.plugins.release.SeasideReleaseRootProjectPlugin

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
            task('uploadArchives', dependsOn: publish)

            // Apply the Seaside release plugin so we can release the project.
            plugins.apply 'com.ngc.seaside.release.root'
            configureReleaseTask(p)

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
                build.dependsOn it
            }

            task('testJar', type: Jar) {
                classifier = 'tests'
                extension = 'zip'
                from sourceSets.test.output
                build.dependsOn it
            }

            // Validate the model is correct.
            task('validateSd', type: JellyFishCliCommandTask) { validateTask ->
                command = 'validate'
                arguments = [(CommonParameters.INPUT_DIRECTORY.name): "${project.projectDir}"]
                build.dependsOn validateTask
                sdJar.dependsOn validateTask
                testJar.dependsOn validateTask
                validateTask.dependsOn tasks.withType(GenerateMavenPom)
                // Dependent local projects must be built and installed first
                project.configurations.sd.allDependencies.withType(ProjectDependency).all { dependency ->
                    dependency.dependencyProject.tasks.matching { it.name == 'install' }.all {
                        validateTask.dependsOn it
                    }
                }
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

    private void configureReleaseTask(Project project) {
        def createTag = project.getTasks().getByName(SeasideReleaseRootProjectPlugin.RELEASE_CREATE_TAG_TASK_NAME)
        def bumpVersion = project.getTasks().getByName(SeasideReleaseRootProjectPlugin.RELEASE_BUMP_VERSION_TASK_NAME)
        def push = project.getTasks().getByName(SeasideReleaseRootProjectPlugin.RELEASE_PUSH_TASK_NAME)
        def upload = project.getTasks().getByName('uploadArchives')
        project.task('release',
                     group: SeasideReleaseRootProjectPlugin.RELEASE_ROOT_PROJECT_TASK_GROUP_NAME,
                     description: 'Releases this project.',
                     dependsOn: [createTag, upload, bumpVersion, push])

        upload.mustRunAfter(createTag)
        bumpVersion.mustRunAfter(upload)
        push.mustRunAfter(bumpVersion)
    }
}
