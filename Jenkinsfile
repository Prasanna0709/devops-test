pipeline {
    agent any
    environment {
        DOCKER_IMAGE = "mydevops"
        CONTAINER_NAME = "mydevopscontainer"
    }

    stages {
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
                withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
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
                    def containerExists = sh(script: "docker ps -aq -f name=$CONTAINER_NAME", returnStdout: true).trim()
                    if (containerExists) {
                        sh "docker stop $CONTAINER_NAME"
                        sh "docker rm $CONTAINER_NAME"
                    } else {
                        echo "No existing container with $CONTAINER_NAME"
                    }

                    def oldImageID = sh(script: "docker images -q $DOCKER_IMAGE | tail -n+2", returnStdout: true).trim()
                    if (oldImageID) {
                        echo "Removing old image: $oldImageID"
                        sh "docker rmi -f $oldImageID || true"
                    } else {
                        echo "No old images found."
                    }

                    sh "docker run -d --name $CONTAINER_NAME -p 8080:8080 $DOCKER_IMAGE:${BUILD_NUMBER}"
                }
            }
        }
    }
}
