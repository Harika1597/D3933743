package uk.ac.tees.mad.sq.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
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
    private val _points = mutableStateOf(0)
    val points: State<Int> get() = _points

    init {
        if (auth.currentUser != null) {
            loggedIn.value = true
            fetchUserData()
            fetchInitialData()
            loadPoints()
        }
    }

    fun addAnswer(questionId: String) {
        Log.d("ADDANSWERCalled", "addAnswer: $questionId")
        viewModelScope.launch {
            if (!repository.isQuestionAnswered(questionId)) {
                Log.d("ADDANSWERConditionhit", "addAnswer: $questionId")
                    repository.addPoints(5)
                    _points.value = repository.getPoints()
                Log.d("Point", "addAnswer: ${_points.value}")
                repository.saveAnswer(questionId)
            }
        }
    }

    fun loadPoints() {
        viewModelScope.launch {
            _points.value = repository.getPoints()
            Log.d("POINTS", "loadPoints: $_points")
        }
    }

    fun resetPoints(){
        viewModelScope.launch {
            repository.resetPoints()
            _points.value = repository.getPoints()
        }
    }

    fun updateUserData(name: String,context: Context){
        viewModelScope.launch {
            repository.updateUserData(name, context)
            fetchUserData()
        }
    }

    fun LogOut(){
        auth.signOut()
        loggedIn.value = false
    }

    fun addProfilePicture(context: Context, uri : Uri) {
        viewModelScope.launch {
            repository.addProfilePicture(context, uri)
            fetchUserData()
        }
    }

    fun fetchInitialData() {
        viewModelScope.launch {
            try {
                quizData.value = quizApi.getQuiz(30, 9, "easy").body()
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
                val response = quizApi.getQuiz(30, cat, "easy")
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