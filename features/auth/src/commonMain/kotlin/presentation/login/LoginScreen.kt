package com.entourageapp.features.auth.presentation.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.entourageapp.core.ui.EntourageBlack
import com.entourageapp.core.ui.EntourageLightBlueGray
import com.entourageapp.core.ui.EntourageRed
import com.entourageapp.core.ui.EntourageTeal
import com.entourageapp.core.ui.EntourageWhite
import com.entourageapp.core.ui.arrowLeft
import com.entourageapp.core.ui.components.AccentButton
import com.entourageapp.core.ui.components.CustomTextBar
import com.entourageapp.core.ui.components.FloatingButton
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: LoginVM = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    if (state.showForgotPasswordDialog) {
        val clipboardManager = LocalClipboardManager.current
        val email = "support@antourage.com"
        AlertDialog(
            onDismissRequest = { viewModel.handleIntent(LoginIntent.OnDismissForgotPasswordDialog) },
            confirmButton = {
                TextButton(
                    onClick = {
                        clipboardManager.setText(AnnotatedString(email))
                        viewModel.handleIntent(LoginIntent.OnDismissForgotPasswordDialog)
                    }
                ) {
                    Text("Копировать почту", color = EntourageTeal)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.handleIntent(LoginIntent.OnDismissForgotPasswordDialog) }) {
                    Text("Закрыть", color = EntourageBlack)
                }
            },
            title = { 
                Text(
                    "Восстановление доступа",
                    style = MaterialTheme.typography.titleMedium
                ) 
            },
            text = { 
                val annotatedString = buildAnnotatedString {
                    append("Для восстановления пароля, пожалуйста, напишите нам на почту:\n\n")
                    withStyle(
                        style = SpanStyle(
                            color = EntourageTeal,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append(email)
                    }
                    append("\n\nМы поможем вам восстановить доступ к аккаунту.")
                }
                Text(
                    text = annotatedString,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            containerColor = EntourageLightBlueGray,
            shape = RoundedCornerShape(24.dp)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FloatingButton(
            onClick = onBackClick,
            icon = arrowLeft
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "вход".uppercase(),
            style = MaterialTheme.typography.titleMedium.copy(fontSize = 32.sp)
        )
        Text(
            text = "Рады снова видеть вас!",
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 18.sp,
                color = EntourageTeal
            )
        )
        Spacer(modifier = Modifier.height(80.dp))
        CustomTextBar(
            label = "Эл. почта",
            value = state.email,
            onValueChange = { viewModel.handleIntent(LoginIntent.OnEmailChanged(it)) }
        )
        CustomTextBar(
            label = "Пароль",
            value = state.password,
            onValueChange = { viewModel.handleIntent(LoginIntent.OnPasswordChanged(it)) },
            isPassword = true
        )
        Text(
            text = "Забыли пароль?",
            modifier = Modifier
                .align(Alignment.End)
                .padding(end = 16.dp)
                .clickable { viewModel.handleIntent(LoginIntent.OnForgotPasswordClicked) },
            style = MaterialTheme.typography.bodySmall.copy(
                color = EntourageTeal,
                fontSize = 16.sp,
                textDecoration = TextDecoration.Underline
            )
        )
        state.generalError?.let {
            Text(
                text = it,
                color = EntourageRed,
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 8.dp, start = 20.dp)
            )
        }
        Spacer(modifier = Modifier.height(80.dp))
        AccentButton(
            modifier = Modifier.fillMaxWidth().height(56.dp),
            onClick = {
                viewModel.handleIntent(LoginIntent.OnLoginClicked(onLoginSuccess))
            },
            text = if (state.isLoading) "загрузка..." else "войти",
            containerColor = EntourageBlack,
            contentColor = EntourageWhite,
            enabled = !state.isLoading
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Нет аккаунта?",
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp)
            )
            TextButton(
                onClick = onNavigateToRegister,
                contentPadding =  PaddingValues(4.dp)
            ) {
                Text(
                    "Зарегистрироваться",
                    color = EntourageTeal,
                    fontSize = 18.sp,
                    textDecoration = TextDecoration.Underline,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp)
                )
            }
        }
    }
}