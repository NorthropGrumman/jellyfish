---
title: Ch. 11 Configuration Management
book-title: Modeling with the System Descriptor
book-page: modeling-with-the-system-descriptor
next-title: Appendix A - System Descriptor Keywords
next-page: appendix-a
prev-title: Ch. 10 Testing the Threat Evaluation System
prev-page: testing-the-threat-evaluation-system
---
{% include base.html %} 
One of the most useful capacities of the System Descriptor (SD) concept is that it adheres to
configuration management best practices.  System Descriptor projects can be versioned, released, and distributed like
transitional software or managed artifacts. Configuration management for SD projects involves the use of the
[Gradle](https://gradle.org/) build tool and [Git](https://git-scm.com/) source control management.

The basic workflow for configuration management of an SD project looks like the following:
1. The project and models are developed using Git to provide source control management.  Team members can utilize
   branches, merging, and pull requests (see below) to contribute to the project.
1. The project is validated and packaged using Gradle. The packaging phase results in a set of ZIP files being created
   and are referred to as artifacts.   These artifacts form the distribution of the project that is released to other
   teams or organizations.  Git tags are used to archive the release of the project.
1. Once the project is packaged, the artifacts are released.  The release phase also uses Gradle.  During this phase,
   the artifacts are uploaded to a remote binary archive repository (BAR).  BARs are responsible for managing binary or
   released artifacts and providing access to these artifacts.  Users can then download related artifacts from a BAR on
   demand.  This enables easy distribution of artifacts.  Examples of BARs include Nexus Repository and Artifactory.

By default, two artifacts are created during a release of an SD project.  The first is a ZIP file that contains all the
`.sd` files that are stored in `src/main/sd`.  The second is a ZIP file that contains all the feature files and other
files stored in `src/main/test`.

# Configuring a Project for Gradle
In this example, we'll configure the `Archipelago` project to be a Gradle build.  Gradle can be [installed
manually](https://gradle.org/install/#manually) or the Gradle wrapper can be used.  See
https://docs.gradle.org/current/userguide/gradle_wrapper.html for more information about the wrapper.

We'll start by creating two new files in the `Archipelago` directory, alongside the `src` directory.  The first file is
named `settings.gradle`.  This file configures the name of the project and allows for the configuration of any
_sub-projects_.

**settings.gradle**
```groovy
rootProject.name = 'archipelago'
```

The project name will be used to form the names of artifacts that are released as part of the project.  By convention,
artifact names are lowercase and use `.` or `-` to separate words.

The next file is named `build.gradle`.  This file is the heart of the Gradle build configuration and describes how to
build and release an SD project.

**build.gradle**
```groovy
buildscript {
    repositories {
        mavenLocal()
 
        maven {
            url http://10.207.42.137/nexus/repository/maven-public // (1)
        }
    }
     
    dependencies {
        classpath 'com.ngc.seaside:jellyfish.cli.gradle.plugins:1.8.2' // (2)
    }
}
 
apply plugin: 'com.ngc.seaside.jellyfish.system-descriptor' // (3)
 
group = 'com.ngc.seaside.archipelago' // (4)
version = '1.0.0-SNAPSHOT' // (5)
```

This script configures how to build the project.  The repositories section is used to configure the location of a BAR
where needed files will be automatically downloaded during the build.  (1) configures the actual URL of the repository.
This value will change depending on project and the organization hosting the repository.  This repository is only used
to read or download artifacts.  This does not configure where the SD artifacts will be published too.  (2) enables the
use of a Gradle plugin that is used with SD projects.  (3) applies this plugin to the project.  (4) configures a _group
ID_ for the artifacts that will be released.  Group IDs are used to organize artifacts.  Multiple artifacts can be
organized under a single group ID.  By convention, groups IDs are created to reflect the name of an _organization_ as
opposed to artifacts or products managed by that organization.  Artifact IDs are used to identify individual products
managed by that organization.  Finally, (5) configures the current version of the project.  More information about
versioning can be found below.

Before running the build, it is necessary to set some user specific variables.  These variables should be placed in a
file named `gradle.properties`.  The default location of this file should be within the directory `$user/.gradle`.
However, this directory can be changed by configuring the environmental variable `$GRADLE_USER_HOME`.  This file should
set the following properties:

**gradle.properties**
```plaintext
nexusReleases=https://nexusrepomgr.ms.northgrum.com/repository/maven-ng-releases/ # (1)
nexusSnapshots=https://nexusrepomgr.ms.northgrum.com/repository/maven-ng-snapshots/ # (2)
 
nexusUsername=username # (3)
nexusPassword=password
```

These properties configure the location of the repository to upload released artifacts to.  It also contains the
credentials of the user to perform the upload.  (1) configures the location of the repository for fully released
artifacts.  (2) configures the location of the repository for snapshot (beta) releases.  Snapshots are discussed below.
(3) configures the user's repository username and password.

Once the build file has been added and `gradle.properties` has been configured, open a command line and move to the
`Archipelago` directory.  Run the command `gradle clean build`.  This will validate the SD project and produce the ZIP
artifacts.  These artifacts can be found in the `build` directory.

# Managing a Project with Git
Since an SD project is made up only of plain text files, it is easy to manage these projects with Git.  SD projects and
their Gradle build files can be committed to a Git repository.  Releases can be made from this repository and tags can
be created to archive different versions of the projects.  In addition, techniques such as 
[pull requests](https://git-scm.com/book/en/v2/GitHub-Contributing-to-a-Project) can also be utilized to enable 
distributed development of a project.  Conflicts can be managed with standard diff like tooling.

A full explanation of how to use Git can be found at https://git-scm.com/book/en/v2.

# Release Management
Version of an SD project is important as it can impact downstream efforts and teams.  By convention, versioning of SD
projects follows [Semantic Versioning](https://semver.org/) principles.  These principles, adapted to modeling projects,
are summarized below:
1. Version numbers follow the format `MAJOR.MINOR.PATCH`.
1. Increment the MAJOR version when you make incompatible model changes.  Examples of this include removing inputs from
   a component, moving or renaming a component, combining or splitting a component, etc.  This also includes removing
   scenarios.
1. Increment the MINOR version when you add functionality in a backwards-compatible manner.  This includes _adding_ new
   inputs, output, scenarios, etc as these will not force internal redesign of existing component implementations.
1. Increment the PATCH version when you make backwards-compatible fixes or updates that may be not relevant to
   downstream teams.  This includes adding new test cases or updating feature files, updating metadata or documentation,
   adding more validation information for data, etc.

Note that the `SNAPSHOT` qualifier can be added to a version to indicate that the given artifact is only a _snapshot_ or
beta release of an artifact.  For example, version 1.2.3-SNAPSHOT is not the actual 1.2.3 release but only a beta
release of 1.2.3 will can be used for testing before the full release.  Snapshot versions are never full releases. Prior
to a release, the SNAPSHOT qualifier must be removed.

Model releases should be incremental.  Release often with small sets of changes as opposed to releasing infrequently
with large sets of changes.  Major rework should be planned and executed in small batches and releases as opposed to a
"big bang" release.

# Performing a Release
A release of an SD project can be performed as follows.  These commands should be executed from the directory that
contains the model project.  This example will perform a release of `Archipelago`:
1. Determine if this release is a major, minor, or patch release.  This example will be a minor releasing moving from
   version 1.0.0 to 1.1.0.  Update the vesion property in the `build.gradle` file to 1.1.0.  Commit this change to Git
   with `git commit -am "Preparing for v1.1.0 release."`.
1. Create a Git tag for the release.  By convention, the tag name is the version of the release prefixed with `v`.  This
   can be done with `git tag -a v1.1.0 -m "Release of v1.1.0"`.
1. Run `gradle clean build upload` to release the artifacts to the remote repository.
1. Once the artifacts have been released, increment the version number to the next snapshot.  By convention, the patch
   version is incremented until the team determines if the next release is a major, minor, or patch release.  We simply
   assume the next release is a patch release until we determine otherwise.  In this example, the version property would
   be updated to 1.1.1-SNAPSHOT and committed to Git.
1. Finally, the Git tag and changes need to be pushed with `git push --tags ; git push`.

**Automatic Releases**
```note-info
Gradle plugins also exists which make it possible to do automatic releases from tools like Jenkins.  This makes it 
possible to create an _deployment pipeline_ for modeling projects.  Building such a pipeline is addressed in more
advanced topics.
```

# Conclusion
* Use semantic versions rules to make versioning of a project meaningful.  Do not mix arbitrary "marketing" versions
  with meaningful semantic versions.  All technical documentation and software should reference artifacts using their
  semantic versions.
* Good practice does not allow the re-release of artifacts.  This means that a versioned, released artifact cannot be
  changed even if the artifact is faulty.  Simply release a new version of the artifact with a fix.  Most BARs do not
  allow released artifacts to be re-released, re-deployed, or otherwise changed. 
* Snapshots are _not_ full releases.  Snapshots represent "nightly builds" or beta builds that can be used to test with.
  Unlike releases, SNAPSHOTS will change over time.