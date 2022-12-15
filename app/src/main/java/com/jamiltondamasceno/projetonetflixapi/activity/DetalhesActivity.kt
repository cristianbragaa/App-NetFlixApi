package com.jamiltondamasceno.projetonetflixapi.activity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.jamiltondamasceno.projetonetflixapi.api.RetrofitInstance
import com.jamiltondamasceno.projetonetflixapi.databinding.ActivityDetalhesBinding
import com.jamiltondamasceno.projetonetflixapi.model.Filme
import com.squareup.picasso.Picasso

class DetalhesActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityDetalhesBinding.inflate( layoutInflater )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( binding.root )

        val filme: Filme?
        val bundle = intent.extras
        if (bundle != null){
            filme = bundle.getParcelable("filme")
            if (filme != null){
                Picasso.get()
                    .load(RetrofitInstance.IMAGE_BASE + "w780" + filme.poster_path)
                    .into(binding.imgPoster)

                binding.textFilmeTitulo.text = filme.title
            }
        }
    }
}