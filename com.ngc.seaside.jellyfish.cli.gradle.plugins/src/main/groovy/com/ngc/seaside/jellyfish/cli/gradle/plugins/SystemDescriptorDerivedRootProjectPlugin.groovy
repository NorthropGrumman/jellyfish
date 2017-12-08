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
				task('generateModels', type: Copy) {
		            def outer = it
		            configurations.sd.resolvedConfiguration.resolvedArtifacts.each { 
					    outer.from zipTree(it.file)
		            }
		            into systemDescriptorDirectory
				    build.dependsOn it
				}
				
				task('generateFeatures', type: Copy) {
			        configurations.sd.dependencies.each {
			            project.dependencies.gherkin("${it.group}:${it.name}:${it.version}:tests") {
			                targetConfiguration = 'test'
			            }
			        }
		            def outer = it
		            configurations.gherkin.resolvedConfiguration.resolvedArtifacts.each {
		                outer.from zipTree(it.file)
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
