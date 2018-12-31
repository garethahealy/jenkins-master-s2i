#!/usr/bin/env groovy
@Library("jenkins-plugin-generator-lib@master") _

node("maven") {
    stage('Checkout') {
        withCredentials([usernameColonPassword(credentialsId: 'myproject-githubauth', variable: 'USERPASS')]) {
            sh """
                git clone https://${USERPASS}@github.com/garethahealy/jenkins-master-s2i.git .

                git config --global user.email jenkins-master-s2i@garethahealy.com
                git config --global user.name jenkins-master-s2i
            """
        }
    }

    stage('Generate plugins file') {
        sh "curl -L -s -o ${WORKSPACE}/jenkins.json https://updates.jenkins.io/stable/update-center.actual.json"

        resolvePluginVersions([pluginTemplatePath:"file://${WORKSPACE}/plugins.txt.template", updateCentrePath:"file://${WORKSPACE}/jenkins.json"])
    }

    stage('Push generated plugins file to git') {
        sh """
            git add plugins.txt
            git commit -m 'Generated plugins.txt - build ${env.BUILD_NUMBER}'

            git push origin master
        """
    }

    stage('Trigger jenkins s2i build') {
        openshift.withCluster() {
            openshift.withProject() {
                def buildConfigSelector = openshift.selector("bc", "custom-jenkins-build")
                def buildSelector = buildConfigSelector.startBuild()
                buildSelector.logs('-f')
            }
        }
    }
}