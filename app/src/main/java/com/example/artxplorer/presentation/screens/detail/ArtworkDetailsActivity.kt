package com.example.artxplorer.presentation.screens.detail

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import coil3.load
import com.example.artxplorer.R
import com.example.artxplorer.data.local.entities.ArtworkEntity
import com.example.artxplorer.databinding.ActivityArtworkDetailsBinding
import com.example.artxplorer.databinding.AlertDialogDeleteSavedArtworkBinding
import com.example.artxplorer.presentation.screens.adapters.RecommendationAdapter
import com.example.artxplorer.presentation.screens.fullscreen.FullScreenViewerActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class ArtworkDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityArtworkDetailsBinding
    private val viewModel: ArtworkDetailsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("ArtworkDetails", "Starting ArtworkDetailsActivity")
        binding = ActivityArtworkDetailsBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val id = intent.getLongExtra("ARTWORK_ID", -1L)
        Log.d("ArtworkDetails", "id recibido $id")

        val recommendationAdapter = RecommendationAdapter()
        recommendationAdapter.setCallback { recommended ->
            startActivity(Intent(this, ArtworkDetailsActivity::class.java).apply {
                putExtra("ARTWORK_ID", recommended.id)
            })
        }
        binding.rvRecommendations.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvRecommendations.adapter = recommendationAdapter

        viewModel.artwork.observe(this) { artwork ->
            Log.d("ArtworkDetails", "Artwork received: $artwork")
            artwork?.let { setupUI(it) }
        }
        viewModel.recommendations.observe(this) { recommendations ->
            binding.tvRecommendationsTitle.isVisible = recommendations.isNotEmpty()
            binding.rvRecommendations.isVisible = recommendations.isNotEmpty()
            recommendationAdapter.submitList(recommendations)
        }
        viewModel.isSaved.observe(this) { isSaved ->
            isSaved.let {
                binding.fabSave.visibility = if (it) View.GONE else View.VISIBLE
                binding.fabDelete?.visibility = if (!it) View.GONE else View.VISIBLE
            }
        }

        viewModel.loading.observe(this) { loading ->
            Log.d("ArtworkDetails", "Loading: $loading")
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(this) { e ->
            Log.d("ArtworkDetails", "algo salio mal: $e")
            Toast.makeText(this, e, Toast.LENGTH_SHORT).show()
        }
        viewModel.msgToast.observe(this) { e ->
            Log.d("ArtworkDetails", "$e")
            Toast.makeText(this, e, Toast.LENGTH_SHORT).show()
        }


        viewModel.loadArtworkDetails(id)
    }

    private fun goToFullScreenViewer(imageUrl: String) {
        val context = binding.root.context
        val intent = Intent(context, FullScreenViewerActivity::class.java).apply {
            putExtra("IMAGE_URL", imageUrl)
        }
        startActivity(intent)
    }

    private fun setupUI(artwork: ArtworkEntity) {
        binding.tvDetailTitle.text = artwork.title
        binding.tvDetailArtist.text = artwork.artist
        binding.tvDetailDate.text = artwork.dateDisplay
        binding.tvDetailDescription.text =
            Html.fromHtml(artwork.description, Html.FROM_HTML_MODE_COMPACT)

        binding.ivDetailArtwork.visibility = View.VISIBLE

        binding.tvDetailMedium.text = "Medium: ${artwork.mediumDisplay}"
        binding.tvDetailOrigin.text = "Place: ${artwork.placeOfOrigin}"
        binding.tvDetailDimensions.text = "Dimensions: ${artwork.dimentions}"

        artwork.styleTitle?.takeIf { it.isNotBlank() }?.let {
            binding.tvDetailStyle.text = getString(R.string.style_format, it)
            binding.tvDetailStyle.isVisible = true
        }
        artwork.classificationTitles?.takeIf { it.isNotBlank() }?.let {
            binding.tvDetailClassification.text = getString(R.string.classification_format, it)
            binding.tvDetailClassification.isVisible = true
        }
        artwork.creditLine?.takeIf { it.isNotBlank() }?.let {
            binding.tvCreditLine.text = getString(R.string.credit_line_format, it)
            binding.tvCreditLine.isVisible = true
        }
        artwork.copyrightNotice?.takeIf { it.isNotBlank() }?.let {
            binding.tvCopyright.text = it
            binding.tvCopyright.isVisible = true
        }

        val artistName = artwork.artist.substringBefore('\n').trim()
        binding.tvRecommendationsTitle.text = if (artistName.isBlank()) {
            getString(R.string.more_artworks)
        } else {
            getString(R.string.more_by_artist, artistName)
        }

        // if no image id, then should disable artwork actions and display fallback image
        if (artwork.imageId?.lowercase().equals("noimage")) {
            binding.downloadArtworkBtn?.visibility = View.GONE
            binding.fabSave.visibility = View.GONE
            binding.goToFullScreenBtn.visibility = View.GONE
            binding.ivDetailArtwork.load(R.drawable.no_image_fallback)
            return
        }

        // else allow all common artwork actions
        binding.fabDelete?.setOnClickListener {
            val dialogBinding = AlertDialogDeleteSavedArtworkBinding.inflate(layoutInflater)
            dialogBinding.titleAlert.text = "Delete Artwork <<${artwork.title}>>?"

            val dialog = MaterialAlertDialogBuilder(this)
                .setView(dialogBinding.root)
                .setCancelable(true)
                .create()

            dialogBinding.btnDelete.setOnClickListener {
                viewModel.deleteLocally()
                finish()
                dialog.dismiss()
                Toast.makeText(this, "Artwork deleted", Toast.LENGTH_SHORT).show()
            }

            dialogBinding.btnCancel.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }

        val imageUrl = artwork.localImagePath
            ?: "https://www.artic.edu/iiif/2/${artwork.imageId}/full/843,/0/default.jpg"
        binding.ivDetailArtwork.load(imageUrl)
        binding.downloadArtworkBtn?.setOnClickListener {
            viewModel.saveArtworkToDevice(this@ArtworkDetailsActivity, imageUrl)
        }
        binding.fabSave.setOnClickListener {
            viewModel.saveArtworkLocal(this, imageUrl)
        }
        binding.goToFullScreenBtn.setOnClickListener {
            goToFullScreenViewer(imageUrl)
        }
    }
}