package org.processum

import groovy.json.JsonOutput

class MattermostNotifier {

    static void sendMessage(String url, String token, String channelId, String message, String color = 'good', Closure httpRequest) {
        def payload = [
            channel_id: channelId,
            message: message,
            props:  [
                attachments: [
                    [
                        color: color,
                        text : message
                    ]
                ]
            ]
        ]

        // Динамическое выполнение httpRequest с параметрами
        def response = httpRequest.call(
            httpMode: 'POST',
            url: "${url}/api/v4/posts",
            customHeaders: [[name: 'Authorization', value: "Bearer ${token}"]],
            contentType: 'APPLICATION_JSON',
            requestBody: JsonOutput.toJson(payload)
        )

        if (response.status != 201) {
            error "Failed to send message to Mattermost. Response: ${response.content}"
        }
    }
}
