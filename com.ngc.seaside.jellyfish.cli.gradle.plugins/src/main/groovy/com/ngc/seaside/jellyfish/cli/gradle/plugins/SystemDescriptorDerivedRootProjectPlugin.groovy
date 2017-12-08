package com.ngc.seaside.jellyfish.cli.gradle.plugins

import com.ngc.seaside.jellyfish.cli.gradle.DerivedRootProjectPluginConvention
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Copy

class SystemDescriptorDerivedRootProjectPlugin implements Plugin<Project> {

    private DerivedRootProjectPluginConvention pluginConvention
	private Project project

    @Override
    void apply(Project project) {
    	this.project = project
        project.configure(project) {
            
            apply plugin: 'base'

			addRepositories()
			addPluginConvention()
			
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
			        dependencies.sd systemDescriptor
		            configurations.sd.resolvedConfiguration.resolvedArtifacts.each {
		                from zipTree(it.file)
		            }
		            into systemDescriptorDirectory
				    build.dependsOn it
				}
				
				task('generateFeatures', type: Copy) {
			        dependencies.gherkin("${systemDescriptor.group}:${systemDescriptor.name}:${systemDescriptor.version}:tests@zip") {
			            targetConfiguration = 'test'
			        }
		            configurations.gherkin.resolvedConfiguration.resolvedArtifacts.each {
		                from zipTree(it.file)
		            }
		            into systemDescriptorTestDirectory
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
                    url nexusConsolidated
                    credentials {
                        username nexusUsername
                        password nexusPassword
                    }
                }
            }
        }
    }
    
    def addPluginConvention() {
    	pluginConvention = new DerivedRootProjectPluginConvention(project)
    	project.convention.plugins['derivedRootProjectPlugin'] = pluginConvention
    }
}
