package uk.ac.tees.mad.sq.data.remote

import kotlinx.serialization.Serializable

data class Result(
    val category: String,
    val correct_answer: String,
    val difficulty: String,
    val incorrect_answers: List<String>,
    val question: String,
    val type: String
)