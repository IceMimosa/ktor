package io.ktor.client.request.forms

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.content.*


/**
 * Submit [formData] request.
 *
 * If [urlEncoded] specified encode [formData] in url parameters and use [HttpMethod.Get] for the request.
 * Otherwise send [HttpMethod.Post] request with [formData] encoded in body.
 *
 * [formData] encoded using application/x-www-form-urlencoded format.
 */
suspend inline fun <reified T> HttpClient.submitForm(
    formData: Parameters = Parameters.Empty,
    urlEncoded: Boolean = false,
    block: HttpRequestBuilder.() -> Unit = {}
): T = request {
    if (urlEncoded) {
        method = HttpMethod.Get
        url.parameters.appendAll(formData)
    } else {
        method = HttpMethod.Post
        body = FormDataContent(formData)
    }

    block()
}

/**
 * Send [HttpMethod.Post] request with [parts] encoded in body.
 * [parts] encoded using multipart/form-data format.
 * https://tools.ietf.org/html/rfc2045
 */
suspend inline fun <reified T> HttpClient.submitFormWithBinaryData(
    formData: List<PartData>,
    block: HttpRequestBuilder.() -> Unit = {}
): T = request {
    method = HttpMethod.Post
    body = MultiPartFormDataContent(formData)
    block()
}

/**
 * Submit [formData] request.
 *
 * If [urlEncoded] specified encode [formData] in url parameters and use [HttpMethod.Get] for the request.
 * Otherwise send [HttpMethod.Post] request with [formData] encoded in body.
 *
 * [formData] encoded using application/x-www-form-urlencoded format.
 */
suspend inline fun <reified T> HttpClient.submitForm(
    scheme: String = "http",
    host: String = "localhost",
    port: Int = 80,
    path: String = "/",
    formData: Parameters = Parameters.Empty,
    urlEncoded: Boolean = false,
    block: HttpRequestBuilder.() -> Unit = {}
): T = submitForm(formData, urlEncoded) {
    url(scheme, host, port, path)
    apply(block)
}

/**
 * Send [HttpMethod.Post] request with [formData] encoded in body.
 * [formData] encoded using multipart/form-data format.
 * https://tools.ietf.org/html/rfc2045
 */
suspend inline fun <reified T> HttpClient.submitFormWithBinaryData(
    scheme: String = "http",
    host: String = "localhost",
    port: Int = 80,
    path: String = "/",
    formData: List<PartData> = emptyList(),
    block: HttpRequestBuilder.() -> Unit = {}
): T = submitFormWithBinaryData(formData) {
    url(scheme, host, port, path)
    apply(block)
}
