package com.breakingbad.borislav.retrofit

import com.breakingbad.borislav.data.Character
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers


interface BreakingBadInterface {

    @Headers("Content-Type: application/json")
    @GET("api/characters")
    fun getCharacters(): Call<List<Character>>

    companion object {

        var BASE_URL = "https://breakingbadapi.com/"

        fun create(): BreakingBadInterface {
            val gson = GsonBuilder()
                .setLenient()
                .create()
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(BreakingBadInterface::class.java)

        }

    }
}