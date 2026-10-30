package com.officemitra.app.feature.salary

import androidx.lifecycle.ViewModel
import com.officemitra.app.data.model.SalarySlip
import com.officemitra.app.data.model.User
import com.officemitra.app.data.repository.OfficeMitraRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SalaryViewModel @Inject constructor(
    private val repository: OfficeMitraRepository
) : ViewModel() {
    val salarySlips: StateFlow<List<SalarySlip>> = repository.salarySlips
    val currentUser: StateFlow<User> = repository.currentUser
}
