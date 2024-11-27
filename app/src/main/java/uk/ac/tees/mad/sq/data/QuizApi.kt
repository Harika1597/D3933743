package uk.ac.tees.mad.sq.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import uk.ac.tees.mad.sq.data.remote.QuizData

interface QuizApi {
    @GET("api.php")
    suspend fun getQuiz(
        @Query("amount") amount: Int,
        @Query("category") category: Int,
        @Query("difficulty") difficulty: String): Response<QuizData>
}