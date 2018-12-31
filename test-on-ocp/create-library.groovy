import hudson.model.*
import jenkins.model.*
import jenkins.plugins.git.GitSCMSource

import org.jenkinsci.plugins.workflow.libs.LibraryConfiguration
import org.jenkinsci.plugins.workflow.libs.SCMSourceRetriever
import org.jenkinsci.plugins.workflow.libs.GlobalLibraries

final Jenkins jenkins = Jenkins.getInstance()

List<LibraryConfiguration> configurations = new ArrayList<LibraryConfiguration>()

GlobalLibraries globalLibraries = GlobalLibraries.get()
List<LibraryConfiguration> configurationsInmutable = globalLibraries.getLibraries()
configurationsInmutable.each {
    configurations.add(it)
}

GitSCMSource librarySource = new GitSCMSource("jenkins-plugin-generator-lib", "https://github.com/garethahealy/jenkins-plugin-generator-lib.git",  null, "*", "", true)

LibraryConfiguration config = new LibraryConfiguration("jenkins-plugin-generator-lib", new SCMSourceRetriever(librarySource))
config.setAllowVersionOverride(true)
config.setImplicit(false)
config.setDefaultVersion("master")

configurations.add(config)

globalLibraries.setLibraries(configurations)

jenkins.save()