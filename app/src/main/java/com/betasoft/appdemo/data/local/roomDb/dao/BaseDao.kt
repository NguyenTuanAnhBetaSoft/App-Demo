package com.betasoft.appdemo.data.local.roomDb.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(data: T)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(list: List<T>)

    @Delete
    fun delete(data: T)

    @Delete
    fun delete(list: List<T>)

    @Update
    fun update(data: T)

    @Update
    fun update(list: List<T>)
}