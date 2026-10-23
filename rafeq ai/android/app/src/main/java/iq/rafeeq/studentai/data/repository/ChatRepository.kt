package iq.rafeeq.studentai.data.repository

import iq.rafeeq.studentai.data.local.AppDatabase
import iq.rafeeq.studentai.data.local.entity.ChatMessageEntity
import iq.rafeeq.studentai.data.remote.AiRemoteClient
import iq.rafeeq.studentai.data.remote.AiResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class ChatRepository(
    private val db: AppDatabase,
    private val aiClient: AiRemoteClient
) {
    val messages: Flow<List<ChatMessageEntity>> = db.chatDao().getAllMessages()

    suspend fun sendMessage(userText: String): AiResponse {
        // Save user message locally
        db.chatDao().insertMessage(
            ChatMessageEntity(
                isUser = true,
                content = userText
            )
        )

        // Gather recent history
        val currentHistory = db.chatDao().getAllMessages().first().map {
            Pair(it.isUser, it.content)
        }

        // Call backend proxy
        val response = aiClient.askTutor(userText, currentHistory)

        when (response) {
            is AiResponse.Success -> {
                db.chatDao().insertMessage(
                    ChatMessageEntity(
                        isUser = false,
                        content = response.text,
                        isError = false
                    )
                )
            }
            is AiResponse.Error -> {
                db.chatDao().insertMessage(
                    ChatMessageEntity(
                        isUser = false,
                        content = response.message,
                        isError = true
                    )
                )
            }
        }

        return response
    }

    suspend fun clearChat() {
        db.chatDao().clearHistory()
    }
}
