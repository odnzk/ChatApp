package com.study.tinkoff.di.module

import android.content.Context
import androidx.room.Room
import com.study.tinkoff.di.AppDatabase
import com.study.tinkoff.di.AppDatabase.Companion.DATABASE_NAME

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal class AppDatabaseModule {

    @Provides
    @Singleton
    fun providesDatabase(context: Context): AppDatabase =
        Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()

}
