package com.example.testapplication.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.testapplication.RepoApplication
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class ApplicationModule(private val mApplication: RepoApplication) {
    @Provides
    @ApplicationContext
    fun provideContext(): Context {
        return mApplication
    }

    @Provides
    fun provideApplication(): RepoApplication {
        return mApplication
    }

    @Provides
    @Singleton
    fun provideCoroutineExceptionHandler() = CoroutineExceptionHandler { coroutineContext, exception ->
        println("CoroutineExceptionHandler got $exception in $coroutineContext, ${coroutineContext[CoroutineName.Key]}")
    }
}
