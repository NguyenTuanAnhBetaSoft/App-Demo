package com.betasoft.appdemo.data.local.roomDb.dao

import androidx.room.Dao
import androidx.room.Query
import com.betasoft.appdemo.data.api.model.ImageLocal

@Dao
interface ImageLocalDao : BaseDao<ImageLocal> {
    @Query("SELECT * FROM ImageLocal")
    fun getAllImageLocal(): List<ImageLocal>
}