import org.processum.MattermostNotifier

 
/**
 * Send a message to Mattermost.
 * Automatically uses MATTERMOST_CREDENTIALS_ID if provided.
 */
 
def call(String message, String color) {
    def url = env.MATTERMOST_URL
    def channelId = env.MATTERMOST_CHANNEL_ID
    def credentialsId = env.MATTERMOST_CREDENTIALS_ID

    def notifier = new MattermostNotifier()

    if (!url || !channelId) {
        error "MATTERMOST_URL and MATTERMOST_CHANNEL_ID must be set as global environment variables!"
    }

    if (credentialsId) {
        // Use Jenkins Credentials if MATTERMOST_CREDENTIALS_ID is defined
        withCredentials([string(credentialsId: credentialsId, variable: 'MATTERMOST_BOT_TOKEN')]) {
            def token = MATTERMOST_BOT_TOKEN
            notifier.sendMessage(url, token, channelId, message, color)
            echo token
        }
    } else {
        // Fallback to global environment variable MATTERMOST_BOT_TOKEN
        def token = env.MATTERMOST_BOT_TOKEN
        if (!token) {
            error "MATTERMOST_BOT_TOKEN must be set as a global environment variable if MATTERMOST_CREDENTIALS_ID is not provided!"
        }
        notifier.sendMessage(url, token, channelId, message, color)
    }
}
