package com.jamiltondamasceno.projetonetflixapi.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jamiltondamasceno.projetonetflixapi.R
import com.jamiltondamasceno.projetonetflixapi.adapter.FilmeAdapter
import com.jamiltondamasceno.projetonetflixapi.api.RetrofitInstance
import com.jamiltondamasceno.projetonetflixapi.databinding.ActivityMainBinding
import com.jamiltondamasceno.projetonetflixapi.model.Filme
import com.jamiltondamasceno.projetonetflixapi.model.FilmeResposta
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val TAG = "info_filme"
    private var job: Job? = null
    private var paginaAtual = 1
    private var adapterFilmes: FilmeAdapter? = null
    private var gridLayoutMenager: GridLayoutManager? = null

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val filmeAPI by lazy { RetrofitInstance.filmeApi }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        adapterFilmes = FilmeAdapter { filme ->
            val intent = Intent(this, DetalhesActivity::class.java)
            intent.putExtra("filme", filme)
            startActivity(intent)
        }
        binding.rvPopulares.adapter = adapterFilmes
        gridLayoutMenager = GridLayoutManager(this,2)
        binding.rvPopulares.layoutManager = gridLayoutMenager

        binding.btnCarregarDados.visibility = View.GONE
        binding.rvPopulares.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                // Esconder FAB no final da lista de filmes
                val totalItens = recyclerView.adapter?.itemCount //20 itens
                val ultimoItem = gridLayoutMenager?.findLastVisibleItemPosition()

                if (totalItens != null && ultimoItem != null){
                    if (totalItens - 1 == ultimoItem) {
                        binding.fabAdicionar.hide()
                    } else {
                        binding.fabAdicionar.show()
                    }
                }

                // Carregamento "infinito"
                val retorno = recyclerView.canScrollVertically(1)
                if (!retorno){  //true -> está descendo, false -> chegou no final
                    binding.btnCarregarDados.visibility = View.VISIBLE
                    binding.btnCarregarDados.setOnClickListener {
                        recuperarFilmesPopularesProximaPagina()
                    }
                }else{
                    binding.btnCarregarDados.visibility = View.GONE
                }
            }
        })
    }

    private fun recuperarPosterPrincipal() {

        CoroutineScope(Dispatchers.IO).launch {
            var resposta: Response<Filme>? = null

            try {
                resposta = filmeAPI.getPosterPrincipal()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            if (resposta != null) {
                if (resposta.isSuccessful) {
                    val filmePoster = resposta.body()
                    withContext(Dispatchers.Main) {
                        if (filmePoster != null) {
                            Picasso.get()
                                .load(RetrofitInstance.IMAGE_BASE + "w780" + filmePoster.poster_path)
                                .error(R.drawable.capa)
                                .into(binding.imgCapa)
                        } else {
                            binding.imgCapa.setImageResource(R.drawable.capa)
                        }
                    }
                } else {
                    Log.i(TAG,
                        "Erro de resposta do servidor - CODE:${resposta.code()} - MESSAGE:${resposta.message()} ")
                }
            }
        }
    }

    private fun recuperarFilmesPopulares(pagina: Int = 1) {

        job = CoroutineScope(Dispatchers.IO).launch {
            var resposta: Response<FilmeResposta>? = null

            try {
                Log.i(TAG, "Página atual: $paginaAtual")
                resposta = filmeAPI.getFilmesPopulares(pagina)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.i(TAG, "Erro ao recuperar filmes populares")
            }

            if (resposta != null) {
                if (resposta.isSuccessful) {

                    val listaFilmes = resposta.body()?.filmes
                    if (listaFilmes != null) {
                        withContext(Dispatchers.Main) {
                            adapterFilmes?.recuperarFilmes(listaFilmes)
                        }
                    }
                } else {
                    Log.i(TAG, "Erro na requisição - código erro: ${resposta.code()}")
                }
            }
        }
    }

    private fun recuperarFilmesPopularesProximaPagina(){
        if (paginaAtual < 1000){
            paginaAtual++
            recuperarFilmesPopulares(paginaAtual)
        }
    }

    override fun onStart() {
        super.onStart()
        recuperarFilmesPopulares()
        recuperarPosterPrincipal()
    }

    override fun onStop() {
        super.onStop()
        job?.cancel()
    }
}