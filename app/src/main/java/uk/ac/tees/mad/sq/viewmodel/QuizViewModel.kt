package uk.ac.tees.mad.sq.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import uk.ac.tees.mad.sq.data.QuizApi
import uk.ac.tees.mad.sq.data.User
import uk.ac.tees.mad.sq.data.remote.QuizData
import uk.ac.tees.mad.sq.repository.MainRepository
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val repository: MainRepository,
    private val quizApi: QuizApi
) : ViewModel() {

    val loggedIn = mutableStateOf(false)
    val loading = mutableStateOf(false)
    val userInformation = mutableStateOf(User())
    val quizData = mutableStateOf<QuizData?>(null)
    private var fetchJob: Job? = null


    init {
        if (auth.currentUser != null) {
            loggedIn.value = true
            fetchUserData()
            fetchInitialData()
        }
    }

    private fun fetchInitialData() {
        viewModelScope.launch {
            try {
                quizData.value = quizApi.getQuiz(10, 9, "easy").body()
            } catch (e: Exception) {
                Log.d("FETCHINITIALDATA", "fetchInitialData: ${e.message}")
            }
        }
    }

    fun signUp(context: Context, email: String, name: String, password: String) {
        loading.value = true
        repository.signUp(email, name, password, onSuccess = {
            loggedIn.value = true
            fetchUserData()
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
            fetchUserData()
            loading.value = false
        }, onFailure = {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            Log.d("LOGIN", "login: $it")
            loading.value = false
        })
    }

    fun fetchFromApi(category : String = "9",onComplete: () -> Unit) {
        fetchJob?.cancel()
        val cat = category.toInt()
        fetchJob = viewModelScope.launch {
            try {
                val response = quizApi.getQuiz(10, cat, "easy")
                quizData.value = response.body()

            } catch (e: Exception) {
            } finally {
                onComplete()
            }
        }
    }

    fun fetchUserData(){
        repository.fetchUserData(onSuccess = {
            userInformation.value = it
        },onFailure = {
            Log.d("FETCHUSERDATA", "fetchUserData: $it")
        })
    }
}