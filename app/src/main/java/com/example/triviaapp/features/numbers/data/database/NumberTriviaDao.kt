package com.example.triviaapp.features.numbers.data.database

import androidx.room.*
import com.example.triviaapp.features.numbers.domain.entities.NumberTriviaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NumberTriviaDao {

    @Query("SELECT * FROM number_trivia_table")
    fun getAllTrivia(): Flow<List<NumberTriviaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrivia(trivia: NumberTriviaEntity)

    @Delete
    suspend fun deleteTrivia(trivia: NumberTriviaEntity)
}