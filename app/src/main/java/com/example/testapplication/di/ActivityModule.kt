package com.example.testapplication.di

import com.example.testapplication.RepoActivity
import com.example.testapplication.RepoApplication
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeRepoActivity(): RepoActivity?
}
