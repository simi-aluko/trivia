package com.example.triviaapp.features.quiz.domain

class QuizEntity(
    val category: String,
    val correct_answer: String,
    val incorrect_answers: List<String>,
    val question: String,
    val favorite: Boolean
)