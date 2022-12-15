package com.jamiltondamasceno.projetonetflixapi.api

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        //Configurando TOKEN no cabeçalho
        val requisicaoAtual = chain.request().newBuilder()
        val requisicao = requisicaoAtual.addHeader(
            "Authorization", "Bearer ${RetrofitInstance.TOKEN}"
        )

        return chain.proceed(requisicao.build())
    }
}