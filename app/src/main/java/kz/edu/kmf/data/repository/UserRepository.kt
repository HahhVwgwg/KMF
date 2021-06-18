package kz.edu.kmf.data.repository

import androidx.lifecycle.LiveData
import kz.edu.kmf.data.model.User
import kz.edu.kmf.data.local.UserDao

class UserRepository(private val userDao: UserDao) {

    val readAllData: LiveData<List<User>> = userDao.readAllData()

    suspend fun addUser(user: User){
        userDao.addUser(user)
    }
}