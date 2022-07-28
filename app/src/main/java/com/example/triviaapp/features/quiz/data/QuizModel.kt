package com.example.triviaapp.features.quiz

data class QuizModel(
    val response_code: Int,
    val results: List<Result>
)

data class Result(
    val category: String,
    val correct_answer: String,
    val difficulty: String,
    val incorrect_answers: List<String>,
    val question: String,
    val type: String
)