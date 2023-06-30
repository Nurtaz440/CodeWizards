package com.example.farganaapp.ui.home.client

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.farganaapp.model.UserDetails
import com.example.farganaapp.repository.UserRepository


class ProfileViewModel(private val userRepository: UserRepository) : ViewModel() {
    val allUsers: LiveData<UserDetails> = userRepository.allUsers

    class ProfileViewModelFactory(private val userRepository: UserRepository) :
        ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
                return ProfileViewModel(userRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}