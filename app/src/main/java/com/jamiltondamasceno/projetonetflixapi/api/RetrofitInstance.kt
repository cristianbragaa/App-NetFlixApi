package com.jamiltondamasceno.projetonetflixapi.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {

    private const val BASE_URL = "https://api.themoviedb.org/3/"
    const val IMAGE_BASE = "https://image.tmdb.org/t/p/"
    const val API_KEY = "d3acd48617b565fdaaf7c66aef6408b5"
    const val TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJkM2FjZDQ4NjE3YjU2NWZkYWFmN2M2NmFlZjY0MDhiNSIsInN1YiI6IjYxMWRiNjI5Y2M5NjgzMDA3ZTEzNjgyMCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.T9bnJuboTJsay-yLefO167YfPvhX9Wf8tyn-dg5gg44"

    private val okHttpClient = OkHttpClient.Builder()
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .addInterceptor(AuthInterceptor())
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    val filmeApi: FilmeAPI = retrofit.create(FilmeAPI::class.java)

}