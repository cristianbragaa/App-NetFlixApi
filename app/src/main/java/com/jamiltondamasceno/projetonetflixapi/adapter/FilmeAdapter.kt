package com.jamiltondamasceno.projetonetflixapi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jamiltondamasceno.projetonetflixapi.R
import com.jamiltondamasceno.projetonetflixapi.api.RetrofitInstance
import com.jamiltondamasceno.projetonetflixapi.databinding.ItemFilmeBinding
import com.jamiltondamasceno.projetonetflixapi.model.Filme
import com.squareup.picasso.Picasso

class FilmeAdapter(
    private val onClickAbrir: (Filme) -> Unit
): RecyclerView.Adapter<FilmeAdapter.FilmeViewHolder>() {

    private var listaFilmes = mutableListOf<Filme>()

    fun recuperarFilmes(listaFilmes: List<Filme>){
        this.listaFilmes.addAll(listaFilmes)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmeViewHolder {
        val binding = ItemFilmeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FilmeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilmeViewHolder, position: Int) {
        holder.bind(listaFilmes[position], onClickAbrir)
    }

    override fun getItemCount(): Int {
        return listaFilmes.size
    }

    inner class FilmeViewHolder(private val binding: ItemFilmeBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(filme: Filme, onClickAbrir: (Filme) -> Unit){

            binding.textTitulo.text = filme.title
            Picasso.get()
                .load(RetrofitInstance.IMAGE_BASE + "w780" + filme.backdrop_path)
                .error(R.drawable.capa)
                .into(binding.imgItemFilme)

            binding.clItem.setOnClickListener {
                onClickAbrir(filme)
            }
        }
    }
}