package com.app.bookshelf.db.dao.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "my_category")
data class MyCategoryEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var name: String?
)
