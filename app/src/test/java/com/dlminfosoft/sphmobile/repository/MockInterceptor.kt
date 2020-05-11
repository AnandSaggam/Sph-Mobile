package com.dlminfosoft.sphmobile.repository

import com.dlminfosoft.sphmobile.BuildConfig
import com.dlminfosoft.sphmobile.TestUtils.FAILURE_RESULT
import com.dlminfosoft.sphmobile.TestUtils.SUCCESS_RESULT
import com.dlminfosoft.sphmobile.TestUtils.SUCCESS_RESULT_WITHOUT_RECORD
import com.dlminfosoft.sphmobile.TestUtils.getSuccessResponseBody
import com.dlminfosoft.sphmobile.TestUtils.getSuccessResponseBodyWithoutRecord
import okhttp3.*

/**
 * This class is interceptor used with retrofit client
 * which provides fake response based on url end code
 */
class MockInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (BuildConfig.DEBUG) {
            var code = 500
            val uri = chain.request().url().uri().toString()
            val responseString = when {
                uri.endsWith(SUCCESS_RESULT) -> {
                    code = 200
                    getSuccessResponseBody
                }
                uri.endsWith(SUCCESS_RESULT_WITHOUT_RECORD) -> {
                    code = 200
                    getSuccessResponseBodyWithoutRecord
                }
                uri.endsWith(FAILURE_RESULT) -> ""
                else -> ""
            }
            return chain.proceed(chain.request())
                .newBuilder()
                .code(code)
                .protocol(Protocol.HTTP_2)
                .message(responseString)
                .body(
                    ResponseBody.create(
                        MediaType.parse("application/json"),
                        responseString.toByteArray()
                    )
                )
                .addHeader("content-type", "application/json")
                .build()
        } else {
            // Just to be on safe side.
            throw IllegalAccessError(
                "MockInterceptor is only meant for Testing Purposes and " +
                        "bound to be used only with DEBUG mode"
            )
        }
    }

}
