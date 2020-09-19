package com.dm.login

import androidx.room.*

@Dao
interface UserDao {

    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<User>

    @Insert
    suspend fun insertAll(vararg user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("SELECT * FROM users WHERE login=:login")
    suspend fun findUser(login: String): User?
}