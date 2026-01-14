package com.example.mybuddy.data.quote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object QuoteApiProvider {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://zenquotes.io/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: QuoteApi = retrofit.create(QuoteApi::class.java)
}