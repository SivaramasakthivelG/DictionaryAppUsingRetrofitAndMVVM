package com.example.dictionary.data.repository

import com.example.dictionary.data.remote_model.DictionaryResponse
import com.example.dictionary.data.remote.ApiService
import com.example.dictionary.data.remote_model.error.ErrorMessage
import com.google.gson.Gson
import javax.inject.Inject

class DictionaryRepo @Inject constructor(private val apiService: ApiService) {

    suspend fun getMeaning(word: String): Result<DictionaryResponse>{
        val response = apiService.getMeaning(word)
        if (response.isSuccessful){
            return  Result.success(response.body()!!)
        }else{
            val errorMessage = response.errorBody()?.string()
            val obj  = Gson().fromJson(errorMessage, ErrorMessage::class.java)
            return  Result.failure(Exception(obj.message))
        }
    }

}