package com.example.artxplorer.presentation.screens.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import coil3.request.error
import com.example.artxplorer.R
import com.example.artxplorer.databinding.ItemRecommendationBinding
import com.example.artxplorer.domain.model.Artwork

class RecommendationAdapter :
    ListAdapter<Artwork, RecommendationAdapter.ViewHolder>(DiffCallback()) {

    private var onArtworkClick: (Artwork) -> Unit = {}

    fun setCallback(onArtworkClick: (Artwork) -> Unit) {
        this.onArtworkClick = onArtworkClick
    }

    class ViewHolder(
        private val binding: ItemRecommendationBinding,
        private val onArtworkClick: (Artwork) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(artwork: Artwork) {
            binding.recommendationTitle.text = artwork.title
            binding.root.setOnClickListener { onArtworkClick(artwork) }

            val imageId = artwork.imageId
            if (imageId == null || imageId.lowercase() == "noimage") {
                binding.recommendationImage.load(R.drawable.no_image_fallback)
                return
            }
            // 400px is enough for this small card, no need to pull the full-size image
            binding.recommendationImage.load(
                "https://www.artic.edu/iiif/2/$imageId/full/400,/0/default.jpg"
            ) {
                error(R.drawable.no_image_fallback)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecommendationBinding.inflate(
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
