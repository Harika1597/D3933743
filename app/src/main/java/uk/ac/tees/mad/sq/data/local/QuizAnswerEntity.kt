package uk.ac.tees.mad.sq.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_answer")
data class QuizAnswerEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val question: String,
)
