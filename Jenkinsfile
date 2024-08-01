pipeline {
    agent any


    stages {
        stage('Pull Code'){
            steps{
                git branch: 'main', credentialsId: '6758ba3f-1ac5-4346-9e6d-c0ad051ed35a', url: 'https://github.com/niehmanyo/ISSCodeHosting.git'

            }
        }

        stage("Build"){
            steps{
               sh '''ls
                mvn clean package'''
            }
        }

    }
}

