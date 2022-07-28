package com.example.triviaapp.features.numbers.domain

import com.example.triviaapp.core.Resource
import com.example.triviaapp.features.numbers.domain.entities.NumberTriviaEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface NumbersRepository {
    suspend fun getMathNumberTrivia(number: Int): Flow<NumberTriviaEntity?>
    suspend fun getRandomNumberTrivia(): Flow<NumberTriviaEntity?>
    suspend fun getNumberTrivia(number: Int): Flow<NumberTriviaEntity?>
    suspend fun getDateTrivia(month: Int, day: Int): Flow<NumberTriviaEntity?>
    suspend fun saveTriviaToDB(trivia: NumberTriviaEntity)
    suspend fun deleteTriviaFromDB(trivia: NumberTriviaEntity)
    fun getAllTrivia(): Flow<List<NumberTriviaEntity>>
}