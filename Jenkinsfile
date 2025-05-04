pipeline {
    agent any

    environment {
        MAVEN_HOME = '/usr/share/maven'
        DOCKER_COMPOSE_FILE = 'docker-compose.yml'
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
                sh 'mvn clean install -DskipTests'
            }
        }

        stage('Build Docker Images') {
            steps {
                script {
                    def services = [
                        'config-server',
                        'eureka-server',
                        'gateway-service',
                        'account-service',
                        'position-service',
                        'department-service'
                    ]

                    for (svc in services) {
                        sh "docker build -t ${svc}:${IMAGE_TAG} -f ${svc}/docker/Dockerfile ${svc}"
                    }
                }
            }
        }

        stage('Deploy with Docker Compose') {
            steps {
                sh "docker compose -f ${DOCKER_COMPOSE_FILE} up -d --build"
            }
        }
    }

    post {
        success {
            echo '✅ CI/CD pipeline completed successfully!'
        }
        failure {
            echo '❌ Build failed!'
        }
    }
}
