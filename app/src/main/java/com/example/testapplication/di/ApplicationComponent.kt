package com.example.testapplication.di

import android.content.Context
import com.example.testapplication.RepoApplication
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ApplicationModule::class,
        ViewModelFactoryModule::class,
        ActivityModule::class,
        AndroidInjectionModule::class
    ]
)
interface ApplicationComponent : AndroidInjector<RepoApplication> {
    override fun inject(demoApplication: RepoApplication)

    @get:ApplicationContext
    val context: Context
    val application: RepoApplication
}
