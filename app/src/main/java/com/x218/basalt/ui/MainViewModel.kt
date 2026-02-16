package com.x218.basalt.ui

import android.app.Application
import android.content.Context.SENSOR_SERVICE
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.x218.basalt.data.PermissionState
import com.x218.basalt.data.getAzimuthFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

val kaabaLocation = Location(LocationManager.GPS_PROVIDER).apply {
    latitude = 21.2445
    longitude = 39.82617
}

data class UiState(
    val perms: PermissionState = PermissionState(coarse = false, fine = false),
    val location: Location = Location(LocationManager.GPS_PROVIDER),
)

class MainViewModel(application: Application) : AndroidViewModel(application) {
    // Expose screen UI state
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    val sm = this.getApplication<Application>()
        .getSystemService(SENSOR_SERVICE) as SensorManager
    val azimuthFlow = getAzimuthFlow(sm)
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            0.0f
        )

    fun updatePerms(perms: PermissionState) {
        _uiState.update { currentUiState ->
            currentUiState.copy(
                perms = perms
            )
        }
    }

    fun updateLocation(location: Location) {
        _uiState.update { currentUiState ->
            currentUiState.copy(
                location = location
            )
        }
    }
}