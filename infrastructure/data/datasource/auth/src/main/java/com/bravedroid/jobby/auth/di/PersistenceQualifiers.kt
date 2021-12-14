package com.bravedroid.jobby.auth.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RefreshTokenSharedPreferences

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RefreshTokenSharedPreferencesEditor

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RefreshTokenSharedPreferencesSecure

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RefreshTokenSharedPreferencesEditorSecure

