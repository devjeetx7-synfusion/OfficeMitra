package com.officemitra.app.feature.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.officemitra.app.core.components.OutlineButton
import com.officemitra.app.core.components.PrimaryButton
import com.officemitra.app.core.components.SecondaryButton
import com.officemitra.app.core.theme.PrimaryLight
import com.officemitra.app.core.theme.TextSecondaryLight

@Composable
fun WelcomeScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToJoinCompany: () -> Unit,
    onNavigateToTermsDemo: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        // Branding
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(PrimaryLight),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Business,
                    contentDescription = "Office Mitra",
                    tint = Color.White,
                    modifier = Modifier.size(44.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Office Mitra",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Smart Workforce Management System",
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondaryLight,
                textAlign = TextAlign.Center
            )
        }

        // Main Actions
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PrimaryButton(
                text = "Log In to Account",
                onClick = onNavigateToLogin
            )

            SecondaryButton(
                text = "Create New Account",
                onClick = onNavigateToRegister
            )

            OutlineButton(
                text = "Join Company with Code",
                onClick = onNavigateToJoinCompany
            )
        }

        // Demo Footer
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            TextButton(onClick = onNavigateToTermsDemo) {
                Text(
                    text = "Privacy Policy & Terms of Service (Demo Links)",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondaryLight
                )
            }
        }
    }
}
