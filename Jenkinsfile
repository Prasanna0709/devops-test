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
                    try {
                        bat "docker stop ${CONTAINER_NAME}"
                        bat "docker rm ${CONTAINER_NAME}"
                    } catch (Exception e) {
                        echo "Container not running or doesn't exist. Skipping stop/remove."
                    }

                    def oldImageIDs = bat(script: "docker images prasanna0218/${DOCKER_IMAGE} -q | more +1", returnStdout: true).trim()

                    if (oldImageIDs) {
                        oldImageIDs.split().each { imageID ->
                            bat "docker rmi -f ${imageID}"
                        }
                    } else {
                        echo "No old images to remove."
                    }

                    bat "docker run -d --name ${CONTAINER_NAME} -p 8080:8080 prasanna0218/${DOCKER_IMAGE}:${BUILD_NUMBER}"
                }
            }
        }
    }
}
