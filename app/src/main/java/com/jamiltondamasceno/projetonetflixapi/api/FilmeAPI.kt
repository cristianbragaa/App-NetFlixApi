package com.jamiltondamasceno.projetonetflixapi.api

import com.jamiltondamasceno.projetonetflixapi.model.Filme
import com.jamiltondamasceno.projetonetflixapi.model.FilmeResposta
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface FilmeAPI {

    @GET("movie/popular")
    suspend fun getFilmesPopulares(
        @Query("page") pagina: Int = 1
    ): Response<FilmeResposta>

    @GET("movie/latest")
    suspend fun getPosterPrincipal(): Response<Filme>
}