package iq.rafeeq.studentai.data.remote

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

sealed class AiResponse {
    data class Success(val text: String) : AiResponse()
    data class Error(val message: String, val isKeyMissing: Boolean = false) : AiResponse()
}

class AiRemoteClient(private val context: Context) {

    private val prefs = context.getSharedPreferences("rafeeq_settings", Context.MODE_PRIVATE)

    fun getBackendUrl(): String {
        return prefs.getString(
            "backend_url",
            "https://rafeeq-ai.workers.dev/api/chat" // Default edge function or proxy
        ) ?: "https://rafeeq-ai.workers.dev/api/chat"
    }

    fun setBackendUrl(url: String) {
        prefs.edit().putString("backend_url", url).apply()
    }

    suspend fun askTutor(userQuestion: String, conversationHistory: List<Pair<Boolean, String>>): AiResponse =
        withContext(Dispatchers.IO) {
            val endpoint = getBackendUrl()

            try {
                val url = URL(endpoint)
                val conn = (url.openConnection() as HttpURLConnection).apply {
                    requestMethod = "POST"
                    connectTimeout = 15000
                    readTimeout = 25000
                    doInput = true
                    doOutput = true
                    setRequestProperty("Content-Type", "application/json; charset=UTF-8")
                    setRequestProperty("Accept", "application/json")
                }

                // Construct JSON payload
                val jsonBody = JSONObject().apply {
                    put("question", userQuestion)
                    
                    val messagesArray = JSONArray()
                    conversationHistory.takeLast(6).forEach { (isUser, content) ->
                        val msgObj = JSONObject().apply {
                            put("role", if (isUser) "user" else "assistant")
                            put("content", content)
                        }
                        messagesArray.put(msgObj)
                    }
                    put("messages", messagesArray)
                }

                // Write body
                conn.outputStream.use { os ->
                    OutputStreamWriter(os, "UTF-8").use { writer ->
                        writer.write(jsonBody.toString())
                        writer.flush()
                    }
                }

                val responseCode = conn.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val reader = BufferedReader(InputStreamReader(conn.inputStream, "UTF-8"))
                    val sb = StringBuilder()
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        sb.append(line)
                    }
                    reader.close()

                    val responseJson = JSONObject(sb.toString())
                    val reply = responseJson.optString("reply", "")
                    if (reply.isNotEmpty()) {
                        AiResponse.Success(reply)
                    } else {
                        AiResponse.Error("تم استلام استجابة فارغة من خادم الذكاء الاصطناعي.")
                    }
                } else {
                    val errorStream = conn.errorStream ?: conn.inputStream
                    val reader = BufferedReader(InputStreamReader(errorStream, "UTF-8"))
                    val sb = StringBuilder()
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        sb.append(line)
                    }
                    reader.close()

                    val errorText = sb.toString()
                    val isKeyMissing = errorText.contains("GROQ_API_KEY") ||
                            errorText.contains("missing_api_key") ||
                            responseCode == 401 || responseCode == 503

                    val userMessage = if (isKeyMissing) {
                        "تنبيه المدرس الذكي:\nلم يتم ربط مفتاح الذكاء الاصطناعي (GROQ_API_KEY) في خادم Backend بعد.\n\nيرجى الدخول إلى لوحة تحكم الإدارة أو إعداد الخادم وإضافة المفتاح في متغيرات البيئة ليعمل المدرس الذكي مباشرة."
                    } else {
                        "تعذر الاتصال بخادم الذكاء الاصطناعي (رمز الاستجابة: $responseCode).\nتأكد من تشغيل الخادم وتوفير اتصال بالإنترنت."
                    }

                    AiResponse.Error(userMessage, isKeyMissing)
                }
            } catch (e: Exception) {
                AiResponse.Error(
                    "تعذر الوصول إلى خادم الذكاء الاصطناعي:\n" +
                            "${e.localizedMessage ?: "انقطاع في الاتصال"}\n\n" +
                            "ملاحظة: الذكاء الاصطناعي يتطلب تشغيل خادم Backend الآمن لحماية مفاتيح API ومنع تسريبها داخل التطبيق.",
                    isKeyMissing = false
                )
            }
        }
}
