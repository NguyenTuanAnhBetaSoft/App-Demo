package com.betasoft.appdemo.data.local.roomDb.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.betasoft.appdemo.data.model.ImageLocal

@Dao
interface ImageLocalDao : BaseDao<ImageLocal> {
    @Query("SELECT * FROM imageLocal")
    fun getAllImageLocal(): PagingSource<Int, ImageLocal>

    @Query("SELECT * FROM imageLocal")
    fun searchImageLocal(): PagingSource<Int, ImageLocal>

    @Query("SELECT * FROM imageLocal WHERE nameAuthor LIKE '%' || :searchQuery || '%'")
    fun searchPlaylist(searchQuery: String?): PagingSource<Int, ImageLocal>
}