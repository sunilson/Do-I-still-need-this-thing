package at.sunilson.doistillneedthisthing.shared.domain.shared

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

abstract class FlowUseCase<Params, Result>(protected val dispatcher: CoroutineDispatcher = CoroutineDispatchers.io) {
    protected abstract fun run(params: Params): Flow<Result>
    operator fun invoke(params: Params): Flow<Result> = run(params).flowOn(dispatcher)
}