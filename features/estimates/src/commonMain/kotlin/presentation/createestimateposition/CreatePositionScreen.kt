package presentation.createestimateposition

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.entourageapp.core.ui.EntourageBlack
import com.entourageapp.core.ui.EntouragePeachAlpha30
import com.entourageapp.core.ui.EntourageRed
import com.entourageapp.core.ui.EntourageTeal
import com.entourageapp.core.ui.EntourageWhite
import com.entourageapp.core.ui.calculator
import com.entourageapp.core.ui.coins
import com.entourageapp.core.ui.components.AccentButton
import com.entourageapp.core.ui.components.Badge
import com.entourageapp.core.ui.components.CustomDropdownBar
import com.entourageapp.core.ui.components.CustomTextBar
import com.entourageapp.core.ui.components.ScreenTitle
import com.entourageapp.core.ui.cross
import com.entourageapp.core.ui.tag
import com.entourageapp.core.ui.tools.formatTwoDecimals
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CreatePositionScreen(
    projectId: Int,
    onBackClick: () -> Unit,
    viewModel: CreatePositionVM = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.handleIntent(CreatePositionIntent.LoadDictionaries(projectId))
    }
    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) onBackClick()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ScreenTitle(
            modifier = Modifier.fillMaxWidth(),
            title = "Добавление в смету",
            onBackClick = onBackClick
        )

        CustomTextBar(
            value = state.name,
            onValueChange = { viewModel.handleIntent(CreatePositionIntent.UpdateName(it)) },
            label = "Название",
            placeholder = "Например, ламинат",
            errorText = if (state.error?.contains("Название") == true) state.error else null
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CustomDropdownBar(
                modifier = Modifier.weight(1f),
                items = state.availableTypes,
                selectedItem = state.selectedType,
                onItemSelected = { viewModel.handleIntent(CreatePositionIntent.SelectType(it)) },
                itemLabel = { it.name },
                label = "Тип",
                placeholder = "..."
            )
            CustomDropdownBar(
                modifier = Modifier.weight(1f),
                items = state.availableUnits,
                selectedItem = state.selectedUnit,
                onItemSelected = { viewModel.handleIntent(CreatePositionIntent.SelectUnit(it)) },
                itemLabel = { it.name },
                label = "Ед. измерения",
                placeholder = "..."
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CustomTextBar(
                modifier = Modifier.weight(1f),
                value = state.price,
                onValueChange = { viewModel.handleIntent(CreatePositionIntent.UpdatePrice(it)) },
                label = "Цена за ед.",
                isNumeric = true,
                placeholder = "200"
            )
            CustomTextBar(
                modifier = Modifier.weight(1f),
                value = state.quantity,
                onValueChange = { viewModel.handleIntent(CreatePositionIntent.UpdateQuantity(it)) },
                label = "Количество",
                isNumeric = true,
                placeholder = "12"
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "ИЛИ",
                color = EntourageTeal,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Surface(
                color = EntouragePeachAlpha30,
                modifier = Modifier.clip(RoundedCornerShape(32.dp))
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(calculator),
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = EntourageBlack
                    )
                    Text(
                        text = "Рассчитать на калькуляторе",
                        color = EntourageBlack,
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp)
                    )
                }
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "Помещение",
                color = EntourageTeal,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 18.sp),
            )
            HorizontalDivider(color = EntourageBlack, thickness = 1.dp)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Badge(tag, "Детская Ани")
                Surface(
                    color = EntouragePeachAlpha30,
                    modifier = Modifier.clip(CircleShape)
                ) {
                    Icon(
                        modifier = Modifier.padding(12.dp).size(18.dp),
                        painter = painterResource(cross),
                        contentDescription = null,
                        tint = EntourageBlack
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "ИТОГО",
                color = EntourageTeal,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 18.sp),
            )
            Surface(
                color = Color.Transparent,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(32.dp),
                border = BorderStroke(width = 1.dp, color = EntourageBlack)
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {},
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = EntourageTeal.copy(0.2f)
                        )
                    ) {
                        Icon(
                            painter = painterResource(coins),
                            contentDescription = null,
                            modifier = Modifier.padding(8.dp).size(24.dp),
                            tint = EntourageBlack
                        )
                    }
                    Text(
                        text = "${state.total.formatTwoDecimals()} ₽",
                        color = EntourageBlack,
                        style = MaterialTheme.typography.headlineLarge.copy(fontSize = 22.sp),
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        if (state.error != null) {
            Text(
                text = state.error!!,
                color = EntourageRed,
                fontSize = 16.sp,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
        AccentButton(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth()
                .height(56.dp),
            onClick = { viewModel.handleIntent(CreatePositionIntent.Submit(projectId)) },
            text = if (state.isLoading) "ДОБАВЛЕНИЕ..." else "ДОБАВИТЬ В СМЕТУ",
            containerColor = EntourageBlack,
            contentColor = EntourageWhite,
            enabled = !state.isLoading
        )
    }
}