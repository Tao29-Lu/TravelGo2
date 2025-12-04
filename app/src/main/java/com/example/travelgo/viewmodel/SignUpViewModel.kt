package com.example.travelgo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.travelgo.data.remote.dto.RegisteredUser
import com.example.travelgo.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SignUpUiState(
    val isLoading: Boolean = false,
    val signUpSuccess: Boolean = false,
    val createdUser: RegisteredUser? = null,
    val error: String? = null
)

class SignUpViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState = _uiState.asStateFlow()

    fun signUp(
        username: String,
        contrasena: String,
        nombre: String,
        email: String
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // Validaciones básicas
            if (nombre.isBlank() || email.isBlank() || username.isBlank() || contrasena.isBlank()) {
                _uiState.update { it.copy(isLoading = false, error = "Todos los campos son obligatorios") }
                return@launch
            }

            val result = repository.register(username, contrasena, nombre, email)

            result.fold(
                onSuccess = { user ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            signUpSuccess = true,
                            createdUser = user
                        )
                    }
                },
                onFailure = { e ->
                    _uiState.update {
                        it.copy(isLoading = false, error = e.message)
                    }
                }
            )
        }
    }

    fun errorShown() {
        _uiState.update { it.copy(error = null) }
    }
}

class SignUpViewModelFactory(private val repository: AuthRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SignUpViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}