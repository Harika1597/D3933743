package uk.ac.tees.mad.sq.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import uk.ac.tees.mad.sq.repository.MainRepository
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val repository: MainRepository,
) : ViewModel() {

    val loggedIn = mutableStateOf(false)
    val loading = mutableStateOf(false)

    fun signUp(context: Context, email: String, name: String, password: String) {
        loading.value = true
        repository.signUp(email, name, password, onSuccess = {
            loggedIn.value = true
            loading.value = false
        }, onFailure = {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            Log.d("SINGUP", "signUp: $it")
            loading.value = false
        })
    }

    fun login(context: Context, email: String, password: String) {
        loading.value = true
        repository.login(email, password, onSuccess = {
            loggedIn.value = true
            loading.value = false
        }, onFailure = {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            Log.d("LOGIN", "login: $it")
            loading.value = false
        })
    }
}