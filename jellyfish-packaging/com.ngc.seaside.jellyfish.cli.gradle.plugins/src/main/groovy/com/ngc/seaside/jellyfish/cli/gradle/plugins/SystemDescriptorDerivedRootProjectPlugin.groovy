package com.ngc.seaside.jellyfish.cli.gradle.plugins

import com.ngc.seaside.jellyfish.cli.gradle.DerivedRootProjectExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Copy

class SystemDescriptorDerivedRootProjectPlugin implements Plugin<Project> {

	private Project project

    @Override
    void apply(Project project) {
    	this.project = project
        project.configure(project) {
            
            apply plugin: 'base'

			addRepositories()
            project.extensions.create('systemDescriptor', DerivedRootProjectExtension, project)
			
			configurations {
                sd {
                    resolutionStrategy.failOnVersionConflict()
                }
                gherkin {
                    resolutionStrategy.failOnVersionConflict()
                    transitive = false
                }
            }
			
            afterEvaluate {
                
				task('generateSd', type: Copy) {
			        dependencies.sd systemDescriptor.project
		            configurations.sd.resolvedConfiguration.resolvedArtifacts.each {
		                from zipTree(it.file)
		            }
		            into systemDescriptor.directory
				    build.dependsOn it
				}
				
				task('generateFeatures', type: Copy) {
			        dependencies.gherkin("${systemDescriptor.project.group}:${systemDescriptor.project.name}:${systemDescriptor.project.version}:tests@zip") {
			            targetConfiguration = 'test'
			        }
		            configurations.gherkin.resolvedConfiguration.resolvedArtifacts.each {
		                from zipTree(it.file)
		            }
		            into systemDescriptor.testDirectory
				    build.dependsOn it
				}
			}
            
            task('clean-gen', group: 'build', description: 'Deletes generated jellyfish projects')
        }
    }
    
    def addRepositories() {
        project.configure(project) {
        	project.repositories {
                mavenLocal()
                maven {
                    credentials {
                        username nexusUsername
                        password nexusPassword
                    }
                    url nexusConsolidated
                }
            }
        }
    }
    
}
