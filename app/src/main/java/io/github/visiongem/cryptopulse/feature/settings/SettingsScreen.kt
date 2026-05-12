package io.github.visiongem.cryptopulse.feature.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.visiongem.cryptopulse.BuildConfig
import io.github.visiongem.cryptopulse.CryptoPulseApp
import io.github.visiongem.cryptopulse.R
import io.github.visiongem.cryptopulse.ui.theme.CryptoPulseTheme

@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val locator = (context.applicationContext as CryptoPulseApp).serviceLocator

    val viewModel: SettingsViewModel = viewModel(
        factory = SettingsViewModel.factory(
            userPreferencesRepository = locator.userPreferencesRepository,
            versionName = BuildConfig.VERSION_NAME,
        ),
    )
    val state by viewModel.state.collectAsStateWithLifecycle()

    SettingsContent(
        state = state,
        onRippleEnabledChange = viewModel::setRippleEnabled,
        modifier = modifier,
    )
}

@Composable
private fun SettingsContent(
    state: SettingsUiState,
    onRippleEnabledChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        SectionHeader(text = stringResource(R.string.settings_section_appearance))
        SwitchRow(
            title = stringResource(R.string.settings_ripple_title),
            subtitle = stringResource(R.string.settings_ripple_subtitle),
            checked = state.rippleEnabled,
            onCheckedChange = onRippleEnabledChange,
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        SectionHeader(text = stringResource(R.string.settings_section_about))
        InfoRow(
            title = stringResource(R.string.settings_version),
            value = state.versionName,
        )
    }
}

@Composable
private fun SectionHeader(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp),
    )
}

@Composable
private fun SwitchRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.bodyLarge)
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
private fun InfoRow(title: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsContentPreview() {
    CryptoPulseTheme {
        SettingsContent(
            state = SettingsUiState(rippleEnabled = true, versionName = "0.3.0"),
            onRippleEnabledChange = {},
        )
    }
}
