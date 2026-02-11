package com.entourageapp.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey


@Composable
fun NavBottomBar(
    selectedKey: NavKey,
    onSelectKey: (NavKey) -> Unit,
    modifier: Modifier = Modifier
){
    NavigationBar(
        modifier = modifier
    ) {
        topLevelNavItems.forEach { (key, item) ->
            NavigationBarItem(
                selected =  key == selectedKey,
                onClick = { onSelectKey(key as NavKey) },
                icon = { Icon(item.icon, contentDescription = null) },
                label = { Text(item.title) }
            )
        }
    }
}
