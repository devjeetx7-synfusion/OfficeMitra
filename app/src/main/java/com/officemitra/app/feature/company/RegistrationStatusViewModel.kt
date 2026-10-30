package com.officemitra.app.feature.company

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.officemitra.app.data.model.User
import com.officemitra.app.data.repository.OfficeMitraRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationStatusViewModel @Inject constructor(
    private val repository: OfficeMitraRepository
) : ViewModel() {
    val currentUser: StateFlow<User> = repository.currentUser

    fun refreshStatus() {
        viewModelScope.launch {
            repository.refreshAll()
        }
    }
}
