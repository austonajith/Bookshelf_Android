package com.app.bookshelf.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.app.bookshelf.db.dao.entity.BooksEntity
import com.app.bookshelf.db.dao.entity.MyCategoryEntity

@Dao
interface CategoryDao {
    @Insert
    suspend fun insertCategoryData(data: List<MyCategoryEntity>)

    @Query("DELETE FROM my_category")
    suspend fun deleteCategoryData()

    @Query("SELECT * FROM my_category")
    suspend fun getCategoryData(): List<MyCategoryEntity>

}