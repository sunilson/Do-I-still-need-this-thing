package at.sunilson.doistillneedthisthing.shared.domain.shared

import kotlinx.coroutines.CoroutineDispatcher

expect object CoroutineDispatchers {
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
}
