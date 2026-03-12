package presentation.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.entourageapp.core.ui.EntourageBlack
import com.entourageapp.core.ui.EntourageRed
import com.entourageapp.core.ui.EntourageTeal
import com.entourageapp.core.ui.EntourageWhite
import com.entourageapp.core.ui.arrowLeft
import com.entourageapp.core.ui.components.AccentButton
import com.entourageapp.core.ui.components.CustomTextBar
import com.entourageapp.core.ui.components.FloatingButton
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RegisterScreen(
    onBackClick: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onRegistrationSuccess: () -> Unit,
    viewModel: RegisterVM = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FloatingButton(onClick = onBackClick, icon = arrowLeft)

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "регистрация".uppercase(),
            style = MaterialTheme.typography.titleMedium.copy(fontSize = 32.sp)
        )

        Text(
            text = "Создайте аккаунт для начала работы",
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 18.sp,
                color = EntourageTeal
            )
        )

        Spacer(modifier = Modifier.height(40.dp))

        CustomTextBar(
            label = "Имя",
            value = state.name,
            onValueChange = { viewModel.handleIntent(RegisterIntent.OnNameChanged(it)) },
            errorText = state.nameError
        )

        CustomTextBar(
            label = "Эл. почта",
            value = state.email,
            onValueChange = { viewModel.handleIntent(RegisterIntent.OnEmailChanged(it)) },
            errorText = state.emailError
        )

        CustomTextBar(
            label = "Пароль",
            value = state.password,
            onValueChange = { viewModel.handleIntent(RegisterIntent.OnPasswordChanged(it)) },
            errorText = state.passwordError,
            isPassword = true
        )

        CustomTextBar(
            label = "Подтвердить пароль",
            value = state.confirmPassword,
            onValueChange = { viewModel.handleIntent(RegisterIntent.OnConfirmPasswordChanged(it)) },
            errorText = state.confirmPasswordError,
            isPassword = true
        )

        Spacer(modifier = Modifier.height(40.dp))

        AccentButton(
            modifier = Modifier.fillMaxWidth().height(56.dp),
            onClick = { viewModel.handleIntent(RegisterIntent.OnRegisterClicked(onRegistrationSuccess)) },
            text = if (state.isLoading) "регистрация..." else "создать аккаунт",
            containerColor = EntourageBlack,
            contentColor = EntourageWhite,
            enabled = !state.isLoading
        )

        state.generalError?.let {
            Text(
                text = "Возникла ошибка при регистрации",
                color = EntourageRed,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Уже есть аккаунт?",
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp)
            )
            TextButton(
                onClick = onNavigateToLogin,
                contentPadding =  PaddingValues(4.dp)
            ) {
                Text(
                    "Войти",
                    color = EntourageTeal,
                    fontSize = 18.sp,
                    textDecoration = TextDecoration.Underline,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp)
                )
            }
        }
    }
}