package uk.ac.tees.mad.sq.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import uk.ac.tees.mad.sq.data.User
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
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

}