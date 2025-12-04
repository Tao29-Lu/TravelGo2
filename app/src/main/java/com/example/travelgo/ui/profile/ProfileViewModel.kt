package com.example.travelgo.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.travelgo.data.remote.dto.UserDto
import com.example.travelgo.repository.UserRepository
import com.example.travelgo.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ProfileUiState(
    val isLoading: Boolean = false,
    val user: UserDto? = null,
    val error: String? = null,
    val logoutSuccess: Boolean = false
)

class ProfileViewModel(private val userRepository: UserRepository, private val authRepository: AuthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    fun loadProfile() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val result = userRepository.getProfile()

            result.onSuccess { user ->
                _uiState.value = _uiState.value.copy(isLoading = false, user = user)
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = error.message ?: "Error al cargar perfil"
                )
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _uiState.value = _uiState.value.copy(logoutSuccess = true)
        }
    }
}

class ProfileViewModelFactory(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(userRepository, authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}