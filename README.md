[![Build Status](https://travis-ci.org/garethahealy/jenkins-master-s2i.svg?branch=master)](https://travis-ci.org/garethahealy/jenkins-master-s2i)
[![License](https://img.shields.io/hexpm/l/plug.svg?maxAge=2592000)]()

# jenkins-master-s2i
Generate a plugins.txt for a container based Jenkins deployment

## testing on minishift
Start minishift:
- minishift start --cpus 4 --memory 8GB --iso-url centos

Create git clone secret:
- oc create secret generic githubauth --from-literal=username=<replace with username> --from-literal=password=<eplace with password> --type=kubernetes.io/basic-auth

Deploy jenkins:
- oc process jenkins-ephemeral -n openshift | oc apply -f -

Configure jenkins with global library:
- export JENKINS_ROUTE=https://$(oc get route jenkins -o jsonpath='{.spec.host}')
- export TOKEN=$(oc whoami -t)
- curl -k -L ${JENKINS_ROUTE}/scriptText -H "Authorization: Bearer ${TOKEN}" $JENKINS_ROUTE --data-urlencode "script=$(< test-on-ocp/create-library.groovy)"

Create build objects:
- oc apply -f test-on-ocp/custom-jenkins-build.yml
- oc apply -f test-on-ocp/jenkins-build-pipeline.yml
- oc start-build jenkins-build-pipeline