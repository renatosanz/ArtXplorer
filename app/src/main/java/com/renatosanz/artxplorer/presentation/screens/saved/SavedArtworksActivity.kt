package com.renatosanz.artxplorer.presentation.screens.saved

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.renatosanz.artxplorer.databinding.ActivitySavedArtworksBinding
import com.renatosanz.artxplorer.databinding.AlertDialogDeleteSavedArtworkBinding
import com.renatosanz.artxplorer.presentation.screens.adapters.SavedArtworkAdapter
import com.renatosanz.artxplorer.presentation.screens.detail.ArtworkDetailsActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class SavedArtworksActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySavedArtworksBinding
    private val viewModel: SavedArtworksViewModel by viewModel() // Koin lo inyecta solo
    private lateinit var adapter: SavedArtworkAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedArtworksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAdapter()
        observeViewModel()

        viewModel.loadSavedArtworks()
    }

    private fun setupAdapter() {
        adapter = SavedArtworkAdapter()
        binding.rvSavedArtworks.apply {
            layoutManager =
                LinearLayoutManager(
                    this@SavedArtworksActivity,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            adapter = this@SavedArtworksActivity.adapter
        }
    }

    private fun observeViewModel() {
        viewModel.savedArtworks.observe(this) { artworks ->
            Log.d("SavedArtworksActivity", "binding data to ui (artworks: $artworks)")
            adapter.setSavedArtworks(artworks)
            adapter.setCallback { artwork ->
                val context = binding.root.context
                val intent = Intent(context, ArtworkDetailsActivity::class.java).apply {
                    putExtra("ARTWORK_ID", artwork.id)
                    putExtra("IS_SAVED", true)
                    addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                }
                context.startActivity(intent)
            }
            adapter.setDeleteCallback { artwork ->
                val dialogBinding = AlertDialogDeleteSavedArtworkBinding.inflate(layoutInflater)
                dialogBinding.titleAlert.text = "Delete Artwork <<${artwork.title}>>?"

                val dialog = MaterialAlertDialogBuilder(this)
                    .setView(dialogBinding.root)
                    .setCancelable(true)
                    .create()

                dialogBinding.btnDelete.setOnClickListener {
                    viewModel.deleteArtwork(artwork.toEntity())
                    dialog.dismiss()
                    Toast.makeText(this, "Artwork deleted", Toast.LENGTH_SHORT).show()
                }

                dialogBinding.btnCancel.setOnClickListener {
                    dialog.dismiss()
                }

                dialog.show()
            }
        }

        viewModel.loading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(this) { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

}