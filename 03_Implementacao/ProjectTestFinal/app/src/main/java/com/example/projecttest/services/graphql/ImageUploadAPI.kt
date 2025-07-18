package com.example.projecttest.services.graphql

import com.example.projecttest.AppConfig
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ImageUploadAPI {
    @Multipart
    @POST(AppConfig.IMAGE_FOLDER_PATH)
    suspend fun uploadImage(@Part file: MultipartBody.Part): FileUploadResponse
}

data class FileUploadResponse(val filePath: String)