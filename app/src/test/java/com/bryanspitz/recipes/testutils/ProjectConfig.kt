package com.bryanspitz.recipes.testutils

import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.spec.IsolationMode

@Suppress("unused")
class ProjectConfig : AbstractProjectConfig() {
    override val isolationMode = IsolationMode.InstancePerLeaf
}