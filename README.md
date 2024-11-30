# jenkins-mattermost-bot-notifications
Jenkins shared library for  notification using mattermost bot api


# Jenkins Shared Library for Mattermost Notifications

This Jenkins Shared Library allows you to send customizable notifications to Mattermost channels using the Mattermost Bot API. Notifications can be sent in different colors (success, failure, etc.), and the messages are based on globally set environment variables.

## Features

- Sends notifications to a Mattermost channel using the Mattermost Bot API.
- Configurable notification colors (good, danger, warning, etc.).
- Supports customizable success, failure, and aborted messages via global variables.
- Simple to integrate into your Jenkins pipeline.

## Prerequisites

- You must have a **Mattermost** bot set up with the required **personal access token**.
- The following global environment variables need to be set in Jenkins:
  - `MATTERMOST_URL`: The URL of your Mattermost instance.
  - `MATTERMOST_BOT_TOKEN`: The token of the bot that will send messages.
  - `MATTERMOST_CHANNEL_ID`: The channel ID where the messages will be posted.
  - `MESSAGE_SUCCESS`: Customizable message template for successful jobs.
  - `MESSAGE_FAILURE`: Customizable message template for failed jobs.
  - `MESSAGE_ABORTED`: Customizable message template for aborted jobs.

## Installation

1. **Add the shared library to your Jenkins instance**:
   - Go to Jenkins → Manage Jenkins → Configure System.
   - Under **Global Pipeline Libraries**, add a new library:
     - **Name**: `mattermost-notifier`
     - **Source Code Management**: Git
     - **Repository URL**: Your GitHub/Bitbucket repository where the shared library resides.

2. **Set the required environment variables** in Jenkins:
   - Go to Jenkins → Manage Jenkins → Configure System.
   - Under **Global properties**, add the following environment variables:
     - `MATTERMOST_URL`: The URL of your Mattermost instance (e.g., `https://mattermost.example.com`).
     - `MATTERMOST_BOT_TOKEN`: The Mattermost bot's token.
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

Once the library is set up, you can use it in your Jenkins pipeline as follows:

### Example Pipeline

```groovy
@Library('mattermost-notifier') _  // Load the shared library

pipeline {
    agent any

    environment {
        MATTERMOST_URL = 'https://mattermost.example.com'
        MATTERMOST_BOT_TOKEN = 'YOUR_BOT_TOKEN'
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