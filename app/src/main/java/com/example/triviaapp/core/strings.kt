package com.example.triviaapp.core

object strings {
    val numbersBaseUrl = "http://numbersapi.com"
    val numberTriviaErrorMessage = "Unable to find trivia"
    val databaseName = "number_trivia_database"
    val triviaSharedPreferencesName = "trivia_shared_preferences"
    val currentNumberTrivia = "currentNumberTrivia"
}

enum class NumberTriviaType {
    Number, Random, Math, Date
}