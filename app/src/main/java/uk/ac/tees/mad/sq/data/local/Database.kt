package uk.ac.tees.mad.sq.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [QuizAnswerEntity::class, UserPointsEntity::class], version = 1)
abstract class QuizDatabase : RoomDatabase() {
    abstract fun quizDao(): QuizDao
}