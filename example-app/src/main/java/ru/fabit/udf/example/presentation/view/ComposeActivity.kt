package ru.fabit.udf.example.presentation.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import ru.fabit.udf.example.presentation.store.state.MainEvent
import ru.fabit.udf.example.presentation.view.ui.theme.UDFTheme
import ru.fabit.udf.example.presentation.viewcontroller.ComposeViewController

@AndroidEntryPoint
@OptIn(ExperimentalMaterial3Api::class)
class ComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UDFTheme {
                Scaffold { paddings ->
                    ComposeScreen(paddings)
                }
            }
        }
    }

    /**
     * renderState и renderEvents разделены на две функции, позволяя обрабатывать изменения независимо.
     * Каждая порождает Compose State и не влияет на рекомпозиции пока стейт не будет подписан
     */
    @Composable
    private fun ComposeScreen(
        paddings: PaddingValues,
        viewController: ComposeViewController = hiltViewModel()
    ) {
        val state = viewController.renderState()
        Column(
            modifier = Modifier
                .padding(paddings)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.weight(1f))
            Row(
                modifier = Modifier
                    .padding(32.dp)
                    .clickable {
                        viewController.featureToggled()
                    }
            ) {
                Text(
                    text = if (state.value.featureEnabled == true)
                        "Turn off"
                    else
                        "Turn on",
                    modifier = Modifier.weight(1f)
                )
                Switch(checked = state.value.featureEnabled == true, onCheckedChange = null)
            }
            Text(text = "Hello world")
            Spacer(Modifier.weight(1f))
            Button(onClick = viewController::buttonClick) {
                Text(text = "Counter: ${state.value.counter}")
            }
            Spacer(Modifier.weight(1f))
            val events = viewController.renderEvents()
            events.value.forEach { event ->
                when (event) {
                    is MainEvent.Error -> Toast.makeText(
                        LocalContext.current,
                        event.message,
                        Toast.LENGTH_SHORT
                    ).show()

                    is MainEvent.ExitDialog -> exitDialog()
                }
            }
        }
        if (state.value.isLoading)
            Box(
                modifier = Modifier
                    .background(Color.White.copy(alpha = .5f))
                    .fillMaxSize()
                    .clickable { },
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        BackHandler {
            viewController.backClick()
        }
    }

    private fun exitDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Do you really want to exit?")
            .setNeutralButton("No", { _, _ -> })
            .setPositiveButton("Yes", { _, _ ->
                finish()
            }).show()
    }
}