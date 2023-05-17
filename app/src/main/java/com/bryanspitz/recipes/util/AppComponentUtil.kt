package com.bryanspitz.recipes.util

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.bryanspitz.recipes.app.AppComponentSource

val Context.appComponent get() = (applicationContext as AppComponentSource).appComponent

@Composable
fun appComponent() = LocalContext.current.appComponent