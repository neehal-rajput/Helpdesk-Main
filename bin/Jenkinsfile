// ============================================================
// Jenkinsfile for HelpDesk Ticketing System
// CI/CD Pipeline: Build → Test → Docker Build → Deploy
// ============================================================

pipeline {

    // 'any' means Jenkins will pick any available agent/worker to run this
    agent any

    // Global variables available throughout the whole pipeline
    environment {
        APP_NAME    = 'helpdesk-app'        // Docker image name
        IMAGE_TAG   = 'latest'              // Docker image tag
        DOCKER_IMG  = "${APP_NAME}:${IMAGE_TAG}"
        K8S_DIR     = 'k8s'                 // Folder containing Kubernetes YAML files
    }

    stages {

        // -------------------------------------------------------
        // STAGE 1: CHECKOUT
        // Gets the latest source code from your Git repository
        // -------------------------------------------------------
        stage('Checkout') {
            steps {
                echo '========== STAGE 1: Checkout Source Code =========='
                checkout scm
                echo 'Source code checked out successfully!'
            }
        }

        // -------------------------------------------------------
        // STAGE 2: BUILD
        // Compiles your Java code and packages it into a .jar file
        // -DskipTests means we skip tests here (we run them in Stage 3)
        // -------------------------------------------------------
        stage('Build') {
            steps {
                echo '========== STAGE 2: Build Application =========='
                sh 'chmod +x ./mvnw'
                sh './mvnw clean package -DskipTests'
                echo 'Build complete! JAR file created in target/ folder.'
            }
            post {
                success {
                    // Archive the built JAR so you can download it from Jenkins UI
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                }
            }
        }

        // -------------------------------------------------------
        // STAGE 3: TEST
        // Runs all unit tests (uses H2 in-memory DB, not real MySQL)
        // Test results are shown in Jenkins UI as a report
        // -------------------------------------------------------
        stage('Test') {
            steps {
                echo '========== STAGE 3: Run Unit Tests =========='
                sh './mvnw test'
                echo 'All tests passed!'
            }
            post {
                always {
                    // Publish test results to Jenkins dashboard
                    junit allowEmptyResults: true,
                          testResults: 'target/surefire-reports/*.xml'
                }
            }
        }

        // -------------------------------------------------------
        // STAGE 4: DOCKER BUILD
        // Builds a Docker image using your Dockerfile
        // Uses Minikube's Docker daemon so the image is available
        // inside Minikube without pushing to Docker Hub
        // -------------------------------------------------------
        stage('Docker Build') {
            steps {
                echo '========== STAGE 4: Build Docker Image =========='
                sh """
                    # Point Docker commands to Minikube's internal Docker
                    eval \$(minikube docker-env)
                    docker build -t ${DOCKER_IMG} .
                    echo "Image '${DOCKER_IMG}' built inside Minikube!"
                """
            }
        }

        // -------------------------------------------------------
        // STAGE 5: DEPLOY
        // Applies all Kubernetes YAML files to deploy the app
        // Order matters: ConfigMap/Secret → MySQL → App
        // -------------------------------------------------------
        stage('Deploy to Minikube') {
            steps {
                echo '========== STAGE 5: Deploy to Kubernetes (Minikube) =========='
                sh """
                    echo '--> Applying ConfigMap and Secrets...'
                    kubectl apply -f ${K8S_DIR}/configmap.yaml
                    kubectl apply -f ${K8S_DIR}/secret.yaml

                    echo '--> Deploying MySQL database...'
                    kubectl apply -f ${K8S_DIR}/mysql-deployment.yaml
                    kubectl apply -f ${K8S_DIR}/mysql-service.yaml

                    echo '--> Waiting for MySQL pod to be ready (up to 2 minutes)...'
                    kubectl rollout status deployment/mysql --timeout=120s

                    echo '--> Deploying HelpDesk application...'
                    kubectl apply -f ${K8S_DIR}/deployment.yaml
                    kubectl apply -f ${K8S_DIR}/service.yaml

                    echo '--> Waiting for HelpDesk pod to be ready (up to 3 minutes)...'
                    kubectl rollout status deployment/helpdesk-app --timeout=180s

                    echo '--> Checking pod status...'
                    kubectl get pods
                    kubectl get services
                """
                echo 'Deployment complete!'
            }
        }

    }

    // -------------------------------------------------------
    // POST-PIPELINE ACTIONS
    // Runs after ALL stages regardless of pass/fail
    // -------------------------------------------------------
    post {
        success {
            echo '✅ PIPELINE SUCCEEDED!'
            echo 'To access your app, run: minikube service helpdesk-service --url'
        }
        failure {
            echo '❌ PIPELINE FAILED! Check the stage logs above for errors.'
        }
        always {
            echo 'Pipeline run finished.'
        }
    }
}
