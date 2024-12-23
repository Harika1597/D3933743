package uk.ac.tees.mad.sq.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import uk.ac.tees.mad.sq.data.User
import uk.ac.tees.mad.sq.data.local.QuizAnswerEntity
import uk.ac.tees.mad.sq.data.local.QuizDao
import uk.ac.tees.mad.sq.data.local.UserPointsEntity
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val quizDao: QuizDao,
    private val storage: FirebaseStorage
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
        Log.d("ADDPOINTS", "addPoints: $currentPoints")
        if (currentPoints == 0) {
            Log.d("ADDPOINTSif", "addPoints: $points")
            quizDao.insertPoints(UserPointsEntity(points = points))
        } else {
            Log.d("ADDPOINTSelse", "addPoints: $points")
            quizDao.addPoints(points)
        }
    }

    suspend fun getPoints(): Int {
        return quizDao.getUserPoints() ?: 0
    }

    suspend fun resetPoints() {
        Log.d("RESETPOINTS", "resetPoints")
        quizDao.clearAllAnswers()
        quizDao.resetPoints()
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

    fun addProfilePicture(context: Context, uri: Uri) {
        val storageRef = storage.reference
        val imageRef = storageRef.child("profile_pictures/${auth.currentUser!!.uid}")
        imageRef.putFile(uri).addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener {
                firestore.collection("users").document(auth.currentUser!!.uid).update("profilePictureUrl", it.toString())
            }.addOnFailureListener {
                Toast.makeText(context, "Failed to upload image", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(context, "Failed to upload image", Toast.LENGTH_SHORT).show()
        }
    }

    suspend fun updateUserData(name: String,context: Context) {
        firestore.collection("users").document(auth.currentUser!!.uid).update("name", name).addOnSuccessListener {
            Toast.makeText(context, "Name updated successfully", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(context, "Failed to update name", Toast.LENGTH_SHORT).show()
        }
    }


}