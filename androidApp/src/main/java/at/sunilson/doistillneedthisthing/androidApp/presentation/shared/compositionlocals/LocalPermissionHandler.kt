package at.sunilson.doistillneedthisthing.presentation.shared.compositionlocals

import androidx.compose.runtime.compositionLocalOf

val LocalPermissionHandler = compositionLocalOf<PermissionHandler>() {
    error("Permission handler not initialized")
}

interface PermissionHandler {
    fun hasPermissions(vararg permission: String): Boolean
    suspend fun requestPermissions(vararg permissions: String): PermissionResult

    sealed class PermissionResult {
        object Granted : PermissionResult()
        object Denied : PermissionResult()
    }
}
