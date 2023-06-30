package com.example.farganaapp.ui.registration.sign_up

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.farganaapp.model.UserDetails
import com.example.farganaapp.repository.UserRepository
import kotlinx.coroutines.launch

class RegistrationViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _registrationResult = MutableLiveData<Boolean>()
    val registrationResult: LiveData<Boolean> = _registrationResult

    //val allUsers: LiveData<List<UserDetails>> = userRepository.allUsers

    fun registerUser(name: String,country:String,region:String,street:String) {
        val user = UserDetails(name = name, country = country, region = region,street = street)
        viewModelScope.launch {
            try {
                userRepository.insertUser(user)
                _registrationResult.postValue(true)
            } catch (e: Exception) {
                _registrationResult.postValue(false)
            }
        }
    }
    class RegistrationViewModelFactory(private val userRepository: UserRepository) :
        ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RegistrationViewModel::class.java)) {
                return RegistrationViewModel(userRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}