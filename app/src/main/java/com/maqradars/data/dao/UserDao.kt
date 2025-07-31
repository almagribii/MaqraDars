package com.maqradars.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.maqradars.data.entity.User
import kotlinx.coroutines.flow.Flow

@Dao
interface serDao {
    @Insert
    suspend fun insertUser(user: User): Long

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("SELECT * FROM users LIMIT 1")
    fun getSingleUser() : Flow<User?>
}