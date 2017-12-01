package com.ngc.seaside.jellyfish.cli.gradle.plugins

import com.ngc.seaside.jellyfish.cli.gradle.ModelUnpacker
import org.gradle.api.Plugin
import org.gradle.api.Project

class SystemDescriptorDerivedRootProjectPlugin implements Plugin<Project> {

    @Override
    void apply(Project p) {
        p.configure(p) {
            repositories {
                mavenLocal()

                maven {
                    url nexusConsolidated
                }
            }

            configurations {
                generate
            }

            ext {
                systemDescriptorDirectoryName = 'models'

                unpacker = new ModelUnpacker()
                      .setDestinationDirectory(file("${project.buildDir}/${systemDescriptorDirectoryName}"))
                      .setConfiguration(configurations.generate)
                      .setExecuteCondition({ !file("${project.buildDir}/${systemDescriptorDirectoryName}").exists() })
            }

            task('clean-gen', group: 'build', description: 'Deletes generated jellyfish projects')
        }
    }
}
