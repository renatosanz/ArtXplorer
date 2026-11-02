package com.example.artxplorer.presentation.screens.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import com.example.artxplorer.databinding.ItemSavedArtworkBinding
import com.example.artxplorer.domain.model.Artwork

class SavedArtworkAdapter() : RecyclerView.Adapter<SavedArtworkAdapter.ViewHolder>() {
    private var savedArtworks: List<Artwork>? = null
    private var onArtworkClick: (Artwork) -> Unit = {}
    private var onArtworkDeleteClick: (Artwork) -> Unit = {}

    fun setSavedArtworks(artworks: List<Artwork>) {
        this.savedArtworks = artworks
        notifyDataSetChanged()
    }

    fun setCallback(onArtworkClick: (Artwork) -> Unit) {
        this.onArtworkClick = onArtworkClick
    }

    fun setDeleteCallback(onArtworkDeleteClick: (Artwork) -> Unit) {
        this.onArtworkDeleteClick = onArtworkDeleteClick
    }

    class ViewHolder(
        private val binding: ItemSavedArtworkBinding,
        private val onArtworkClick: (Artwork) -> Unit,
        private val onArtworkDeleteClick: (Artwork) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(artwork: Artwork) {
            Log.d("SavedArtworkAdapter", "Binding data: $artwork")
            val imageUrl = "https://www.artic.edu/iiif/2/${artwork.imageId}/full/843,/0/default.jpg"

            binding.tvSavedTitle.text = artwork.title
            binding.tvSavedYear.text = artwork.dateDisplay
            binding.tvSavedArtist.text = artwork.artist
            binding.ivSavedPreview.load(imageUrl)
            binding.root.setOnClickListener { onArtworkClick(artwork) }
            binding.deleteSavedBtn.setOnClickListener {
                onArtworkDeleteClick(artwork)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ViewHolder {
        val binding = ItemSavedArtworkBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding, onArtworkClick,onArtworkDeleteClick)
    }

    override fun onBindViewHolder(
        holder: ViewHolder, position: Int
    ) {
        savedArtworks?.let { holder.bind(it[position]) }
    }

    override fun getItemCount(): Int = savedArtworks?.size ?: 0
}