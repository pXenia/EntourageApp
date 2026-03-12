package presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
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
    viewModel: LoginViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberLazyListState()

    Column(
        modifier = Modifier.fillMaxSize(),
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
            label = "Эл. почта"
        )
        CustomTextBar(
            label = "Пароль"
        )
        Spacer(modifier = Modifier.height(80.dp))
        AccentButton(
            modifier = Modifier.padding(bottom = 16.dp).fillMaxWidth().height(56.dp),
            onClick = { },
            text = "войти",
            containerColor = EntourageBlack,
            contentColor = EntourageWhite
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