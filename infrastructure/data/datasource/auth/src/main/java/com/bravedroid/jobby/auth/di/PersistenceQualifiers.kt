package com.bravedroid.jobby.auth.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RefreshTokenSharedPreferences

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RefreshTokenSharedPreferencesEditor
