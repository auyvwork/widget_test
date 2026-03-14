package com.example.untitled

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider

class ClickerWidget : GlanceAppWidget() {

    override val stateDefinition = PreferencesGlanceStateDefinition

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val prefs = currentState<Preferences>()
            val count = prefs[intPreferencesKey("count")] ?: 0

            Column(
                modifier = GlanceModifier
                    .fillMaxSize()

                    .background(ColorProvider(Color(0xFF1C1C1E)))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "$count",
                    style = TextStyle(
                        fontSize = 56.sp,
                        fontWeight = FontWeight.Medium,
                        color = ColorProvider(Color.White)
                    ),
                    modifier = GlanceModifier.padding(bottom = 24.dp)
                )

                Button(
                    text = "КЛИК",
                    onClick = actionRunCallback<UpdateCountCallback>()
                )
            }
        }
    }
}

class UpdateCountCallback : ActionCallback {
    override suspend fun onAction(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        updateAppWidgetState(context, PreferencesGlanceStateDefinition, glanceId) { prefs ->
            prefs.toMutablePreferences().apply {
                val key = intPreferencesKey("count")
                this[key] = (this[key] ?: 0) + 1
            }
        }
        ClickerWidget().update(context, glanceId)
    }
}

class ClickerWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget = ClickerWidget()
}