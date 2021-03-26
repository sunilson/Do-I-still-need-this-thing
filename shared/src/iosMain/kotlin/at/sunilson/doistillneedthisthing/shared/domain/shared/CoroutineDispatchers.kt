package at.sunilson.doistillneedthisthing.shared.domain.shared

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

actual object CoroutineDispatchers {
    actual val main: CoroutineDispatcher = Dispatchers.Default
    actual val io: CoroutineDispatcher = Dispatchers.Default
}