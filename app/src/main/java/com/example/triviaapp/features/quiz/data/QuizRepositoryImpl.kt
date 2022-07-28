package com.example.triviaapp.features.quiz.data

import com.example.triviaapp.core.Resource
import com.example.triviaapp.features.quiz.domain.QuizEntity
import com.example.triviaapp.features.quiz.domain.QuizRepository
import kotlinx.coroutines.flow.Flow

class QuizRepositoryImpl(quizNetworkService: QuizNetworkService) : QuizRepository {
    override fun getQuizzes(amount: Int): Flow<Resource<List<QuizEntity>>> {
        TODO("Not yet implemented")
    }

    override suspend fun saveQuizToDB(quiz: QuizEntity) {}
}