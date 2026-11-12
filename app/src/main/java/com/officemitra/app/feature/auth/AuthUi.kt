package com.officemitra.app.feature.auth

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import com.officemitra.app.core.components.InfoBanner
import com.officemitra.app.core.theme.AppIcons

/** An error message in the existing banner style. Announced by screen readers when it appears. */
@Composable
fun AuthErrorBanner(message: String, modifier: Modifier = Modifier) {
    InfoBanner(
        message = message,
        icon = AppIcons.Error,
        backgroundColor = MaterialTheme.colorScheme.errorContainer,
        contentColor = MaterialTheme.colorScheme.onErrorContainer,
        modifier = modifier.semantics { liveRegion = LiveRegionMode.Polite }
    )
}

/** A confirmation or information message. Announced by screen readers when it appears. */
@Composable
fun AuthInfoBanner(message: String, modifier: Modifier = Modifier) {
    InfoBanner(
        message = message,
        modifier = modifier.semantics { liveRegion = LiveRegionMode.Polite }
    )
}
