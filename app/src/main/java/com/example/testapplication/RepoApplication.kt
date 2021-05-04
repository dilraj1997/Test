package com.example.testapplication

import android.app.Application
import com.example.testapplication.di.ApplicationComponent
import com.example.testapplication.di.ApplicationModule
import com.example.testapplication.di.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class RepoApplication : Application(), HasAndroidInjector {
    @Inject
    lateinit var supportAndroidInjector: DispatchingAndroidInjector<Any>

    private lateinit var mApplicationComponent: ApplicationComponent

    val applicationComponent
        get() = mApplicationComponent

    override fun onCreate() {
        super.onCreate()
        mApplicationComponent = DaggerApplicationComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .build()
        mApplicationComponent.inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return supportAndroidInjector
    }
}
