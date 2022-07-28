package com.example.triviaapp.features.numbers.domain.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.triviaapp.core.NumberTriviaType

@Entity(tableName = "number_trivia_table")
data class NumberTriviaEntity(@PrimaryKey @ColumnInfo(name = "number") val number: Int,
                              @ColumnInfo(name = "text") val text: String,
                              @ColumnInfo(name = "type") val type: NumberTriviaType)