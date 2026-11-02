package com.example.artxplorer.presentation.screens.fullscreen

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import coil3.load
import com.example.artxplorer.databinding.ActivityFullscreenViewerBinding

class FullScreenViewerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFullscreenViewerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFullscreenViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageUrl = intent.getStringExtra("IMAGE_URL") ?: return

        binding.photoView.load(imageUrl) {
            listener(
                onSuccess = { _, _ -> binding.progressBar.visibility = View.GONE },
                onError = { _, _ -> binding.progressBar.visibility = View.GONE })
        }
    }
}