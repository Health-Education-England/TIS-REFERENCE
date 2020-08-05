@Library('utils@master')_

def utils = new hee.tis.utils()

node {

    def service = "reference"

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

        stage('Analyze Quality') {
          withCredentials([string(credentialsId: 'sonar-token', variable: 'SONAR_TOKEN')]) {
            if (env.CHANGE_ID) {
              sh "'${mvn}' sonar:sonar -Dsonar.login='${SONAR_TOKEN}' -Dsonar.pullrequest.key=$env.CHANGE_ID"
            } else {
              sh "'${mvn}' sonar:sonar -Dsonar.login='${SONAR_TOKEN}' -Dsonar.branch.name=$env.BRANCH_NAME"
            }
          }
        }

        milestone 2

        stage('Dockerise') {
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
          sh "docker build -t heetiscontainerregistry.azurecr.io/reference:$buildVersion -f ./reference-service/Dockerfile ./reference-service"
          sh "docker push heetiscontainerregistry.azurecr.io/reference:$buildVersion"

          sh "docker tag heetiscontainerregistry.azurecr.io/reference:$buildVersion heetiscontainerregistry.azurecr.io/reference:latest"
          sh "docker push heetiscontainerregistry.azurecr.io/reference:latest"

          sh "docker rmi heetiscontainerregistry.azurecr.io/reference:$buildVersion"
          sh "docker rmi heetiscontainerregistry.azurecr.io/reference:latest"

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
              }
            }

        }

    }
}
