package at.sunilson.doistillneedthisthing.domain.shared

import at.sunilson.doistillneedthisthing.shared.domain.shared.CoroutineDispatchers
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.runCatching
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

abstract class UseCase<P, R>(private val dispatcher: CoroutineDispatcher = CoroutineDispatchers.io) {
    protected abstract suspend fun run(params: P): R
    suspend operator fun invoke(params: P): Result<R, Throwable> = withContext(dispatcher) {
        runCatching {
            run(params)
        }
    }
}