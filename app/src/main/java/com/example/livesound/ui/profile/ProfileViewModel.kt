package com.example.livesound.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Perfil del Usuario"
    }
    val text: LiveData<String> = _text

    private val _textUserName = MutableLiveData<String>().apply {
        value = "Polimusico"
    }
    val textUserName: LiveData<String> = _textUserName

    private val _textUserMail = MutableLiveData<String>().apply {
        value = "polimusico@poligran.edu.com"
    }
    val textUserMail: LiveData<String> = _textUserMail
}