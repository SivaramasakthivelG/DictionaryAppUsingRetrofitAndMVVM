package com.example.dictionary.data.remote

import com.example.dictionary.data.DictionaryResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

//https://api.dictionaryapi.dev/api/v2/entries/en/

interface ApiService {

    @GET("api/v2/entries/en/{word}") // we want end point where the api get the data from the http
    //the word is changing as per input so we use path
    suspend fun getMeaning(@Path("word") word: String): Response<DictionaryResponse>


}