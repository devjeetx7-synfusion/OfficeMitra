package com.officemitra.app.feature.holidays

import androidx.lifecycle.ViewModel
import com.officemitra.app.data.model.Holiday
import com.officemitra.app.data.repository.OfficeMitraRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HolidaysViewModel @Inject constructor(
    private val repository: OfficeMitraRepository
) : ViewModel() {
    val holidays: StateFlow<List<Holiday>> = repository.holidays
}
