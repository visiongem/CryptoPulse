package io.github.visiongem.cryptopulse.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.visiongem.cryptopulse.R
import io.github.visiongem.cryptopulse.domain.AppError
import io.github.visiongem.cryptopulse.ui.theme.CryptoPulseTheme

@Composable
fun ErrorState(
    error: AppError,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = error.localize(),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text(stringResource(R.string.action_retry))
        }
    }
}

@Composable
private fun AppError.localize(): String = when (this) {
    AppError.NoNetwork -> stringResource(R.string.error_no_network)
    AppError.Timeout -> stringResource(R.string.error_timeout)
    is AppError.Api -> stringResource(R.string.error_api, code, message)
    is AppError.Unknown -> stringResource(R.string.error_unknown)
}

@Preview(showBackground = true)
@Composable
private fun ErrorStatePreview() {
    CryptoPulseTheme {
        ErrorState(error = AppError.NoNetwork, onRetry = {})
    }
}
