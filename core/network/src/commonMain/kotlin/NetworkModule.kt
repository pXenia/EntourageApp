package com.entourageapp.core.network

import org.koin.core.module.Module

//private const val BASE_URL = "http://10.0.2.2:8000/" // для эмулятора
//private const val BASE_URL = "http://10.240.155.126:8000/" // для устройства
private const val BASE_URL = "http://0.0.0.0:8000/"

expect val networkModule: Module