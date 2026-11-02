package com.example.artxplorer.presentation.screens.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import com.example.artxplorer.R
import com.example.artxplorer.databinding.ItemArtworkPreviewBinding
import com.example.artxplorer.domain.model.Artwork

class ArtworkAdapter() : ListAdapter<Artwork, ArtworkAdapter.ViewHolder>(DiffCallback()) {

    private var onArtworkClick: (Artwork) -> Unit = {}

    fun setCallback(onArtworkClick: (Artwork) -> Unit) {
        this.onArtworkClick = onArtworkClick
    }

    class ViewHolder(
        private val binding: ItemArtworkPreviewBinding,
        private val onArtworkClick: (Artwork) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(artwork: Artwork) {


            binding.root.setOnClickListener { onArtworkClick(artwork) }
            binding.title.text = artwork.title
            binding.artist.text = artwork.artist
            binding.year.text = artwork.dateDisplay

            // if no image id, then display fallback image
            if (artwork.imageId?.lowercase().equals("noimage")) {
                binding.artworkPreview.load(R.drawable.no_image_fallback)
                return
            }
            // else show preview
            val imageUrl = "https://www.artic.edu/iiif/2/${artwork.imageId}/full/843,/0/default.jpg"
            binding.artworkPreview.load(imageUrl)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemArtworkPreviewBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding, onArtworkClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<Artwork>() {
        override fun areItemsTheSame(oldItem: Artwork, newItem: Artwork) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Artwork, newItem: Artwork) =
            oldItem == newItem
    }
}