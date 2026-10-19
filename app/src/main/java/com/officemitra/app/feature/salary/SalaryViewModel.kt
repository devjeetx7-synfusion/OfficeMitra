package com.officemitra.app.feature.salary

import androidx.lifecycle.ViewModel
import com.officemitra.app.data.model.SalarySlip
import com.officemitra.app.data.repository.DemoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SalaryViewModel @Inject constructor(
    private val repository: DemoRepository
) : ViewModel() {
    val salarySlips: StateFlow<List<SalarySlip>> = repository.salarySlips
}
