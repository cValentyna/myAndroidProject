package com.project.myapp.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.myapp.data.datastore.DataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val dataStore: DataStore) : ViewModel() {

    val isCheckedFlow = dataStore.getIsChecked()

    fun forgetUser() {
        viewModelScope.launch {
            dataStore.clearPreferences()
        }
    }
}
