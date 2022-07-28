package com.example.triviaapp.features.quiz.data

import com.example.triviaapp.features.quiz.domain.QuizRepository
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mock


class QuizRepositoryImplTest{

    @Mock
    lateinit var quizNetworkService: QuizNetworkService

    private val quizRepositoryImpl = QuizRepositoryImpl(quizNetworkService)

    @Test
    fun `should get quiz list`(){
        // arrange
        val amount = 1

        // act
        val quiz = quizRepositoryImpl.getQuizzes(amount)

        // assert
        assertEquals(1, quiz.)
    }
}