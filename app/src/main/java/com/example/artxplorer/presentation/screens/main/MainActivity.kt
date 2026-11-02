package com.example.artxplorer.presentation.screens.main

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import com.example.artxplorer.data.remote.api.HomePageArtworks
import com.example.artxplorer.data.remote.api.SearchResult
import com.example.artxplorer.data.remote.dto.toDomain
import com.example.artxplorer.databinding.ActivityHomepageBinding
import com.example.artxplorer.databinding.AlertDialogDeleteSavedArtworkBinding
import com.example.artxplorer.databinding.DialogSearchArtworkMetadataBinding
import com.example.artxplorer.domain.model.Artwork
import com.example.artxplorer.presentation.screens.adapters.ArtworkAdapter
import com.example.artxplorer.presentation.screens.adapters.SearchArtworkResultAdapter
import com.example.artxplorer.presentation.screens.detail.ArtworkDetailsActivity
import com.example.artxplorer.presentation.screens.saved.SavedArtworksActivity
import com.example.artxplorer.presentation.screens.settings.SettingsActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomepageBinding
    private val viewModel: MainViewModel by viewModel()
    private lateinit var adapterOne: ArtworkAdapter
    private lateinit var adapterTwo: ArtworkAdapter
    private lateinit var adapterThree: ArtworkAdapter

    private lateinit var searchAdapter: SearchArtworkResultAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // search
        setupSearch()
        searchAdapter = SearchArtworkResultAdapter()
        binding.rvSearchResults.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
            adapter = searchAdapter
        }
        searchAdapter.setCallback { goArtworkDetailsActivity(Artwork(id = it.id)) }
        searchAdapter.setInfoCallback { showSearchArtworkResultMetadata(it) }

        // main content
        adapterOne = ArtworkAdapter()
        setupAdapter(binding.artworksRecyclerViewOne, adapterOne)
        adapterOne.setCallback { goArtworkDetailsActivity(it) }

        adapterTwo = ArtworkAdapter()
        setupAdapter(binding.artworksRecyclerViewTwo, adapterTwo)
        adapterTwo.setCallback { goArtworkDetailsActivity(it) }

        adapterThree = ArtworkAdapter()
        setupAdapter(binding.artworksRecyclerViewThree, adapterThree)
        adapterThree.setCallback { goArtworkDetailsActivity(it) }

        setupActivityCallbacks()
        setupBackPressHandling()
        observeViewModel()

        viewModel.loadHomePage()
    }

    private fun setupAdapter(
        recyclerView: RecyclerView,
        artworkAdapter: ArtworkAdapter,
        orientation: Int = RecyclerView.HORIZONTAL
    ) {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, orientation, false)
            adapter = artworkAdapter
        }
        val helper = PagerSnapHelper()
        helper.attachToRecyclerView(recyclerView)
    }

    private fun goArtworkDetailsActivity(artwork: Artwork) {
        val context = binding.root.context
        val intent = Intent(context, ArtworkDetailsActivity::class.java).apply {
            putExtra("ARTWORK_ID", artwork.id)
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        context.startActivity(intent)

    }

    private fun showSearchArtworkResultMetadata(artwork: SearchResult) {
        val dialogBinding = DialogSearchArtworkMetadataBinding.inflate(layoutInflater)
        dialogBinding.tvTitle.text = artwork.title
        dialogBinding.tvAltText.text = artwork.thumbnail?.altText
        dialogBinding.tvSearchScore.text = "${artwork.score} Points"

        val dialog =
            MaterialAlertDialogBuilder(this)
                .setView(dialogBinding.root)
                .setCancelable(true)
                .create()

        dialogBinding.btnClose.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }

    private fun goSavedArtworksActivity() {
        val context = binding.root.context
        val intent = Intent(context, SavedArtworksActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        context.startActivity(intent)
    }

    private fun goSettingsActivity() {
        val context = binding.root.context
        val intent = Intent(context, SettingsActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        context.startActivity(intent)
    }

    private fun observeViewModel() {
        viewModel.homepage.observe(this) { homepage ->
            val data = homepage.data.map { it.toDomain() }
            val chunks = data.chunked(data.size.coerceAtLeast(1).floorDiv(3).coerceAtLeast(1))

            adapterOne.submitList(chunks.getOrElse(0) { emptyList() })
            adapterTwo.submitList(chunks.getOrElse(1) { emptyList() })
            adapterThree.submitList(chunks.getOrElse(2) { emptyList() })

            setupUI(homepage)
        }
        viewModel.searchArtworkResults.observe(this) {
            val artworksList = it.data
            searchAdapter.submitList(artworksList)
        }

        viewModel.randomArtwork.observe(this) { artwork ->
            setupRandomArtwork(artwork)
        }

        viewModel.loading.observe(this) { isLoading ->
            // the swipe spinner is already visible during pull-to-refresh, so hide the linear bar
            binding.progressBar.visibility =
                if (isLoading && !binding.swipeRefresh.isRefreshing) View.VISIBLE else View.GONE
            if (!isLoading) binding.swipeRefresh.isRefreshing = false
        }

        viewModel.error.observe(this) { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupActivityCallbacks() {
        binding.btnGoToSaved.setOnClickListener {
            goSavedArtworksActivity()
        }
        binding.btnGoToSetting.setOnClickListener {
            goSettingsActivity()
        }
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refreshHomePage()
        }
    }

    private fun setupBackPressHandling() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.searchView.isShowing) {
                    binding.searchView.hide()
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }

    private fun setupSearch() {
        binding.searchView.setupWithSearchBar(binding.seachBar)
        binding.searchView.editText.setOnEditorActionListener { v, actionId, event ->
            val query = binding.searchView.text.toString()
            Log.d("MainActivity", "Query String: $query")
            viewModel.search(query)
            false
        }
    }

    private fun setupRandomArtwork(artwork: Artwork) {
        Log.d("MainActivity", "setting up random artwork")
        val imageUrl = "https://www.artic.edu/iiif/2/${artwork.imageId}/full/843,/0/default.jpg"
        binding.randomArtworkTitle.text = artwork.title
        binding.randomArtworkArtist.text = artwork.artist
        binding.randomArtworkYear.text = artwork.dateDisplay
        binding.randomArtworkDescription.text =
            Html.fromHtml(artwork.description, Html.FROM_HTML_MODE_COMPACT)
        binding.randomArtworkPreview.load(imageUrl)
        binding.randomArtworkPreview.setOnClickListener {
            goArtworkDetailsActivity(artwork)
        }
    }

    private fun setupUI(homePageArtworks: HomePageArtworks) {
        binding.versionLabel.text = buildString {
            append("API Version ")
            append(homePageArtworks.info.version)
        }



        binding.goNextPageBtn.isEnabled =
            homePageArtworks.pagination.current_page < homePageArtworks.pagination.total_pages
        binding.goNextPageBtn.setOnClickListener {
            viewModel.loadHomePage(homePageArtworks.pagination.current_page + 1)
        }

        binding.goPrevPageBtn.isEnabled = homePageArtworks.pagination.current_page > 1
        binding.goPrevPageBtn.setOnClickListener {
            viewModel.loadHomePage(homePageArtworks.pagination.current_page - 1)
        }
    }
}