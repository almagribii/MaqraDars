package com.maqradars.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "username")
    val username: String? = null,
    @ColumnInfo(name = "last_active")
    val lastActive: Long? = null,
    @ColumnInfo(name = "is_dark_mode")
    val isDarkMode: Boolean = false,
)