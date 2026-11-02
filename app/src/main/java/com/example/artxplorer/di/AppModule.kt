package com.example.artxplorer.di

import android.app.Application
import androidx.room.Room
import com.example.artxplorer.data.local.AppDatabase
import com.example.artxplorer.data.local.dao.ArtworkDao
import com.example.artxplorer.data.remote.api.ApiService
import com.example.artxplorer.data.repository.ArtworkRepository
import com.example.artxplorer.domain.repository.ArtworkRepositoryImpl
import com.example.artxplorer.domain.usecase.ClearSavedArtworksUseCase
import com.example.artxplorer.domain.usecase.DeleteArtworkLocalUseCase
import com.example.artxplorer.domain.usecase.GetArtworkDetailUseCase
import com.example.artxplorer.domain.usecase.GetHomePageUseCase
import com.example.artxplorer.domain.usecase.GetRandomArtworkUseCase
import com.example.artxplorer.domain.usecase.GetSavedArtworksUseCase
import com.example.artxplorer.domain.usecase.SaveArtworkLocalUseCase
import com.example.artxplorer.domain.usecase.SearchArtworkUseCase
import com.example.artxplorer.presentation.screens.detail.ArtworkDetailsViewModel
import com.example.artxplorer.presentation.screens.main.MainViewModel
import com.example.artxplorer.presentation.screens.saved.SavedArtworksViewModel
import com.example.artxplorer.presentation.screens.settings.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    single {
        Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.artic.edu/api/v1/").build()
    }
    single { get<Retrofit>().create(ApiService::class.java) }
    single<ArtworkRepository> { ArtworkRepositoryImpl(get(), get()) }
}

val domainModule = module {
    factory { GetHomePageUseCase(get()) }
    factory { GetArtworkDetailUseCase(get()) }
    factory { SaveArtworkLocalUseCase(get()) }
    factory { GetSavedArtworksUseCase(get()) }
    factory { DeleteArtworkLocalUseCase(get()) }
    factory { ClearSavedArtworksUseCase(get()) }
    factory { GetRandomArtworkUseCase(get()) }
    factory { SearchArtworkUseCase(get()) }
}

val presentationModule = module {
    viewModel { MainViewModel(get(), get(), get()) }
    viewModel { SavedArtworksViewModel(get(), get()) }
    viewModel { ArtworkDetailsViewModel(get(), get(), get()) }
    viewModel { SettingsViewModel(androidContext(), get()) }
}


fun provideDataBase(application: Application): AppDatabase =
    Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        "table_post"
    ).fallbackToDestructiveMigration(true).allowMainThreadQueries().build()

fun provideArtworkDao(postDataBase: AppDatabase): ArtworkDao = postDataBase.artworkDao()

val databaseModule = module {
    single { provideDataBase(get()) }
    single { provideArtworkDao(get()) }
}
