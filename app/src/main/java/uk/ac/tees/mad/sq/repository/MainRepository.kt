package uk.ac.tees.mad.sq.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import uk.ac.tees.mad.sq.data.User
import uk.ac.tees.mad.sq.data.local.QuizAnswerEntity
import uk.ac.tees.mad.sq.data.local.QuizDao
import uk.ac.tees.mad.sq.data.local.UserPointsEntity
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val quizDao: QuizDao
) {

    suspend fun saveAnswer(questionId: String) {
        quizDao.insertAnswer(QuizAnswerEntity(question = questionId))
    }
    suspend fun isQuestionAnswered(questionId: String): Boolean {
        Log.d("ISQUESTIONANSWERED", "isQuestionAnswered: ${quizDao.getAnswerByQuestionId(questionId)}")
        return quizDao.getAnswerByQuestionId(questionId) != null
    }
    suspend fun addPoints(points: Int) {
        val currentPoints = getPoints()
        if (currentPoints == 0) {
            quizDao.insertPoints(UserPointsEntity(points = points))
        } else {
            quizDao.addPoints(points)
        }
    }
    suspend fun getPoints(): Int {
        return quizDao.getUserPoints() ?: 0
    }

    fun signUp(email: String, name: String, password: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
            auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
                val uid = it.user?.uid
                firestore.collection("users").document(uid!!).set(User(
                    id = uid,
                    name = name,
                    email = email,
                    password = password
                )).addOnSuccessListener {
                    onSuccess()
                }.addOnFailureListener { error->
                    onFailure(error.localizedMessage ?: "Unknown error occurred")
                }
            }.addOnFailureListener {
                onFailure(it.localizedMessage ?: "Unknown error occurred")
            }
    }

    fun login(email: String, password: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            onSuccess()
        }.addOnFailureListener { error->
            onFailure(error.localizedMessage ?: "Unknown error occurred")
        }
    }

    fun fetchUserData(onSuccess: (User) -> Unit, onFailure: (String) -> Unit) {
        firestore.collection("users").document(auth.currentUser!!.uid).get().addOnSuccessListener {
            onSuccess(
                User(
                    id = it.getString("id")!!,
                    profilePictureUrl = it.getString("profilePictureUrl")!!,
                    name = it.getString("name")!!,
                    email = it.getString("email")!!,
                    password = it.getString("password")!!
                )
            )
        }.addOnFailureListener {
            onFailure(
                it.localizedMessage ?: "Unknown error occurred"
            )
        }
    }

}