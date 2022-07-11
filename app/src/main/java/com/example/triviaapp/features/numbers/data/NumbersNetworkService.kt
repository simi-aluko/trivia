package com.example.triviaapp.features.numbers.data

import com.example.triviaapp.features.numbers.data.models.DateTriviaModel
import com.example.triviaapp.features.numbers.data.models.NumberTriviaModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface NumbersNetworkService {
    @GET("{number}/math?json")
    fun getMathNumberTrivia(@Path("number") number: Int): Call<NumberTriviaModel>

    @GET("random?json")
    fun getRandomNumberTrivia(): Call<NumberTriviaModel>

    @GET("{number}?json")
    fun getNumberTrivia(@Path("number") number: Int): Call<NumberTriviaModel>

    @GET("{month}/{day}/date?json")
    fun getDateTrivia(@Path("month") month: Int, @Path("day") day: Int): Call<DateTriviaModel>
}