package com.example.testapplication.di

import com.example.testapplication.ui.RepoActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeRepoActivity(): RepoActivity?
}
