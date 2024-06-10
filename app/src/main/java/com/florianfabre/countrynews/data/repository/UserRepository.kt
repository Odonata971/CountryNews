package com.florianfabre.countrynews.data.repository

import com.florianfabre.countrynews.data.dao.UserDAO
import com.florianfabre.countrynews.data.model.User

/**
 * Repository for the `User` entity.
 *
 * @property userDAO The DAO for the `User` entity.
 *
 * @method addNewUser Inserts a new user into the `User` table.
 * @method insertUsers Inserts a list of users into the `User` table.
 * @method getUser Retrieves a user from the `User` table by their login name and password.
 * @method getUserByLoginName Retrieves a user from the `User` table by their login name.
 * @method deleteUser Deletes a user from the `User` table by their login name.
 */
class UserRepository(private val userDAO: UserDAO) {
    fun addNewUser(user: User) = userDAO.addNewUser(user)
    fun insertUsers(users: List<User>) = userDAO.insertUsers(users)
    fun getUser(loginName: String, password: String) = userDAO.getUser(loginName, password)
    fun getUserByLoginName(loginName: String) = userDAO.getUserByLoginName(loginName)
    fun deleteUser(loginName: String) = userDAO.deleteUser(loginName)
}