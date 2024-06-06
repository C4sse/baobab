package com.example.caloriesapp.di

import com.example.caloriesapp.data.api.CalorieNinjasApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.calorieninjas.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideCalorieNinjasApi(retrofit: Retrofit): CalorieNinjasApi {
        return retrofit.create(CalorieNinjasApi::class.java)
    }
}
