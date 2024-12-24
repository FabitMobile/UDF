package ru.fabit.udf.example.presentation.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.switchmaterial.SwitchMaterial
import dagger.hilt.android.AndroidEntryPoint
import ru.fabit.udf.example.R
import ru.fabit.udf.example.presentation.store.state.MainChange
import ru.fabit.udf.example.presentation.store.state.MainEvent
import ru.fabit.udf.example.presentation.store.state.MainState
import ru.fabit.udf.example.presentation.viewcontroller.MainViewController
import ru.fabit.viewcontroller.core.EventsView
import ru.fabit.viewcontroller.coroutines.registerViewController

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), EventsView<MainState, MainEvent> {

    private val viewController by viewModels<MainViewController>()

    private lateinit var text: TextView
    private lateinit var button: Button
    private lateinit var progress: FrameLayout
    private lateinit var featureToggle: SwitchMaterial

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        registerViewController(viewController)
        initUi()
    }

    private fun initUi() {
        text = findViewById(R.id.textView)
        button = findViewById(R.id.button2)
        progress = findViewById(R.id.progress)
        featureToggle = findViewById(R.id.feature_toggle)
        button.setOnClickListener {
            viewController.buttonClick()
        }
        featureToggle.setOnClickListener {
            viewController.featureToggled()
        }
        findViewById<Button>(R.id.compose).setOnClickListener {
            startActivity(Intent(this, ComposeActivity::class.java))
        }
    }

    override fun renderState(state: MainState, events: List<MainEvent>, payload: Any?) {
        events.forEach { event ->
            when (event) {
                is MainEvent.Error -> {
                    Toast.makeText(this, event.message, Toast.LENGTH_SHORT).show()
                }

                MainEvent.ExitDialog -> exitDialog()
            }
        }

        payload as List<MainChange>
        payload.forEach { change ->
            when (change) {
                is MainChange.Text -> text.text = change.text
            }
        }

        progress.isVisible = state.isLoading
        button.text = "Count: ${state.counter}"
        button.isEnabled = state.buttonEnabled
        featureToggle.isChecked = state.featureEnabled == true
        featureToggle.text = if (state.featureEnabled == true)
            "Turn off"
        else
            "Turn on"
    }

    override fun onBackPressed() {
        viewController.backClick()
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