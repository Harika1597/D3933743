package uk.ac.tees.mad.sq.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface QuizDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnswer(answer: QuizAnswerEntity)

    @Query("SELECT * FROM QUIZ_ANSWER WHERE question = :questionId")
    suspend fun getAnswerByQuestionId(questionId: String): QuizAnswerEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPoints(userPoints: UserPointsEntity)

    @Query("SELECT points FROM user_points LIMIT 1")
    suspend fun getUserPoints(): Int?

    @Query("UPDATE user_points SET points = points + :points WHERE id = 1")
    suspend fun addPoints(points: Int)

    @Query("DELETE FROM user_points")
    suspend fun resetPoints()

    @Query("DELETE FROM QUIZ_ANSWER")
    suspend fun clearAllAnswers()
}