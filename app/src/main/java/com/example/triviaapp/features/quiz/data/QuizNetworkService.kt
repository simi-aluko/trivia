package com.example.triviaapp.features.quiz.data

import com.example.triviaapp.features.quiz.QuizModel
import retrofit2.Call
import retrofit2.http.GET

interface QuizNetworkService {
    @GET
    fun getQuiz(amount: Int): Call<List<QuizModel>>
}