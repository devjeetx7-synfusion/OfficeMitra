package com.officemitra.app.feature.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.officemitra.app.data.model.AppNotification
import com.officemitra.app.data.repository.OfficeMitraRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val repository: OfficeMitraRepository
) : ViewModel() {

    val notifications: StateFlow<List<AppNotification>> = repository.notifications

    fun markAsRead(id: String) {
        viewModelScope.launch {
            repository.markNotificationAsRead(id)
        }
    }

    fun markAllAsRead() {
        viewModelScope.launch {
            repository.markAllNotificationsAsRead()
        }
    }

    fun clearAll() {
        viewModelScope.launch {
            repository.clearNotifications()
        }
    }
}
