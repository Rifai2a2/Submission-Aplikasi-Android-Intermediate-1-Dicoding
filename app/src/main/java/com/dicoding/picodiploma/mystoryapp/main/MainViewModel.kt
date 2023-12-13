package com.dicoding.picodiploma.mystoryapp.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.picodiploma.mystoryapp.data.UserRepository
import com.dicoding.picodiploma.mystoryapp.userpref.UserModel

class MainViewModel(private val repository: UserRepository): ViewModel() {
    fun getStories() = repository.getStory()

    suspend fun logout(){
        repository.logout()
    }
    fun getSession(): LiveData<UserModel>{
        return repository.getSession().asLiveData()
    }
}