package com.entourageapp.features.userprofile.presentation.projectmanagement

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.entourageapp.core.ui.EntourageBlack
import com.entourageapp.core.ui.EntourageLightBlueGray
import com.entourageapp.core.ui.EntouragePeachAlpha80
import com.entourageapp.core.ui.EntourageTeal
import com.entourageapp.core.ui.arrowLeft
import com.entourageapp.core.ui.calendar
import com.entourageapp.core.ui.components.AccentButton
import com.entourageapp.core.ui.components.Avatar
import com.entourageapp.core.ui.components.FloatingButton
import com.entourageapp.core.ui.folder
import com.entourageapp.core.ui.user
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun ProjectManagementScreen(
    onBackClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp)
    ) {
        ProjectManagementHeader(onBackClick = onBackClick)

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 16.dp)
        ) {
            item {
                ManagementSection(title = "ПРИГЛАШЕНИЯ") {
                    InvitationCard(
                        userName = "Михаил Иванов",
                        userEmail = "misha901@gmail.com",
                        projectName = "Квартира на Ленина 42",
                        participantsCount = 2,
                        dateRange = "27.06.2025-22.11.2025",
                        role = "Редактор"
                    )
                }
            }

            item {
                ManagementSection(title = "УДАЛИТЬ ПРОЕКТ") {
                    ProjectActionCard(
                        projectName = "Квартира на Ленинском",
                        participantsCount = 2,
                        dateRange = "20.11.2024-12.03.2026",
                        role = "Владелец",
                        buttonText = "УДАЛИТЬ",
                        buttonColor = EntouragePeachAlpha80
                    )
                }
            }

            item {
                ManagementSection(title = "ВЫЙТИ ИЗ ПРОЕКТА") {
                    ProjectActionCard(
                        projectName = "Дача",
                        participantsCount = 2,
                        dateRange = "21.06.2023-21.06.2025",
                        role = "Читатель",
                        buttonText = "ВЫЙТИ",
                        buttonColor = EntouragePeachAlpha80
                    )
                }
            }
        }
    }
}

@Composable
private fun ProjectManagementHeader(onBackClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FloatingButton(
                onClick = onBackClick,
                icon = arrowLeft
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = "УПРАВЛЕНИЕ УЧАСТИЕМ\nВ ПРОЕКТАХ",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 24.sp
                )
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(thickness = 1.dp, color = EntourageBlack.copy(alpha = 0.2f))
    }
}

@Composable
private fun ManagementSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge.copy(
                color = EntourageTeal,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        )
        content()
    }
}

@Composable
private fun InvitationCard(
    userName: String,
    userEmail: String,
    projectName: String,
    participantsCount: Int,
    dateRange: String,
    role: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = EntourageLightBlueGray.copy(alpha = 0.4f)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Avatar(initials = "МИ", size = 48)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = userName,
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "($userEmail)",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                            color = EntourageBlack.copy(alpha = 0.6f)
                        )
                    }
                    Text(
                        text = "приглашает вас",
                        style = MaterialTheme.typography.bodySmall,
                        color = EntourageTeal
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            ProjectInfoRow(icon = folder, text = projectName)
            ProjectInfoRow(icon = user, text = "$participantsCount участника")
            ProjectInfoRow(icon = calendar, text = dateRange)

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Ваша роль",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.width(8.dp))
                RoleBadge(role)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButtonSimple(text = "Отклонить", onClick = {})
                Spacer(modifier = Modifier.width(12.dp))
                AccentButton(
                    text = "ПРИНЯТЬ",
                    onClick = {},
                    containerColor = EntourageTeal.copy(alpha = 0.4f),
                    contentColor = EntourageBlack,
                    modifier = Modifier.height(40.dp)
                )
            }
        }
    }
}

@Composable
private fun ProjectActionCard(
    projectName: String,
    participantsCount: Int,
    dateRange: String,
    role: String,
    buttonText: String,
    buttonColor: Color
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = EntourageLightBlueGray.copy(alpha = 0.4f)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = projectName.uppercase(),
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = 1.dp,
                color = EntourageBlack.copy(alpha = 0.6f)
            )

            ProjectInfoRow(icon = user, text = "$participantsCount участника")
            ProjectInfoRow(icon = calendar, text = dateRange)

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Ваша роль",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.width(8.dp))
                RoleBadge(role)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                AccentButton(
                    text = buttonText,
                    onClick = {},
                    containerColor = buttonColor,
                    contentColor = EntourageBlack,
                    modifier = Modifier.height(40.dp)
                )
            }
        }
    }
}

@Composable
private fun ProjectInfoRow(icon: DrawableResource, text: String) {
    Row(
        modifier = Modifier.padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = EntourageBlack
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun RoleBadge(role: String) {
    Surface(
        color = EntourageLightBlueGray.copy(alpha = 0.6f),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = role,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
            color = EntourageBlack.copy(alpha = 0.7f)
        )
    }
}

@Composable
private fun TextButtonSimple(text: String, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = EntourageLightBlueGray.copy(alpha = 0.6f)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
