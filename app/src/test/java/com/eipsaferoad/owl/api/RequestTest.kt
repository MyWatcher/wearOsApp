package com.eipsaferoad.owl.api

import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import okhttp3.FormBody
import okhttp3.Headers
import okhttp3.OkHttpClient
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RequestTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        mockkConstructor(OkHttpClient::class)
        every { anyConstructed<OkHttpClient>().newCall(any()).execute() } returns mockk {
            every { isSuccessful } returns true
            every { body!!.string() } returns "Success"
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `makeRequest calls callback with successful response`() = runTest {
        val url = "https://example.com"
        val headers = Headers.Builder().add("Authorization", "Bearer token").build()
        val body = FormBody.Builder().add("key", "value").build()
        val requestType = Request.Companion.REQUEST_TYPE.POST
        val callback = mockk<(String) -> Unit>(relaxed = true)
        Request.makeRequest(url, headers, body, requestType, callback)
        testDispatcher.scheduler.advanceUntilIdle()
        verify(exactly = 0) { callback("Success") }
    }

    @Test
    fun `makeRequest handles exception during request`() = runTest {
        every { anyConstructed<OkHttpClient>().newCall(any()).execute() } throws Exception("Network error")

        val url = "https://example.com"
        val headers = Headers.Builder().add("Authorization", "Bearer token").build()
        val body = FormBody.Builder().add("key", "value").build()
        val requestType = Request.Companion.REQUEST_TYPE.POST
        val callback = mockk<(String) -> Unit>(relaxed = true)
        Request.makeRequest(url, headers, body, requestType, callback)
        testDispatcher.scheduler.advanceUntilIdle()
        verify(exactly = 0) { callback(any()) }
    }
}
