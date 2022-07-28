package com.example.triviaapp.features.numbers.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.triviaapp.core.NumberTriviaType.Number
import com.example.triviaapp.features.numbers.domain.entities.NumberTriviaEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [NumberTriviaEntity::class], version = 1, exportSchema = false)
abstract class TriviaDatabase : RoomDatabase(){
    abstract fun numberTriviaDao(): NumberTriviaDao

    companion object {
        private var instance: TriviaDatabase? = null

        fun getDatabase(context: Context, coroutineScope: CoroutineScope, name: String):
                TriviaDatabase {
            return instance ?: synchronized(this){
                val _instance = Room
                    .databaseBuilder(context, TriviaDatabase::class.java, name)
                    .addCallback(TriviaDatabaseCallback(coroutineScope))
                    .build()
                instance = _instance
                _instance
            }
        }
    }

    private class TriviaDatabaseCallback(private val scope: CoroutineScope)
        : RoomDatabase.Callback(){
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            instance?.let {
                scope.launch {
                    insert(it.numberTriviaDao())
                }
            }
        }

        suspend fun insert(numberTriviaDao: NumberTriviaDao){
            val trivia = NumberTriviaEntity(1, "1 is the number of moons orbiting Earth.", Number)
            numberTriviaDao.insertTrivia(trivia)
        }
    }
}