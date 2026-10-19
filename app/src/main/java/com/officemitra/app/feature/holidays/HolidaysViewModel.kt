package com.officemitra.app.feature.holidays

import androidx.lifecycle.ViewModel
import com.officemitra.app.data.model.Holiday
import com.officemitra.app.data.repository.DemoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HolidaysViewModel @Inject constructor(
    private val repository: DemoRepository
) : ViewModel() {
    val holidays: StateFlow<List<Holiday>> = repository.holidays
}
