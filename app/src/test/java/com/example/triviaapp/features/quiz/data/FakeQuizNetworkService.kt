package com.example.triviaapp.features.quiz.data

import com.example.triviaapp.features.quiz.QuizModel
import retrofit2.Call

class FakeQuizNetworkService: QuizNetworkService {
    override fun getQuiz(amount: Int): Call<List<QuizModel>> {
        TODO("Not yet implemented")
    }
}