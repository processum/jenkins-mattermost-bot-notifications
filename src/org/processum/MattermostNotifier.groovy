package org.processum

import groovy.json.JsonOutput
import java.net.URL
import java.net.HttpURLConnection

class MattermostNotifier {

    // Нестатический метод для отправки сообщения
    void sendMessage(String url, String token, String channelId, String message, String color = 'good') {
        // Формируем тело запроса
        def payload = [
            channel_id: channelId,
            message: '',
            props:  [
                attachments: [
                    [
                        color: color,
                        text: message
                    ]
                ]
            ]
        ]

        // Преобразуем тело запроса в JSON
        def requestBody = JsonOutput.toJson(payload)

        // URL для отправки запроса
        def endpoint = "${url}/api/v4/posts"
        
        try {
            // Создаем URL и соединение
            def urlConnection = new URL(endpoint).openConnection() as HttpURLConnection
            urlConnection.setRequestMethod('POST')
            urlConnection.setRequestProperty('Authorization', "Bearer ${token}")
            urlConnection.setRequestProperty('Content-Type', 'application/json')
            urlConnection.setDoOutput(true)
            urlConnection.getOutputStream().write(requestBody.getBytes('UTF-8'))

            // Получаем ответ от сервера
            def responseCode = urlConnection.getResponseCode()
            def responseMessage = urlConnection.getResponseMessage()

            // Закрываем соединение
            urlConnection.disconnect()

            // Проверяем успешный статус ответа
            if (responseCode != 201) {
                throw new RuntimeException("Failed to send message to Mattermost. Response: ${responseCode} ${responseMessage}")
            }
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while sending message to Mattermost: ${e.message}")
        }
    }
}
