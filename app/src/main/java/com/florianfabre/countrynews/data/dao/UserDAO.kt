package com.florianfabre.countrynews.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.florianfabre.countrynews.data.model.User

/**
 * DAO for the `User` entity.
 *
 * @method addNewUser Inserts a new user into the `User` table.
 * @method insertUsers Inserts a list of users into the `User` table.
 * @method getUser Retrieves a user from the `User` table by their login name and password.
 * @method getUserByLoginName Retrieves a user from the `User` table by their login name.
 * @method deleteUser Deletes a user from the `User` table by their login name.
 */
@Dao
interface UserDAO {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addNewUser(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUsers(users: List<User>)

    @Query("SELECT * FROM User WHERE username = :loginName AND password = :password")
    fun getUser(loginName: String, password: String): User?

    @Query("SELECT * FROM User WHERE username = :loginName")
    fun getUserByLoginName(loginName: String): User?

    @Query("DELETE FROM User WHERE username = :loginName")
    fun deleteUser(loginName: String)
}