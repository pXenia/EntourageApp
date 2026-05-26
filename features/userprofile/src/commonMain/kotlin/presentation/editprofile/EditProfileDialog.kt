package com.entourageapp.features.userprofile.presentation.editprofile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.entourageapp.core.ui.EntourageBlack
import com.entourageapp.core.ui.EntourageLightBlueGray
import com.entourageapp.core.ui.arrowRight
import com.entourageapp.core.ui.components.CustomTextBar
import com.entourageapp.core.ui.components.DialogButton
import org.jetbrains.compose.resources.painterResource

@Composable
fun EditProfileDialog(
    state: UserProfileState,
    onIntent: (UserProfileIntent) -> Unit,
) {
    Dialog(onDismissRequest = { onIntent(UserProfileIntent.SetEditDialogVisibility(isVisible = false)) }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .background(EntourageLightBlueGray)
                .imePadding()
                .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Редактирование профиля",
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 22.sp),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            when (state.editStep) {
                UserProfileState.EditProfileStep.Selection -> {
                    EditProfileSelection(onIntent)
                }
                UserProfileState.EditProfileStep.EditName -> {
                    EditNameLayout(state, onIntent)
                }
                UserProfileState.EditProfileStep.EditPassword -> {
                    EditPasswordLayout(state, onIntent)
                }
            }
        }
    }
}

@Composable
private fun EditProfileSelection(onIntent: (UserProfileIntent) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ActionItem(
            text = "Редактировать имя",
            onClick = { onIntent(UserProfileIntent.SetEditStep(UserProfileState.EditProfileStep.EditName)) }
        )
        ActionItem(
            text = "Редактировать пароль",
            onClick = { onIntent(UserProfileIntent.SetEditStep(UserProfileState.EditProfileStep.EditPassword)) }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            DialogButton(
                text = "Отменить",
                onClick = { onIntent(UserProfileIntent.SetEditDialogVisibility(false)) }
            )
        }
    }
}

@Composable
private fun ActionItem(text: String, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(32.dp),
        color = EntourageBlack.copy(alpha = 0.05f)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp)
            )
            Icon(
                painter = painterResource(arrowRight),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = Color.Black
            )
        }
    }
}

@Composable
private fun EditNameLayout(
    state: UserProfileState,
    onIntent: (UserProfileIntent) -> Unit
) {
    Column {
        Text(
            text = "Введите новое имя пользователя",
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
            modifier = Modifier.padding(bottom = 8.dp).align(Alignment.CenterHorizontally)
        )

        CustomTextBar(
            value = state.newName,
            onValueChange = { onIntent(UserProfileIntent.UpdateNewName(it)) },
            placeholder = "Имя",
            barModifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            DialogButton(
                text = "Сохранить",
                onClick = { onIntent(UserProfileIntent.SaveName) }
            )
        }
    }
}

@Composable
private fun EditPasswordLayout(
    state: UserProfileState,
    onIntent: (UserProfileIntent) -> Unit
) {
    Column {
        Text(
            text = "Введите и подтвердите новый пароль",
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
            modifier = Modifier.padding(bottom = 8.dp).align(Alignment.CenterHorizontally)
        )

        CustomTextBar(
            value = state.currentPassword,
            onValueChange = { onIntent(UserProfileIntent.UpdateCurrentPassword(it)) },
            placeholder = "Текущий пароль",
            isPassword = true,
            barModifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        CustomTextBar(
            value = state.newPassword,
            onValueChange = { onIntent(UserProfileIntent.UpdateNewPassword(it)) },
            placeholder = "Новый пароль",
            isPassword = true,
            barModifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        CustomTextBar(
            value = state.confirmPassword,
            onValueChange = { onIntent(UserProfileIntent.UpdateConfirmPassword(it)) },
            placeholder = "Подтвердите новый пароль",
            isPassword = true,
            barModifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            DialogButton(
                text = "Сохранить",
                onClick = { onIntent(UserProfileIntent.SavePassword) }
            )
        }
    }
}
