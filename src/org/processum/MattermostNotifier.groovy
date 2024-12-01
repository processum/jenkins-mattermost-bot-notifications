package org.processum

import groovy.json.JsonOutput

class MattermostNotifier {

    // Method to send a message to Mattermost
    static void sendMessage(String url, String token, String channelId, String message, String color = 'good') {
        // Prepare the payload
        def payload = [
            channel_id: channelId,
            message: '',
            props:  [
                attachments: [
                    [
                        color: color,
                        text : message
                    ]
                ]
            ]
        ]

        // Use the Jenkins pipeline 'httpRequest' step within a script block
        def response = null
        script {
            response = httpRequest(
                httpMode: 'POST',
                url: "${url}/api/v4/posts",
                customHeaders: [[name: 'Authorization', value: "Bearer ${token}"]],
                contentType: 'APPLICATION_JSON',
                requestBody: JsonOutput.toJson(payload)
            )
        }

        // Check the response and handle errors if necessary
        if (response.status != 201) {
            error "Failed to send message to Mattermost. Response: ${response.content}"
        }
    }
}
