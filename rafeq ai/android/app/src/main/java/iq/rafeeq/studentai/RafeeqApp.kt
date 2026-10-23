package iq.rafeeq.studentai

import android.app.Application
import iq.rafeeq.studentai.data.local.AppDatabase
import iq.rafeeq.studentai.data.remote.AiRemoteClient
import iq.rafeeq.studentai.data.repository.ChatRepository
import iq.rafeeq.studentai.data.repository.StudyRepository

class RafeeqApp : Application() {

    lateinit var database: AppDatabase
        private set

    lateinit var studyRepository: StudyRepository
        private set

    lateinit var chatRepository: ChatRepository
        private set

    lateinit var aiClient: AiRemoteClient
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this

        database = AppDatabase.getInstance(this)
        aiClient = AiRemoteClient(this)
        studyRepository = StudyRepository(database)
        chatRepository = ChatRepository(database, aiClient)
    }

    companion object {
        lateinit var instance: RafeeqApp
            private set
    }
}
