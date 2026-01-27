/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.x218.basalt.ui.basalt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.x218.basalt.data.BasaltRepository
import com.x218.basalt.ui.basalt.BasaltUiState.Error
import com.x218.basalt.ui.basalt.BasaltUiState.Loading
import com.x218.basalt.ui.basalt.BasaltUiState.Success
import javax.inject.Inject

@HiltViewModel
class BasaltViewModel @Inject constructor(
    private val basaltRepository: BasaltRepository
) : ViewModel() {

    val uiState: StateFlow<BasaltUiState> = basaltRepository
        .basalts.map<List<String>, BasaltUiState>(::Success)
        .catch { emit(Error(it)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

    fun addBasalt(name: String) {
        viewModelScope.launch {
            basaltRepository.add(name)
        }
    }
}

sealed interface BasaltUiState {
    object Loading : BasaltUiState
    data class Error(val throwable: Throwable) : BasaltUiState
    data class Success(val data: List<String>) : BasaltUiState
}
