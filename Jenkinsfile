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
                bat "mvn clean package"
            }
        }

        stage('docker image creation') {
            steps {
                bat "docker build -t prasanna0218/${DOCKER_IMAGE}:${BUILD_NUMBER} ."
            }
        }

        stage('docker login') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    bat "echo %DOCKER_PASS% | docker login -u %DOCKER_USER% --password-stdin"
                }
            }
        }

        stage('docker push') {
            steps {
                bat "docker push prasanna0218/${DOCKER_IMAGE}:${BUILD_NUMBER}"
            }
        }

        stage('docker deploy') {
            steps {
                script {
                    def containerExists = bat(script: "docker ps -aq -f name=${CONTAINER_NAME}", returnStdout: true).trim()
                    if (containerExists?.trim()) {
                        bat "docker stop ${CONTAINER_NAME}"
                        bat "docker rm ${CONTAINER_NAME}"
                    } else {
                        echo "No existing container with ${CONTAINER_NAME}"
                    }

                    def oldImageID = bat(script: "docker images -q prasanna0218/${DOCKER_IMAGE} | more +1", returnStdout: true).trim()
                    if (oldImageID?.trim()) {
                        echo "Removing old image: ${oldImageID}"
                        bat "docker rmi -f ${oldImageID}"
                    } else {
                        echo "No old images found."
                    }

                    bat "docker run -d --name ${CONTAINER_NAME} -p 8080:8080 prasanna0218/${DOCKER_IMAGE}:${BUILD_NUMBER}"
                }
            }
        }
    }
}
