package com.entourageapp.features.userprofile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entourageapp.features.userprofile.domain.UserProfileRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserProfileVM(
    private val repository: UserProfileRepository
) : ViewModel() {

    private val _state = MutableStateFlow(UserProfileState())
    val state: StateFlow<UserProfileState> = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<UserProfileSideEffect>()
    val sideEffect: SharedFlow<UserProfileSideEffect> = _sideEffect.asSharedFlow()

    fun onIntent(intent: UserProfileIntent) {
        when (intent) {
            is UserProfileIntent.LoadProfile -> loadProfile()
            is UserProfileIntent.SetEditDialogVisibility -> setEditDialogVisibility(intent.isVisible)
            is UserProfileIntent.SetEditStep -> setEditStep(intent.step)
            is UserProfileIntent.UpdateNewName -> updateNewName(intent.name)
            is UserProfileIntent.UpdateCurrentPassword -> updateCurrentPassword(intent.password)
            is UserProfileIntent.UpdateNewPassword -> updateNewPassword(intent.password)
            is UserProfileIntent.UpdateConfirmPassword -> updateConfirmPassword(intent.password)
            is UserProfileIntent.UpdateDeleteConfirmPassword -> updateDeleteConfirmPassword(intent.password)
            is UserProfileIntent.SaveName -> saveName()
            is UserProfileIntent.SavePassword -> savePassword()
            is UserProfileIntent.Logout -> logout()
            is UserProfileIntent.SetDeleteDialogVisibility -> setDeleteDialogVisibility(intent.isVisible)
            is UserProfileIntent.DeleteAccount -> deleteAccount()
        }
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _state.update { it.copy(status = UserProfileState.UserProfileStatus.Loading) }
            try {
                val user = repository.getMe()
                _state.update {
                    it.copy(
                        name = user.name,
                        email = user.email,
                        initials = user.name.split(" ").joinToString("") { it.take(1) }.uppercase(),
                        status = UserProfileState.UserProfileStatus.Content
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(status = UserProfileState.UserProfileStatus.Error) }
                _sideEffect.emit(UserProfileSideEffect.ShowError("Ошибка при загрузке профиля"))
            }
        }
    }

    private fun setEditDialogVisibility(isVisible: Boolean) {
        _state.update {
            it.copy(
                isEditDialogVisible = isVisible,
                editStep = UserProfileState.EditProfileStep.Selection,
                newName = it.name,
                currentPassword = "",
                newPassword = "",
                confirmPassword = ""
            )
        }
    }

    private fun setEditStep(step: UserProfileState.EditProfileStep) {
        _state.update { it.copy(editStep = step) }
    }

    private fun updateNewName(name: String) {
        _state.update { it.copy(newName = name) }
    }

    private fun updateCurrentPassword(password: String) {
        _state.update { it.copy(currentPassword = password) }
    }

    private fun updateNewPassword(password: String) {
        _state.update { it.copy(newPassword = password) }
    }

    private fun updateConfirmPassword(password: String) {
        _state.update { it.copy(confirmPassword = password) }
    }

    private fun updateDeleteConfirmPassword(password: String) {
        _state.update { it.copy(deleteConfirmPassword = password) }
    }

    private fun saveName() {
        viewModelScope.launch {
            try {
                val newName = _state.value.newName
                repository.updateName(newName)
                _state.update {
                    it.copy(
                        name = newName,
                        initials = newName.split(" ").joinToString("") { it.take(1) }.uppercase(),
                        isEditDialogVisible = false
                    )
                }
                _sideEffect.emit(UserProfileSideEffect.ShowMessage("Имя успешно обновлено"))
            } catch (e: Exception) {
                _sideEffect.emit(UserProfileSideEffect.ShowError("Ошибка при сохранении имени"))
            }
        }
    }

    private fun savePassword() {
        viewModelScope.launch {
            try {
                val current = _state.value.currentPassword
                val new = _state.value.newPassword
                val confirm = _state.value.confirmPassword
                if (new != confirm) {
                    _sideEffect.emit(UserProfileSideEffect.ShowError("Пароли не совпадают"))
                    return@launch
                }
                repository.updatePassword(current, new)
                _state.update { it.copy(isEditDialogVisible = false) }
                _sideEffect.emit(UserProfileSideEffect.ShowMessage("Пароль успешно изменен"))
            } catch (e: Exception) {
                _sideEffect.emit(UserProfileSideEffect.ShowError("Ошибка при сохранении пароля"))
            }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            try {
                repository.logout()
                _sideEffect.emit(UserProfileSideEffect.NavigateToLogin)
            } catch (e: Exception) {
                _sideEffect.emit(UserProfileSideEffect.ShowError("Ошибка при выходе из аккаунта"))
            }
        }
    }

    private fun setDeleteDialogVisibility(isVisible: Boolean) {
        _state.update { it.copy(isDeleteDialogVisible = isVisible, deleteConfirmPassword = "") }
    }

    private fun deleteAccount() {
        viewModelScope.launch {
            try {
                repository.deleteAccount(_state.value.deleteConfirmPassword)
                _sideEffect.emit(UserProfileSideEffect.NavigateToLogin)
            } catch (e: Exception) {
                _sideEffect.emit(UserProfileSideEffect.ShowError("Ошибка при удалении аккаунта"))
            }
        }
    }
}
