package com.bravedroid.jobby.auth.di

import javax.inject.Qualifier

//Dispatchers
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class DefaultDispatcher

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class IoDispatcher

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class MainDispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MainImmediateDispatcher

//Scopes
@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope
