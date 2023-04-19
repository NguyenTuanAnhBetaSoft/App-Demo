package com.betasoft.appdemo.di

import javax.inject.Qualifier

@Qualifier
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FUNCTION)
annotation class AppContext
