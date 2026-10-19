package com.officemitra.app.feature.notifications

import androidx.lifecycle.ViewModel
import com.officemitra.app.data.model.AppNotification
import com.officemitra.app.data.repository.DemoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val repository: DemoRepository
) : ViewModel() {

    val notifications: StateFlow<List<AppNotification>> = repository.notifications

    fun markAsRead(id: String) {
        repository.markNotificationAsRead(id)
    }

    fun markAllAsRead() {
        repository.markAllNotificationsAsRead()
    }

    fun clearAll() {
        repository.clearNotifications()
    }
}
