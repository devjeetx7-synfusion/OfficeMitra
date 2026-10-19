package com.officemitra.app.feature.company

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.Business
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.QrCode
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.officemitra.app.core.components.*
import com.officemitra.app.core.theme.PrimaryLight
import com.officemitra.app.core.theme.TextSecondaryLight

@Composable
fun JoinCompanyScreen(
    onNavigateToStatus: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: JoinCompanyViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        OfficeMitraTopBar(
            title = "Join Company",
            showBackButton = true,
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(PrimaryLight.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Business,
                    contentDescription = null,
                    tint = PrimaryLight,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Enter Company Code",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Get your company code from your HR department.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondaryLight
            )

            Spacer(modifier = Modifier.height(16.dp))

            InfoBanner(
                message = "Demo Code: Enter 'OM-10245' to preview Acme Tech Corp. Note: Company Code is for UI demo only and not proof of employment."
            )

            Spacer(modifier = Modifier.height(20.dp))

            AppTextField(
                value = uiState.companyCode,
                onValueChange = viewModel::onCompanyCodeChanged,
                label = "Company Code",
                placeholder = "OM-10245",
                leadingIcon = Icons.Outlined.QrCode,
                isError = uiState.codeError != null,
                errorMessage = uiState.codeError
            )

            // Company Preview Card
            uiState.matchedCompany?.let { company ->
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Company Preview Found", style = MaterialTheme.typography.labelMedium, color = PrimaryLight)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = company.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text(text = company.address, style = MaterialTheme.typography.bodySmall, color = TextSecondaryLight)
                        Text(text = "${company.totalEmployees} Employees", style = MaterialTheme.typography.bodySmall, color = TextSecondaryLight)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            AppTextField(
                value = uiState.employeeId,
                onValueChange = viewModel::onEmployeeIdChanged,
                label = "Employee ID (Optional)",
                placeholder = "EMP-1024",
                leadingIcon = Icons.Outlined.Badge
            )

            Spacer(modifier = Modifier.height(16.dp))

            AppTextField(
                value = uiState.fullName,
                onValueChange = viewModel::onFullNameChanged,
                label = "Full Name",
                leadingIcon = Icons.Outlined.Person
            )

            Spacer(modifier = Modifier.height(16.dp))

            AppTextField(
                value = uiState.mobileNumber,
                onValueChange = viewModel::onMobileChanged,
                label = "Registered Mobile Number",
                leadingIcon = Icons.Outlined.Phone
            )

            Spacer(modifier = Modifier.height(28.dp))

            PrimaryButton(
                text = "Submit Join Request",
                onClick = { viewModel.submitJoinRequest(onNavigateToStatus) }
            )
        }
    }
}
