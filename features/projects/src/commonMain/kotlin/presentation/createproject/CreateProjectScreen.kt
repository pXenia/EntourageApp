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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.entourageapp.core.ui.EntourageBlack
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

@Composable
fun CreateProjectScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    val scrollState = rememberScrollState()
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
                placeholder = "Например, двушка на Ленинском"
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CustomDateField(
                    modifier = Modifier.weight(1f),
                    label = "Дата начала"
                )
                CustomDateField(
                    modifier = Modifier.weight(1f),
                    label = "Дата окончания"
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                CustomTextBar(
                    label = "Площадь, кв. м",
                    placeholder = "90,1",
                    // textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f).padding(end = 4.dp)
                )
                SquareCheckbox(
                    modifier = Modifier.weight(1f).padding(top = 18.dp)
                        .align(Alignment.CenterVertically)
                )
            }
            CustomTextBar(
                label = "Планируемый бюджет, ₽",
                placeholder = "Например, 2 700 000",
                // textAlign = TextAlign.Center
            )
            CustomTextBar(
                label = "Участники",
                placeholder = "user@example.ru",
                trailingIcon = add,
                onTrailingIconClick = {}
            )
            AddUserCard(Modifier.fillMaxWidth(), "Анна Миронова", "редакт.", "annie@example.ru")
            //AddUserCard(Modifier.fillMaxWidth(), "Миша М", "чит.", "mishamironov17@example.ru")
            CustomTextBar(
                label = "Описание",
                placeholder = "Что-то важное\nЧто-то важное",
                modifier = Modifier.fillMaxWidth(),
                isSingleLine = false
            )
            AccentButton(
                modifier = Modifier.padding(bottom = 16.dp).fillMaxWidth().height(56.dp),
                text = "создать",
                containerColor = EntourageBlack,
                contentColor = EntourageWhite
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