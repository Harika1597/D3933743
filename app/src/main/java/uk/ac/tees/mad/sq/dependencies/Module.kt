package uk.ac.tees.mad.sq.dependencies

import android.content.Context
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uk.ac.tees.mad.sq.data.QuizApi
import uk.ac.tees.mad.sq.data.local.QuizDao
import uk.ac.tees.mad.sq.data.local.QuizDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Provides
    fun providesAuth() : FirebaseAuth = Firebase.auth

    @Provides
    fun providesFirestore() : FirebaseFirestore = Firebase.firestore

    @Provides
    fun providesRetrorfit() : Retrofit = Retrofit.Builder()
        .baseUrl("https://opentdb.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    fun providesQuizApi(retrofit: Retrofit) : QuizApi = retrofit.create(QuizApi::class.java)

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): QuizDatabase {
        return Room.databaseBuilder(
            appContext,
            QuizDatabase::class.java,
            "quiz_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideQuizDao(database: QuizDatabase): QuizDao {
        return database.quizDao()
    }

    @Provides
    fun providesStorage():FirebaseStorage = Firebase.storage

}