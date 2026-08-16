package com.google.wallpaperapp.data.utils

import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.http.HttpStatusCode
import org.jetbrains.compose.resources.StringResource
import wallpaperapp.composeapp.generated.resources.Res
import wallpaperapp.composeapp.generated.resources.error_no_connection
import wallpaperapp.composeapp.generated.resources.error_rate_limited
import wallpaperapp.composeapp.generated.resources.error_server
import wallpaperapp.composeapp.generated.resources.error_timeout
import wallpaperapp.composeapp.generated.resources.error_unauthorized
import wallpaperapp.composeapp.generated.resources.something_went_wrong

/**
 * Turns a network failure into something a person can act on.
 *
 * Without this the UI shows whatever `Throwable.message` happens to be, which for a Pexels error
 * response is a kotlinx-serialization complaint about missing fields -- accurate, and completely
 * unhelpful, since the real problem is the request was rejected.
 */
fun Throwable.toUserMessage(): StringResource = when {
    this is ClientRequestException -> when (response.status) {
        HttpStatusCode.TooManyRequests -> Res.string.error_rate_limited
        HttpStatusCode.Unauthorized, HttpStatusCode.Forbidden -> Res.string.error_unauthorized
        else -> Res.string.something_went_wrong
    }

    this is ServerResponseException -> Res.string.error_server

    this is HttpRequestTimeoutException ||
        this is ConnectTimeoutException ||
        this is SocketTimeoutException -> Res.string.error_timeout

    // Anything left that is not our own bug is almost always the network being unreachable.
    this is RuntimeException && this::class.simpleName?.contains("Unresolved") == true ->
        Res.string.error_no_connection

    else -> Res.string.something_went_wrong
}
