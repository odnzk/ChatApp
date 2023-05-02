package com.study.chat.data.remote

import androidx.test.platform.app.InstrumentationRegistry
import com.study.network.ZulipApi
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

class MockRequestDispatcher : Dispatcher() {

    private val getResponses: MutableMap<String, String> = mutableMapOf(
        ZulipApi.MESSAGES_END_POINT to "messages.json"
    )
    private val postResponses: MutableMap<String, String> = mutableMapOf(
        ZulipApi.MESSAGES_END_POINT to "send_message.json",
        ZulipApi.UPDATE_REACTION_END_POINT to EMPTY_RESPONSE
    )

    override fun dispatch(request: RecordedRequest): MockResponse {
        val endPointsMap = when (request.method) {
            "POST" -> postResponses
            "GET" -> getResponses
            else -> getResponses
        }
        return endPointsMap.findEndPoint(request.path)?.let { endPoint ->
            toSuccessResponse(endPointsMap[endPoint] ?: EMPTY_RESPONSE)
        } ?: createErrorResponse()
    }

    private fun createErrorResponse() = MockResponse().setResponseCode(ERROR_CODE)

    private fun toSuccessResponse(filePath: String): MockResponse =
        MockResponse().apply {
            setResponseCode(SUCCESS_CODE)
            if (filePath != EMPTY_RESPONSE) {
                setBody(loadFromAssets(filePath))
            }
        }


    private fun loadFromAssets(path: String) =
        InstrumentationRegistry.getInstrumentation().context.resources.assets.open(path).use {
            it.bufferedReader().readText()
        }

    private fun Map<String, String>.findEndPoint(path: String?): String? =
        path?.let { keys.find { endPoint -> path.contains(endPoint) } }

    companion object {
        private const val SUCCESS_CODE = 200
        private const val ERROR_CODE = 404
        private const val EMPTY_RESPONSE = ""
    }


}

