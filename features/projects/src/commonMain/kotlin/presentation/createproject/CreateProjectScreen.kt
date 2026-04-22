package com.entourageapp.features.projects.presentation.createproject

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.entourageapp.core.ui.EntourageBlack
import com.entourageapp.core.ui.EntourageRed
import com.entourageapp.core.ui.EntourageTeal
import com.entourageapp.core.ui.EntourageWhite
import com.entourageapp.core.ui.add
import com.entourageapp.core.ui.components.AccentButton
import com.entourageapp.core.ui.components.CustomDateField
import com.entourageapp.core.ui.components.CustomTextBar
import com.entourageapp.core.ui.components.ScreenTitle
import com.entourageapp.core.ui.cross
import com.entourageapp.core.ui.user
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CreateProjectScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    viewModel: CreateProjectVM = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()
    var showAddUserDialog by remember { mutableStateOf(false) }

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) onBackClick()
    }

    if (showAddUserDialog) {
        AddParticipantDialog(
            email = state.currentParticipantEmail,
            onDismiss = { showAddUserDialog = false },
            onConfirm = { allowEdit ->
                viewModel.handleIntent(
                    CreateProjectIntent.AddParticipant(
                        email = state.currentParticipantEmail,
                        allowEdit = allowEdit
                    )
                )
                showAddUserDialog = false
            }
        )
    }

    Column {
        ScreenTitle(
            modifier = modifier.fillMaxWidth().padding(bottom = 8.dp),
            title = "Создание проекта",
            onBackClick = onBackClick
        )

        Column(
            modifier = modifier.fillMaxSize().verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CustomTextBar(
                label = "Название",
                value = state.title,
                onValueChange = { viewModel.handleIntent(CreateProjectIntent.UpdateTitle(it)) },
                placeholder = "Например, двушка на Ленинском",
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CustomDateField(
                    modifier = Modifier.weight(1f),
                    value = state.startDate,
                    onValueChange = { viewModel.handleIntent(CreateProjectIntent.UpdateStartDate(it)) },
                    label = "Дата начала",
                )
                CustomDateField(
                    modifier = Modifier.weight(1f),
                    value = state.endDate ?: "",
                    onValueChange = { viewModel.handleIntent(CreateProjectIntent.UpdateEndDate(it)) },
                    label = "Дата окончания"
                )
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                CustomTextBar(
                    label = "Площадь, кв. м",
                    placeholder = if (state.isCalculatedSquare) "--" else "Например, 90",
                    modifier = Modifier.weight(1f).padding(end = 4.dp),
                    value = if (state.isCalculatedSquare) "--" else state.square,
                    isEnable = !state.isCalculatedSquare,
                    isNumeric = true,
                    onValueChange = { viewModel.handleIntent(CreateProjectIntent.UpdateSquare(it)) },
                )
                SquareCheckbox(
                    modifier = Modifier.weight(1f).padding(top = 18.dp)
                        .align(Alignment.CenterVertically),
                    isChecked = state.isCalculatedSquare,
                    onCheckedChange = {
                        viewModel.handleIntent(CreateProjectIntent.UpdateIsCalculatedSquare(it))
                    },
                )
            }

            CustomTextBar(
                label = "Планируемый бюджет, ₽",
                placeholder = "Например, 2 700 000",
                value = state.budget,
                isNumeric = true,
                onValueChange = { viewModel.handleIntent(CreateProjectIntent.UpdateBudget(it)) },
            )

            CustomTextBar(
                label = "Участники",
                placeholder = "user@example.ru",
                value = state.currentParticipantEmail,
                onValueChange = {
                    viewModel.handleIntent(CreateProjectIntent.UpdateCurrentParticipantEmail(it))
                },
                trailingIcon = add,
                onTrailingIconClick = {
                    if (state.currentParticipantEmail.isNotBlank()) {
                        showAddUserDialog = true
                    }
                }
            )

            state.pendingParticipants.forEach { participant ->
                AddUserCard(
                    modifier = Modifier.fillMaxWidth(),
                    name = participant.email,
                    role = when (participant.roleCode) {
                        "editor" -> "редакт."
                        "viewer" -> "читатель"
                        else -> participant.roleCode
                    },
                    email = participant.email,
                    onCrossClick = {
                        viewModel.handleIntent(CreateProjectIntent.RemoveParticipant(participant.email))
                    }
                )
            }

            CustomTextBar(
                label = "Описание",
                placeholder = "Что-то важное",
                value = state.description,
                onValueChange = { viewModel.handleIntent(CreateProjectIntent.UpdateDescription(it)) },
                modifier = Modifier.fillMaxWidth(),
                isSingleLine = false
            )

            if (state.error != null) {
                Text(
                    text = state.error!!,
                    color = EntourageRed,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }

            AccentButton(
                modifier = Modifier.padding(bottom = 16.dp).fillMaxWidth().height(56.dp),
                onClick = { viewModel.handleIntent(CreateProjectIntent.Submit) },
                text = if (state.isLoading) "создание..." else "создать",
                containerColor = EntourageBlack,
                contentColor = EntourageWhite,
                enabled = !state.isLoading
            )
        }
    }
}

@Composable
private fun SquareCheckbox(
    modifier: Modifier = Modifier,
    isChecked: Boolean = true,
    onCheckedChange: (Boolean) -> Unit = {}
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.size(36.dp),
            colors = CheckboxDefaults.colors(
                checkedColor = EntourageTeal,
                uncheckedColor = EntourageTeal
            )
        )

        Text(
            text = "рассчитать автоматически".uppercase(),
            maxLines = 2,
            style = MaterialTheme.typography.bodySmall,
            autoSize = TextAutoSize.StepBased(
                minFontSize = 10.sp,
                maxFontSize = 12.sp,
            )
        )
    }
}

@Composable
private fun AddUserCard(
    modifier: Modifier = Modifier,
    name: String,
    role: String,
    email: String,
    onCrossClick: () -> Unit = {}
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(32.dp),
        color = EntourageTeal.copy(alpha = 0.2f),
    ) {
        Row(
            modifier = Modifier.padding(vertical = 4.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                painter = painterResource(user),
                contentDescription = null,
                modifier = Modifier.padding(start = 20.dp).size(24.dp),
            )
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 16.sp)
                    )
                    Text(
                        text = "($role)",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Text(
                    text = email,
                    style = MaterialTheme.typography.headlineSmall.copy(fontSize = 14.sp)
                )
            }
            IconButton(
                onClick = onCrossClick,
                modifier = Modifier.padding(end = 4.dp)
            ) {
                Icon(
                    painter = painterResource(cross),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}