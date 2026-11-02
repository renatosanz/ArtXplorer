package com.renatosanz.artxplorer.presentation.screens.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import coil3.request.error
import coil3.request.placeholder
import com.renatosanz.artxplorer.R
import com.renatosanz.artxplorer.data.remote.api.SearchResult
import com.renatosanz.artxplorer.data.common.decodeBase64ToDrawable
import com.renatosanz.artxplorer.databinding.ItemSearchArtworkResultPreviewBinding


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
            binding.artist.text = searchResult.artistTitle
            binding.artist.isVisible = !searchResult.artistTitle.isNullOrBlank()
            binding.date.text = searchResult.dateDisplay
            binding.date.isVisible = !searchResult.dateDisplay.isNullOrBlank()

            binding.root.setOnClickListener { onArtworkClick(searchResult) }
            binding.btnShowMetadata.setOnClickListener { onInfoBtnClick(searchResult) }

            val imageId = searchResult.imageId
            val lqip = decodeBase64ToDrawable(searchResult.thumbnail?.lqip)
            if (imageId.isNullOrBlank()) {
                // Old cached results only carry the tiny base64 preview.
                binding.artworkPreview.load(lqip ?: R.drawable.no_image_fallback)
                return
            }
            // 400px is enough for this small card, no need to pull the full-size image.
            // The lqip covers the load and stays if the image request fails.
            binding.artworkPreview.load(
                "https://www.artic.edu/iiif/2/$imageId/full/400,/0/default.jpg"
            ) {
                if (lqip != null) {
                    placeholder(lqip)
                    error(lqip)
                } else {
                    error(R.drawable.no_image_fallback)
                }
            }
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
