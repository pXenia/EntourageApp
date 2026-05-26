package com.entourageapp.features.userprofile.presentation.editprofile

data class UserProfileState(
    val name: String = "Пётр Петров",
    val email: String = "petr@petrov.com",
    val initials: String = "ПП",
    val status: UserProfileStatus = UserProfileStatus.Content,
    val isEditDialogVisible: Boolean = false,
    val editStep: EditProfileStep = EditProfileStep.Selection,
    val newName: String = "",
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val deleteConfirmPassword: String = "",
    val isDeleteDialogVisible: Boolean = false
) {
    sealed interface UserProfileStatus {
        data object Loading : UserProfileStatus
        data object Content : UserProfileStatus
        data object Error : UserProfileStatus
    }

    enum class EditProfileStep {
        Selection,
        EditName,
        EditPassword
    }
}

sealed interface UserProfileIntent {
    data object LoadProfile : UserProfileIntent
    data class SetEditDialogVisibility(val isVisible: Boolean) : UserProfileIntent
    data class SetEditStep(val step: UserProfileState.EditProfileStep) : UserProfileIntent
    data class UpdateNewName(val name: String) : UserProfileIntent
    data class UpdateCurrentPassword(val password: String) : UserProfileIntent
    data class UpdateNewPassword(val password: String) : UserProfileIntent
    data class UpdateConfirmPassword(val password: String) : UserProfileIntent
    data class UpdateDeleteConfirmPassword(val password: String) : UserProfileIntent
    data object SaveName : UserProfileIntent
    data object SavePassword : UserProfileIntent
    data object Logout : UserProfileIntent
    data class SetDeleteDialogVisibility(val isVisible: Boolean) : UserProfileIntent
    data object DeleteAccount : UserProfileIntent
}

sealed interface UserProfileSideEffect {
    data class ShowError(val message: String) : UserProfileSideEffect
    data class ShowMessage(val message: String) : UserProfileSideEffect
}
