package uk.ac.tees.mad.sq.data.remote

data class QuizData(
    val response_code: Int,
    val results: List<Result>
)