package com.florianfabre.countrynews.data.repository

import com.florianfabre.countrynews.data.dao.UserDAO
import com.florianfabre.countrynews.data.model.User
import org.mindrot.jbcrypt.BCrypt

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
    fun addNewUser(user: User) {
        val hashedPassword = BCrypt.hashpw(user.password, BCrypt.gensalt())
        val userWithHashedPassword = user.copy(password = hashedPassword)
        userDAO.addNewUser(userWithHashedPassword)
    }
    fun insertUsers(users: List<User>) = userDAO.insertUsers(users)
    fun getUserByLoginName(loginName: String) = userDAO.getUserByLoginName(loginName)
    fun verifyUser(loginName: String, password: String): Boolean {
        val user = getUserByLoginName(loginName)
        return user?.let { BCrypt.checkpw(password, it.password) } ?: false
    }
    fun deleteUser(loginName: String) = userDAO.deleteUser(loginName)
}