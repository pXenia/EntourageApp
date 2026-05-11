package com.entourageapp.features.auth.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.entourageapp.core.ui.EntourageBlack
import com.entourageapp.core.ui.EntourageTeal
import com.entourageapp.core.ui.EntourageWhite
import com.entourageapp.core.ui.appBackground
import com.entourageapp.core.ui.components.AccentButton
import com.entourageapp.core.ui.logo
import org.jetbrains.compose.resources.painterResource

@Composable
fun AuthStartScreen(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .appBackground()
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 160.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .clip(CircleShape)
                    .innerShadow(
                        shape = CircleShape,
                        shadow = Shadow(
                            radius = 36.dp,
                            spread = 8.dp,
                            color = EntourageWhite.copy(alpha = 0.8f),
                            offset = DpOffset(x = 0.dp, 0.dp)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(logo),
                        contentDescription = null,
                        modifier = Modifier.size(100.dp),
                        contentScale = ContentScale.Fit
                    )
                    Text(
                        text = "АНТУРАЖ",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp,
                            color = EntourageBlack
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Организуйте ремонт с вниманием к каждой детали",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 16.sp,
                    color = EntourageTeal,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 150.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AccentButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                text = "Войти",
                onClick = onLoginClick,
                containerColor = EntourageBlack,
                contentColor = EntourageWhite,
            )

            AccentButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                text = "Зарегистрироваться",
                onClick = onRegisterClick,
                containerColor = Color.Transparent,
                contentColor = EntourageBlack,
                shadowColor = EntourageWhite.copy(alpha = 0.8f),
            )
        }
    }
}
