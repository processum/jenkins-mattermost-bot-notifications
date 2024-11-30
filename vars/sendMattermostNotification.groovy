import org.processum.MattermostNotifier

def call(String message, String color = 'good') {

    def url = env.MATTERMOST_URL
    def token = env.MATTERMOST_BOT_TOKEN
    def channelId = env.MATTERMOST_CHANNEL_ID

    if (!url || !token || !channelId) {
        echo "Error: MATTERMOST_URL, MATTERMOST_BOT_TOKEN, and MATTERMOST_CHANNEL_ID must be set as global environment variables!"
        error "Required environment variables are missing!"
    }

    MattermostNotifier.sendMessage(url, token, channelId, message, color)
}
