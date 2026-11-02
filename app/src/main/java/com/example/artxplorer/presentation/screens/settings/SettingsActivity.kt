package com.example.artxplorer.presentation.screens.settings

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.artxplorer.R
import com.example.artxplorer.databinding.ActivitySettingsBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

// shortcut: opens the Buy Me a Coffee homepage, replace with your own profile URL
private const val BUY_ME_A_COFFEE_URL = "https://www.buymeacoffee.com/renatosanz"
private const val GITHUB_URL = "https://github.com/renatosanz/ArtXplorer"
private const val PORTFOLIO_URL = "https://portfolio.riprtx.uk/"

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }

        setupThemeOptions()
        setupClearCache()
        setupAbout()
        observeViewModel()
    }

    private fun setupThemeOptions() {
        val checkedId = when (ThemePreferences.get(this)) {
            AppCompatDelegate.MODE_NIGHT_NO -> R.id.themeLight
            AppCompatDelegate.MODE_NIGHT_YES -> R.id.themeDark
            else -> R.id.themeSystem
        }
        binding.themeGroup.check(checkedId)
        binding.themeGroup.setOnCheckedChangeListener { _, id ->
            val mode = when (id) {
                R.id.themeLight -> AppCompatDelegate.MODE_NIGHT_NO
                R.id.themeDark -> AppCompatDelegate.MODE_NIGHT_YES
                else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }
            if (mode != ThemePreferences.get(this)) ThemePreferences.set(this, mode)
        }
    }

    private fun setupClearCache() {
        binding.btnClearCache.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle(R.string.clear_cache_confirm_title)
                .setMessage(R.string.clear_cache_confirm_message)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(R.string.clear_cache) { _, _ -> viewModel.clearSavedArtworks() }
                .show()
        }
    }

    private fun setupAbout() {
        binding.tvVersion.text = getString(
            R.string.version_format,
            packageManager.getPackageInfo(packageName, 0).versionName ?: ""
        )
        binding.btnGithub.setOnClickListener {
            openLink(GITHUB_URL)
        }
        binding.btnPortfolio.setOnClickListener {
            openLink(PORTFOLIO_URL)
        }
        binding.btnBuyCoffee.setOnClickListener {
            openLink(BUY_ME_A_COFFEE_URL)
        }
    }

    private fun openLink(uri: String) {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uri)))
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, R.string.no_app_found, Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeViewModel() {
        viewModel.loading.observe(this) { isLoading ->
            binding.clearCacheProgress.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.btnClearCache.isEnabled = !isLoading
        }

        viewModel.cleared.observe(this) {
            Toast.makeText(this, R.string.clear_cache_done, Toast.LENGTH_SHORT).show()
        }

        viewModel.error.observe(this) { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }
}
