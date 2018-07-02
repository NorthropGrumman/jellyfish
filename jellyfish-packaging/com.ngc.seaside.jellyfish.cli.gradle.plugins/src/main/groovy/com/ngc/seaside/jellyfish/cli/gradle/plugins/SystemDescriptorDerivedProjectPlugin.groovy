package com.ngc.seaside.jellyfish.cli.gradle.plugins

import com.ngc.seaside.jellyfish.api.CommonParameters
import com.ngc.seaside.jellyfish.cli.gradle.JellyFishProjectGenerator
import com.ngc.seaside.jellyfish.utilities.command.JellyfishCommandPhase
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project

class SystemDescriptorDerivedProjectPlugin implements Plugin<Project> {

   @Override
   void apply(Project p) {
      p.configure(p) {
         // TODO TH: This is a workaround to ensure that repositories are defined so that the SD project dependencies
         // can be resolved when the project is configured.  We can't use the repositories plugin because that plugin
         // does not create the repositories until after the project is evaluated.
         addRepositories(p)
         cleanGen(p)
         generate(p)

         apply from: "${project.projectDir}/build.generated.gradle"
      }
   }

   /**
    * Runs jellyfish to generate the project, if necessary.
    */
   private def generate(Project p) {
      if (!p.file("${p.projectDir}/build.generated.gradle").exists()) {
         p.logger.info(":${p.name}:generate")
         def systemDescriptor = p.parent.systemDescriptor
         def sdProject = systemDescriptor.project
         def gav = "${sdProject.group}:${sdProject.name}:${sdProject.version}"
         // Note when running Jellyfish from Gradle, we are always running in the deferred phase.
         new JellyFishProjectGenerator(p.logger)
               .setCommand(p.command)
               .setArguments([
               (CommonParameters.MODEL.name)                 : "${systemDescriptor.model}",
               (CommonParameters.DEPLOYMENT_MODEL.name)      : systemDescriptor.deploymentModel,
               (CommonParameters.GROUP_ARTIFACT_VERSION.name): gav,
               (CommonParameters.OUTPUT_DIRECTORY.name)      : "${p.rootDir.absolutePath}",
               (CommonParameters.UPDATE_GRADLE_SETTING.name) : 'false',
               (CommonParameters.PHASE.name)                 : JellyfishCommandPhase.DEFERRED.toString()])
               .generate()
      }
   }

   /**
    * Deletes the generated files, if necessary. This will occur when gradle was explicitly run with the clean-gen
    * task or when jellyfish-cli or the system descriptor has changed.
    */
   private def cleanGen(Project p) {
      def systemDescriptor = p.parent.systemDescriptor
      def sdProject = systemDescriptor.project
      // Generate md5 for jellyfish-cli dependency and its transitive dependencies
      def md5 = generateMD5(findCliDependencyFiles(p),
                            findSdDependencyFiles(p, systemDescriptor),
                            "${sdProject.group}:${sdProject.name}:${sdProject.version}",
                            "${systemDescriptor.model}",
                            "${systemDescriptor.deploymentModel}")
      def md5File = p.file("${p.projectDir}/.cli-dependencies.md5")

      def shouldCleanGen = !md5File.exists() ||
                           md5File.text != md5 ||
                           p.gradle.startParameter.taskNames.contains('clean-gen')
      if (shouldCleanGen) {
         p.logger.info(":${p.name}:clean-gen")
         p.delete 'src'
         p.delete 'build.generated.gradle'
         md5File.text = md5
      }
   }

   private def findCliDependencyFiles(Project p) {
      def cliDependency
      def parentProject = p
      while (true) {
         cliDependency = parentProject.buildscript
               .configurations
               .classpath
               .dependencies
               .find { it.group == 'com.ngc.seaside' && it.name == 'jellyfish.cli.gradle.plugins' }
         if (cliDependency != null) {
            break
         }
         parentProject = parentProject.parent
         if (parentProject == null) {
            throw new GradleException(
                  'com.ngc.seaside.jellyfish.system-descriptor-derived plugin requires ' +
                  'com.ngc.seaside.jellyfish.cli.gradle.plugins on the classpath')
         }
      }
      return parentProject.buildscript.configurations.classpath.files(cliDependency)
   }

   private def findSdDependencyFiles(Project p, systemDescriptor) {
      p.configurations {
         temp
      }
      def sdProject = systemDescriptor.project
      p.dependencies {
         temp sdProject
      }
      def sdDependency = p.configurations
            .temp
            .dependencies
            .find { sdProject.group == it.group && sdProject.name == it.name }
      def files = p.configurations.temp.files(sdDependency)
      p.configurations.remove p.configurations.temp
      return files
   }

   private def generateMD5(Iterable<File> cliFiles, Iterable<File> sdFiles, String... params) {
      def digest = java.security.MessageDigest.getInstance('MD5')
      (cliFiles + sdFiles).sort { it.name }.each {
         digest.update(it.name.bytes)
         it.eachByte(4096) { buffer, length ->
            digest.update(buffer, 0, length)
         }
      }
      params.each {
         digest.update(it?.bytes ?: ''.bytes)
      }
      return digest.digest().encodeHex().toString()
   }

   // See TODO note above
   private def addRepositories(project) {
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
