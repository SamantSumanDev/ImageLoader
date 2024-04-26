package com.samant.acharyaassignment.repository

import com.samant.acharyaassignment.api.ApiService
import okhttp3.RequestBody.Companion.toRequestBody

object MainRepository {

    suspend fun getMediaCoverages(
        limit: Int,
        api: ApiService
    ) =
        api.getMediaCoverages(
            limit
        )


}