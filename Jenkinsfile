pipeline {
    agent any

    parameters {
        string(name: 'SERVICE_NAME', defaultValue: 'account-service', description: 'Tên service cần build (VD: account-service)')
    }

    environment {
        IMAGE_TAG = 'latest'
    }

    tools {
        maven 'Maven 3.9'
        jdk 'JDK 17'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/kiennt94/microservice'
            }
        }

        stage('Build with Maven') {
            steps {
                echo "📦 Build Maven module: ${params.SERVICE_NAME}"
                sh "mvn clean install -pl ${params.SERVICE_NAME} -am -DskipTests"
            }
        }

        stage('Build Docker Image') {
            steps {
                echo "🐳 Build Docker image: ${params.SERVICE_NAME}:${IMAGE_TAG}"
                sh "docker build -t ${params.SERVICE_NAME}:${IMAGE_TAG} -f ${params.SERVICE_NAME}/docker/Dockerfile ${params.SERVICE_NAME}"
            }
        }

        stage('Push Docker Image') {
            steps {
                echo "📤 Push Docker image to Docker Hub"
                withCredentials([usernamePassword(credentialsId: 'docker-hub-creds', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    script {
                        def imageName = "${DOCKER_USER}/mrs-${params.SERVICE_NAME}:${IMAGE_TAG}"
                        sh 'echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin'
                        sh "docker tag ${params.SERVICE_NAME}:${IMAGE_TAG} ${imageName}"
                        sh "docker push ${imageName}"
                    }
                }
            }
        }

        stage('Deploy Docker Container') {
            steps {
                echo "🚀 Run Docker container for ${params.SERVICE_NAME}"
                sh "docker rm -f ${params.SERVICE_NAME} || true"
                sh "docker run -d --network app-network --name ${params.SERVICE_NAME} ${params.SERVICE_NAME}:${IMAGE_TAG}"
            }
        }
    }

    post {
        success {
            echo "✅ Build và deploy thành công service: ${params.SERVICE_NAME}"
        }
        failure {
            echo "❌ Build thất bại cho service: ${params.SERVICE_NAME}"
        }
    }
}
