package com.officemitra.app.feature.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.officemitra.app.core.components.*
import com.officemitra.app.core.theme.AppIcons

@Composable
fun EditProfileScreen(
    onBackClick: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val editState by viewModel.editState.collectAsState()
    // Saved across rotation and process death, so typed changes are not lost.
    var name by rememberSaveable { mutableStateOf(uiState.user.name) }
    var phone by rememberSaveable { mutableStateOf(uiState.user.phone) }
    var edited by rememberSaveable { mutableStateOf(false) }
    val inputsEnabled = !editState.isSaving

    // The profile can still be loading when the screen opens; fill the form once it arrives,
    // unless the user has already started typing.
    LaunchedEffect(uiState.user.id, uiState.user.name, uiState.user.phone) {
        if (!edited) {
            name = uiState.user.name
            phone = uiState.user.phone
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        OfficeMitraTopBar(
            title = "Edit Profile",
            showBackButton = true,
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Update Personal Info",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Email, Role, Company Code, and Employee ID are locked by HR policy.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(20.dp))

                FormTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        edited = true
                        viewModel.onProfileEdited()
                    },
                    label = "Full Name",
                    leadingIcon = AppIcons.Person,
                    isError = editState.nameError != null,
                    errorMessage = editState.nameError,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Next
                    ),
                    enabled = inputsEnabled
                )

                Spacer(modifier = Modifier.height(14.dp))

                FormTextField(
                    // The email address is the login identity and is shown for reference only.
                    value = uiState.user.email,
                    onValueChange = {},
                    label = "Email Address",
                    leadingIcon = AppIcons.Email,
                    readOnly = true,
                    enabled = false
                )

                Spacer(modifier = Modifier.height(14.dp))

                FormTextField(
                    value = phone,
                    onValueChange = {
                        phone = it
                        edited = true
                        viewModel.onProfileEdited()
                    },
                    label = "Mobile Number",
                    leadingIcon = AppIcons.Phone,
                    isError = editState.phoneError != null,
                    errorMessage = editState.phoneError,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Done
                    ),
                    enabled = inputsEnabled
                )

                Spacer(modifier = Modifier.height(20.dp))

                editState.errorMessage?.let { message ->
                    InfoBanner(
                        message = message,
                        icon = AppIcons.Error,
                        backgroundColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
                    )
                }

                if (editState.isSaved) {
                    InfoBanner(
                        message = "Profile updated successfully!",
                        modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
                    )
                }
            }

            PrimaryButton(
                text = if (editState.isSaved) "Done" else "Save Changes",
                isLoading = editState.isSaving,
                onClick = {
                    if (editState.isSaved) {
                        onBackClick()
                    } else {
                        viewModel.updateProfile(name, phone)
                    }
                }
            )
        }
    }
}
