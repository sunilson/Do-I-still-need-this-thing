package at.sunilson.doistillneedthisthing.androidApp

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import at.sunilson.doistillneedthisthing.presentation.shared.compositionlocals.PermissionHandler
import kotlinx.coroutines.delay
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MainActivity : AppCompatActivity(), PermissionHandler {

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { isGranted ->
            if (isGranted.none { !it.value }) {
                currentPermissionContinuation?.resume(PermissionHandler.PermissionResult.Granted)
            } else {
                currentPermissionContinuation?.resume(PermissionHandler.PermissionResult.Denied)
            }
            currentPermissionContinuation = null
        }

    var currentPermissionContinuation: Continuation<PermissionHandler.PermissionResult>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*
        setContent {
            DoIStillNeedThisThingTheme {
                Surface(color = MaterialTheme.colors.background) {
                    CompositionLocalProvider(LocalPermissionHandler provides this) {
                        AppContainer()
                    }
                }
            }
        }
         */
    }

    override fun hasPermissions(vararg permission: String): Boolean {
        permission.forEach {
            if (checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED) return false
        }
        return true
    }

    override suspend fun requestPermissions(vararg permissions: String): PermissionHandler.PermissionResult {
        while (currentPermissionContinuation != null) {
            delay(100)
        }
        return suspendCoroutine { continuation ->
            currentPermissionContinuation = continuation
            permissionLauncher.launch(permissions)
        }
    }
}