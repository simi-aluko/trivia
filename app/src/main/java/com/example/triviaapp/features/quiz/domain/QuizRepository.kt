package com.example.triviaapp.features.quiz.domain

import com.example.triviaapp.core.Resource
import kotlinx.coroutines.flow.Flow

interface QuizRepository {
    fun getQuizzes(amount: Int): Flow<Resource<List<QuizEntity>>>
    suspend fun saveQuizToDB(quiz: QuizEntity)
}