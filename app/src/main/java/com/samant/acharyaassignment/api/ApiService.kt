package com.samant.acharyaassignment.api


import com.samant.acharyaassignment.ui.Model.Coverage
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
interface ApiService {
        @GET("content/misc/media-coverages")
        fun getMediaCoverages(@Query("limit") limit: Int): Call<List<Coverage>>
}

