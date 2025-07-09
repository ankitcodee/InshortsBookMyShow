package com.example.movieapp

import android.app.Application
import androidx.room.Room
import com.example.movieapp.data.local.AppDatabase

class App : Application() {
    companion object {
        lateinit var database: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "movies_db"
        ).fallbackToDestructiveMigration().build()
    }
}
