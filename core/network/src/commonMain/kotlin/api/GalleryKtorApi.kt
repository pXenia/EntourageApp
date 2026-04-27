package com.entourageapp.core.network.api

import com.entourageapp.core.network.dto.ImageDto
import com.entourageapp.core.network.dto.ImageUpdateDto
import com.entourageapp.core.network.dto.ImageUploadedDto
import com.entourageapp.core.network.dto.MessageDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders

class GalleryKtorApi(private val client: HttpClient) : GalleryApi {

    override suspend fun getImages(projectId: Int, roomId: Int?): List<ImageDto> =
        client.get("projects/$projectId/images/") {
            roomId?.let { parameter("room_id", it) }
        }.body()

    override suspend fun uploadImage(
        projectId: Int,
        fileBytes: ByteArray,
        fileName: String,
        mimeType: String,
        roomId: Int?,
        note: String?
    ): ImageUploadedDto =
        client.post("projects/$projectId/images/") {
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append(
                            key = "file",
                            value = fileBytes,
                            headers = Headers.build {
                                append(HttpHeaders.ContentType, mimeType)
                                append(
                                    HttpHeaders.ContentDisposition,
                                    "form-data; name=\"file\"; filename=\"$fileName\""
                                )
                            }
                        )
                        roomId?.let { append("room_id", it.toString()) }
                        note?.let { append("note", it) }
                    }
                )
            )
        }.body()

    override suspend fun deleteImage(projectId: Int, imageId: Int): MessageDto =
        client.delete("projects/$projectId/images/$imageId").body()

    override suspend fun updateImage(
        projectId: Int,
        imageId: Int,
        note: String?,
        roomId: Int?
    ): MessageDto =
        client.patch("projects/$projectId/images/$imageId") {
            setBody(ImageUpdateDto(note = note, roomId = roomId))
        }.body()
}