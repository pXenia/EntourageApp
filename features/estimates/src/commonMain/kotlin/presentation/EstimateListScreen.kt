package presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun EstimateListScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        EstimateCard(modifier = Modifier.fillMaxWidth())
    }
}