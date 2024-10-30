package com.example.dictionary.di

import com.example.dictionary.data.remote.ApiService
import com.example.dictionary.data.repository.DictionaryRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object HiltModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://api.dictionaryapi.dev/")
            .addConverterFactory(GsonConverterFactory.create()) // this is to convert the json to kotlin data object
            .build()
    }

    // in normal case we directly create this with val apiService = retrofit.create(ApiService::class.java), but we simplify things using hilt
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    fun provideRepo(apiService: ApiService): DictionaryRepo {
        return DictionaryRepo(apiService)
    }

}