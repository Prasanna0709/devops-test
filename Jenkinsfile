pipeline{
    agent any
    environment{
        DOCKER_IMAGE = "mydevops"
        CONTAINER_NAME = "mydevopscontainer"
   }
   stages{
    stage('git checkout the code') {
        steps {
            checkout scm
       }
   }
   stage('maven build') {
        steps {
            sh "mvn clean compile"
        }
   }
   stage('docker image creation') {
        steps {
            sh "docker build -t $DOCKER_IMAGE:${BUILD_NUMBER} ."
       }
  }
  stage('docker login') {
        steps {
            withCredentials([usernamePassword(credentialsId:'dockerhub-creds',usernameVariable: 'DOCKER_USER',passwordVariable: 'DOCKER_PASS')]) {
                 sh "echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin"
           }
       }
 }
  stage('docker push') {
        steps {
            sh "docker push $DOCKER_IMAGE:${BUILD_NUMBER}"
       }
 }
 stage('docker deploy') {
    steps {
        script {
            def containerExists = sh(script:"docker ps -aq -f name = $CONTAINER_NAME",returnStdout: true).trim()
            if(containerExists) {
                sh "docker stop $CONTAINER_NAME"
                sh "docker rm $CONTAINER_NAME"
            } else {
                echo "No existing container with $CONTAINER_NAME"
            }
        }

        sh """
            OLD_IMAGE_ID = \$(docker images -q DOCKER_IMAGE | tail -n+2)
            if [ -n "\$OLD_IMAGE_ID" ]; then
                echo "Removing old images..."
               docker rmi -f \$OLD_IMAGE_ID || true
            else
                echo "No old images found."
            fi
       """

       sh 'docker run -d --name ${CONTAINER_NAME} -p 8080:8080 ${DOCKER_IMAGE}:${BUILD_NUMBER}'
    }
 }
