@Library('utils@master')_

def utils = new hee.tis.utils()

node {

    def service = "reference"
    def containerRegistryLocaltion = "430723991443.dkr.ecr.eu-west-2.amazonaws.com"

    deleteDir()

    stage('Checkout Git Repo') {
      checkout scm
    }

    env.GIT_COMMIT=sh(returnStdout: true, script: "git log -n 1 --pretty=format:'%h'").trim()
    def mvn = "${tool 'Maven 3.3.9'}/bin/mvn"
    def workspace = pwd()
    def parent_workspace = pwd()
    def repository = "${env.GIT_COMMIT}".split("TIS-")[-1].split(".git")[0]
    def buildNumber = env.BUILD_NUMBER
    def buildVersion = env.GIT_COMMIT
    def imageName = ""
    def imageVersionTag = ""
    boolean isService = false

    println "[Jenkinsfile INFO] Commit Hash is ${GIT_COMMIT}"

    if (fileExists("$workspace/$service-service/pom.xml")) {
        workspace = "$workspace/$service-service"
        env.WORKSPACE= workspace
        sh 'cd "$workspace"'
        isService = true
    }

    try {

        milestone 1


        stage('Build') {
          sh "'${mvn}' clean install -DskipTests"
        }

        stage('Unit Tests') {
          try {
            sh "'${mvn}' clean test"
          } finally {
            junit '**/target/surefire-reports/TEST-*.xml'
          }
        }

        milestone 2

        stage('Dockerise') {
          sh "aws ecr get-login-password --region eu-west-2 | docker login --username AWS --password-stdin 430723991443.dkr.ecr.eu-west-2.amazonaws.com"
          env.VERSION = utils.getMvnToPom(workspace, 'version')
          env.GROUP_ID = utils.getMvnToPom(workspace, 'groupId')
          env.ARTIFACT_ID = utils.getMvnToPom(workspace, 'artifactId')
          env.PACKAGING = utils.getMvnToPom(workspace, 'packaging')
          imageName = env.ARTIFACT_ID
          imageVersionTag = env.GIT_COMMIT

          if (isService) {
              imageName = service
              env.IMAGE_NAME = imageName
          }

          sh "mvn package -DskipTests"
          sh "cp ./reference-service/target/reference-service-*.war ./reference-service/target/app.jar"
          sh "docker build -t ${containerRegistryLocaltion}/${service}:$buildVersion -f ./reference-service/Dockerfile ./reference-service"
          sh "docker push ${containerRegistryLocaltion}/${service}:$buildVersion"

          sh "docker tag ${containerRegistryLocaltion}/${service}:$buildVersion ${containerRegistryLocaltion}/reference:latest"
          sh "docker push ${containerRegistryLocaltion}/${service}:latest"

          sh "docker rmi ${containerRegistryLocaltion}/${service}:$buildVersion"
          sh "docker rmi ${containerRegistryLocaltion}/${service}:latest"

          println "[Jenkinsfile INFO] Stage Dockerize completed..."
        }

    } catch (err) {

        throw err

    } finally {

        if (env.BRANCH_NAME == "master") {

            milestone 3

            stage('Staging') {
              node {
                println "[Jenkinsfile INFO] Stage Deploy starting..."

                sh "ansible-playbook -i $env.DEVOPS_BASE/ansible/inventory/stage $env.DEVOPS_BASE/ansible/${service}.yml --extra-vars=\"{\'versions\': {\'${service}\': \'${env.GIT_COMMIT}\'}}\""
              }
            }

            milestone 4

            stage('Approval') {
              timeout(time:5, unit:'HOURS') {
                input message: 'Deploy to production?', ok: 'Deploy!'
              }
            }

            milestone 5

            stage('Production') {
              node {
                sh "ansible-playbook -i $env.DEVOPS_BASE/ansible/inventory/prod $env.DEVOPS_BASE/ansible/${service}.yml --extra-vars=\"{\'versions\': {\'${service}\': \'${env.GIT_COMMIT}\'}}\""
                sh "ansible-playbook -i $env.DEVOPS_BASE/ansible/inventory/nimdta $env.DEVOPS_BASE/ansible/${service}.yml --extra-vars=\"{\'versions\': {\'${service}\': \'${env.GIT_COMMIT}\'}}\""
              }
            }

        }

    }
}
