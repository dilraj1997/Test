package com.example.testapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.example.testapplication.di.CustomViewModelFactory
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class RepoActivity : AppCompatActivity(), HasAndroidInjector {
    companion object {
        const val TAG = "RepoActivity"
    }

    @Inject
    lateinit var supportFragmentInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var viewModelFactory: CustomViewModelFactory

    private val mRepoViewModel by viewModels<RepoViewModel> {
        viewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "$mRepoViewModel")
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return supportFragmentInjector
    }
}