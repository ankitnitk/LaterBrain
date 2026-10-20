package com.laterbrain.app

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.laterbrain.app.capture.SharedContentParser
import com.laterbrain.app.ui.edit.EditItemScreen
import com.laterbrain.app.ui.edit.EditItemViewModel
import com.laterbrain.app.ui.theme.LaterBrainTheme

/**
 * Opens when the user picks "Save to Later Brain" in another app's Share menu.
 * Shows the quick-save screen pre-filled with what was shared, then closes and
 * returns the user to the app they were in.
 */
class ShareActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent?.action != Intent.ACTION_SEND) {
            finish()
            return
        }
        val text = intent.getCharSequenceExtra(Intent.EXTRA_TEXT)?.toString()
        val subject = intent.getStringExtra(Intent.EXTRA_SUBJECT) ?: intent.getStringExtra(Intent.EXTRA_TITLE)
        if (text.isNullOrBlank() && subject.isNullOrBlank()) {
            Toast.makeText(this, "Nothing to save from this share", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        val shared = SharedContentParser.parse(text, subject)

        enableEdgeToEdge()
        val repository = appContainer.repository
        setContent {
            LaterBrainTheme {
                EditItemScreen(
                    viewModel = viewModel { EditItemViewModel(repository, itemId = null, shared = shared) },
                    fromShare = true,
                    onClose = { finish() },
                    onSaved = {
                        Toast.makeText(this, "Saved. I'll bring it back at the right moment.", Toast.LENGTH_SHORT).show()
                        finish()
                    },
                )
            }
        }
    }
}
