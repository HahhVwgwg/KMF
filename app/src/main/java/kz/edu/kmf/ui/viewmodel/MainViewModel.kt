package kz.edu.kmf.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kz.edu.kmf.data.local.UserDatabase
import kz.edu.kmf.data.api.OwnApi
import kz.edu.kmf.data.api.UserResponse
import kz.edu.kmf.data.model.User
import kz.edu.kmf.data.repository.UserRepository
import kz.edu.kmf.utils.Resource


class MainViewModel(application: Application) : AndroidViewModel(application) {


    override fun onCleared() {
        super.onCleared()
        Log.i("MainViewModel", "MainViewModel destroyed")
    }

    fun registerUser(user: User) = liveData(Dispatchers.IO) {
        val userResponse = UserResponse()
        userResponse.email = user.email
        userResponse.password = user.password
        userResponse.firstName = user.firstName
        userResponse.lastName = user.lastName
        userResponse.userStatus = user.userStatus
        userResponse.username = user.username
        userResponse.phone = user.phone
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = OwnApi.retrofitService.registerUser(userResponse)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun getUsers(user: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = OwnApi.retrofitService.getUser(user)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    val readAllData: LiveData<List<User>>
    private val repository: UserRepository

    init {
        val userDao = UserDatabase.getDatabase(application).userDao()
        repository = UserRepository(userDao)
        println("Counter ")
        readAllData = repository.readAllData
    }

    fun addUser(event: User) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addUser(event)
        }
    }
}