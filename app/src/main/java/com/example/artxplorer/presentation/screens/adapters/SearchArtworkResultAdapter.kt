package com.example.artxplorer.presentation.screens.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import com.example.artxplorer.data.remote.api.SearchResult
import com.example.artxplorer.data.common.decodeBase64ToDrawable
import com.example.artxplorer.databinding.ItemSearchArtworkResultPreviewBinding


class SearchArtworkResultAdapter() :
    ListAdapter<SearchResult, SearchArtworkResultAdapter.ViewHolder>(DiffCallback()) {

    private var onArtworkClick: (SearchResult) -> Unit = {}
    private var onInfoBtnClick: (SearchResult) -> Unit = {}

    fun setCallback(onArtworkClick: (SearchResult) -> Unit) {
        this.onArtworkClick = onArtworkClick
    }

    fun setInfoCallback(onInfoBtnClick: (SearchResult) -> Unit) {
        this.onInfoBtnClick = onInfoBtnClick
    }

    class ViewHolder(
        private val binding: ItemSearchArtworkResultPreviewBinding,
        private val onArtworkClick: (SearchResult) -> Unit,
        private val onInfoBtnClick: (SearchResult) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(searchResult: SearchResult) {
            binding.title.text = searchResult.title
            binding.root.setOnClickListener { onArtworkClick(searchResult) }
            binding.btnShowMetadata.setOnClickListener { this.onInfoBtnClick(searchResult) }
            binding.artworkPreview.load(decodeBase64ToDrawable(searchResult.thumbnail?.lqip))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSearchArtworkResultPreviewBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding, onArtworkClick, onInfoBtnClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<SearchResult>() {
        override fun areItemsTheSame(oldItem: SearchResult, newItem: SearchResult) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: SearchResult, newItem: SearchResult) =
            oldItem == newItem
    }
}