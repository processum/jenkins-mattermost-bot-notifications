def generateSuccessMessage(env, currentBuild) {
    return """✅ Job: *${env.JOB_NAME} #${env.BUILD_NUMBER}* Status: *${currentBuild.currentResult}* Duration: ${currentBuild.durationString} *<${env.JENKINS_URL}blue/organizations/jenkins/${env.JOB_NAME.replaceAll('/', '%2F')}/detail/${env.JOB_BASE_NAME}/${env.BUILD_ID}/pipeline|Link>*"""
}

def generateErrorMessage(env, currentBuild) {
    return """❌ Job: *${env.JOB_NAME} #${env.BUILD_NUMBER}* Status: *${currentBuild.currentResult}* *<${env.JENKINS_URL}blue/organizations/jenkins/${env.JOB_NAME.replaceAll('/', '%2F')}/detail/${env.JOB_BASE_NAME}/${env.BUILD_ID}/pipeline|Link>*"""
}

def generateInitMessage(env, currentBuild) {
    return """🔄 Job: *${env.JOB_NAME} #${env.BUILD_NUMBER}* Status: *${currentBuild.getBuildCauses()[0].shortDescription}*  *<${env.JENKINS_URL}blue/organizations/jenkins/${env.JOB_NAME.replaceAll('/', '%2F')}/detail/${env.JOB_BASE_NAME}/${env.BUILD_ID}/pipeline|Link>*"""
}
