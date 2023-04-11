package com.betasoft.appdemo.data.local.roomDb.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.betasoft.appdemo.data.api.model.ImageLocal
import com.betasoft.appdemo.data.local.roomDb.dao.ImageLocalDao


@Database(
    entities = [
        ImageLocal::class,
    ], version = 1, exportSchema = true

)

abstract class AppDatabase : RoomDatabase() {
    abstract fun ImageLocalDao(): ImageLocalDao
}