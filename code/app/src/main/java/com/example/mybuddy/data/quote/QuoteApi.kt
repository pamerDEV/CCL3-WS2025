package com.example.mybuddy.data.quote

import retrofit2.http.GET

interface QuoteApi {
    @GET("random")
    suspend fun getRandomQuote(): List<QuoteDto>
}