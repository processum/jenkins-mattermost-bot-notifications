# Jenkins Mattermost Bot Notifications

This Jenkins Shared Library allows you to send customizable notifications to Mattermost channels using the Mattermost Bot API. Notifications can be sent with different colors (e.g., success, failure), and the messages are customizable using global environment variables.

## Features

- Sends notifications to a Mattermost channel using the Mattermost Bot API.
- Configurable notification colors (good, danger, warning, etc.).
- Supports customizable success, failure, and aborted messages via global environment variables.
- Simple to integrate into your Jenkins pipeline.

## Prerequisites

Before using this shared library, ensure you have the following set up:

1. **Mattermost bot**: You must have a bot in your Mattermost instance with the required **personal access token**.
2. **Global environment variables in Jenkins**:
   - `MATTERMOST_CREDENTIALS_ID`: The ID of the Jenkins credentials containing your Mattermost bot token and URL.
   - `MATTERMOST_CHANNEL_ID`: The channel ID where the messages will be posted.
   - `MESSAGE_SUCCESS`: Customizable message for successful jobs.
   - `MESSAGE_FAILURE`: Customizable message for failed jobs.
   - `MESSAGE_ABORTED`: Customizable message for aborted jobs.

## Installation

1. **Add the shared library to your Jenkins instance**:
   - Go to Jenkins → **Manage Jenkins** → **Configure System**.
   - Under **Global Pipeline Libraries**, add a new library:
     - **Name**: `mattermost-notifier`
     - **Source Code Management**: Git
     - **Repository URL**: Your GitHub/Bitbucket repository where the shared library resides.

2. **Set the required global environment variables**:
   - Go to Jenkins → **Manage Jenkins** → **Configure System**.
   - Under **Global properties**, add the following environment variables:
     - `MATTERMOST_CREDENTIALS_ID`: The credentials ID in Jenkins that contains your Mattermost bot's token and URL.
     - `MATTERMOST_CHANNEL_ID`: The channel ID for the target Mattermost channel.
     - `MESSAGE_SUCCESS`: A message template for successful builds (e.g., `✅ Build SUCCESSFUL! Job: ${env.JOB_NAME} #${env.BUILD_NUMBER}`).
     - `MESSAGE_FAILURE`: A message template for failed builds (e.g., `❌ Build FAILED! Job: ${env.JOB_NAME} #${env.BUILD_NUMBER}`).
     - `MESSAGE_ABORTED`: A message template for aborted builds (e.g., `⚠️ Build ABORTED! Job: ${env.JOB_NAME} #${env.BUILD_NUMBER}`).

3. **Add the shared library to your Jenkins pipeline**:
   - In your Jenkinsfile, add the following line at the top:
     ```groovy
     @Library('mattermost-notifier') _
     ```

## Usage

Once the library is set up, you can use it in your Jenkins pipeline to send notifications to Mattermost channels. Below is a sample pipeline using the library.

### Example Jenkinsfile

```groovy
@Library('mattermost-notifier') _  // Load the shared library

pipeline {
    agent any

    environment {
        MATTERMOST_CREDENTIALS_ID = 'your-credentials-id'  // Jenkins credentials ID
        MATTERMOST_CHANNEL_ID = 'your-channel-id'
        MESSAGE_SUCCESS = "✅ Build SUCCESSFUL! Job: ${env.JOB_NAME} #${env.BUILD_NUMBER}"
        MESSAGE_FAILURE = "❌ Build FAILED! Job: ${env.JOB_NAME} #${env.BUILD_NUMBER}"
        MESSAGE_ABORTED = "⚠️ Build ABORTED! Job: ${env.JOB_NAME} #${env.BUILD_NUMBER}"
    }

    stages {
        stage('Build') {
            steps {
                echo 'Building...'
            }
        }
        stage('Test') {
            steps {
                echo 'Testing...'
            }
        }
    }

    post {
        success {
            // Send success notification with green color
            notifyMattermost(MESSAGE_SUCCESS, 'good')
        }
        failure {
            // Send failure notification with red color
            notifyMattermost(MESSAGE_FAILURE, 'danger')
        }
        aborted {
            // Send aborted notification with yellow color
            notifyMattermost(MESSAGE_ABORTED, 'warning')
        }
    }
}
