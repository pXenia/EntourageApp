package com.entourageapp.features.userprofile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.entourageapp.core.ui.EntourageLightBlueGray
import com.entourageapp.core.ui.EntourageRed
import com.entourageapp.core.ui.EntourageTeal
import com.entourageapp.core.ui.components.CustomTextBar

@Composable
fun DeleteAccountDialog(
    state: UserProfileState,
    onIntent: (UserProfileIntent) -> Unit
) {
    Dialog(onDismissRequest = { onIntent(UserProfileIntent.SetDeleteDialogVisibility(isVisible = false)) }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .background(EntourageLightBlueGray)
                .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Удаление аккаунта",
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 22.sp),
                textAlign = TextAlign.Center,
                color = EntourageRed
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Вы уверены, что хотите удалить аккаунт? Это действие необратимо.\nВведите пароль для подтверждения:",
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            CustomTextBar(
                value = state.deleteConfirmPassword,
                onValueChange = { onIntent(UserProfileIntent.UpdateDeleteConfirmPassword(it)) },
                placeholder = "Пароль",
                isPassword = true,
                barModifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = { onIntent(UserProfileIntent.SetDeleteDialogVisibility(isVisible = false)) }) {
                    Text(
                        "Отменить",
                        color = EntourageTeal,
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp)
                    )
                }

                TextButton(onClick = { onIntent(UserProfileIntent.DeleteAccount) }) {
                    Text(
                        "Удалить",
                        color = EntourageRed,
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp)
                    )
                }
            }
        }
    }
}
